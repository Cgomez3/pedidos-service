package com.prueba.pedidos.pedidos_service.aplicacion.service;

import com.prueba.pedidos.pedidos_service.aplicacion.dto.*;
import com.prueba.pedidos.pedidos_service.aplicacion.service.parser.CsvPedidoParser;
import com.prueba.pedidos.pedidos_service.aplicacion.service.referencia.DatosReferenciaService;
import com.prueba.pedidos.pedidos_service.aplication.error.TipoErrorCarga;
import com.prueba.pedidos.pedidos_service.aplication.model.CargaIdempotencia;
import com.prueba.pedidos.pedidos_service.aplication.model.Pedido;
import com.prueba.pedidos.pedidos_service.aplication.model.Zona;
import com.prueba.pedidos.pedidos_service.aplication.port.in.CargarPedidosUseCase;
import com.prueba.pedidos.pedidos_service.aplication.port.out.IdempotenciaRepositoryPort;
import com.prueba.pedidos.pedidos_service.aplication.port.out.PedidoRepositoryPort;
import com.prueba.pedidos.pedidos_service.aplication.service.PedidoValidationService;
import com.prueba.pedidos.pedidos_service.infraestructure.config.PedidoBatchProperties;
import com.prueba.pedidos.pedidos_service.shared.error.ErrorCode;
import com.prueba.pedidos.pedidos_service.shared.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.*;
import java.util.stream.Collectors;

public class CargarPedidosService implements CargarPedidosUseCase {

    private final CsvPedidoParser csvParser;
    private final DatosReferenciaService datosReferenciaService;
    private final PedidoValidationService validationService;
    private final PedidoRepositoryPort pedidoRepository;
    private final IdempotenciaRepositoryPort idempotenciaRepository;
    private final PedidoBatchProperties batchProperties;
    private  final HashService hashService;
    private static final Logger log = LoggerFactory.getLogger(CargarPedidosService.class);

    public CargarPedidosService(
            CsvPedidoParser csvParser,
            DatosReferenciaService datosReferenciaService,
            PedidoValidationService validationService,
            PedidoRepositoryPort pedidoRepository,
            IdempotenciaRepositoryPort idempotenciaRepository,
            PedidoBatchProperties batchProperties,
            HashService hashService) {

        this.csvParser = csvParser;
        this.datosReferenciaService = datosReferenciaService;
        this.validationService = validationService;
        this.pedidoRepository = pedidoRepository;
        this.idempotenciaRepository = idempotenciaRepository;
        this.batchProperties=batchProperties;
        this.hashService=hashService;
    }

    @Override
    public ResumenCargaResponse cargarPedidos(MultipartFile file, String idempotencyKey) {

        long start = System.currentTimeMillis();

        log.info("Iniciando carga de pedidos. idempotencyKey={}, fileName={}, size={}",
                idempotencyKey,
                file.getOriginalFilename(),
                file.getSize());

        // 1 Parsear CSV
        List<Pedido> pedidos = csvParser.parse(file);
        int batchSize = batchProperties.getBatchSize();
        log.info("CSV parseado correctamente. totalPedidos={}", pedidos.size());
        String hashArchivo = hashService.calcularHash(file);
        Optional<CargaIdempotencia> cargaExistente =
                idempotenciaRepository.buscarPorClave(idempotencyKey);

        if (cargaExistente.isPresent()) {
            log.warn("IdempotencyKey ya registrada. idempotencyKey={}", idempotencyKey);
            if (cargaExistente.get().getArchivoHash().equals(hashArchivo)) {
                log.warn("Carga duplicada detectada. idempotencyKey={}, hash={}",
                        idempotencyKey, hashArchivo);
                throw new BusinessException(ErrorCode.CARGA_DUPLICADA);

            }

        }
        log.info("Datos de referencia cargados correctamente");

        // 2️ Cargar datos de referencia
        DatosReferencia datos = datosReferenciaService.cargarDatos(pedidos);

        List<Pedido> pedidosValidos = new ArrayList<>();
        List<ErrorFila> erroresFila = new ArrayList<>();

        int fila = 1;

        for (Pedido pedido : pedidos) {

            try {

                validationService.validarPedido(pedido);

                validarDatosReferencia(pedido, datos);

                pedidosValidos.add(pedido);

                //guardar por batch
                if(pedidosValidos.size() >= batchSize){
                    log.info("Guardando batch de pedidos. batchSize={}", pedidosValidos.size());
                    pedidoRepository.guardarTodos(pedidosValidos);
                    pedidosValidos.clear();
                }

            } catch (BusinessException e) {
                log.debug("Error validando fila {} pedido {}: {}",
                        fila,
                        pedido.getNumeroPedido(),
                        e.getMessage());
                erroresFila.add(new ErrorFila(
                        fila,
                        pedido.getNumeroPedido(),
                        e.getErrorCode().getCode(),
                        e.getMessage(),
                        e.getErrorCode().getTipoError()
                ));
            }

            fila++;
        }

        // 3️ Guardar pedidos válidos
        if (!pedidosValidos.isEmpty() ) {
            log.info("Guardando último batch. cantidad={}", pedidosValidos.size());
            pedidoRepository.guardarTodos(pedidosValidos);
        }

        // 4️ Registrar idempotencia
        idempotenciaRepository.guardarCarga(new CargaIdempotencia(idempotencyKey, hashArchivo, LocalDateTime.now()));
        log.info("Registro de idempotencia guardado. idempotencyKey={}", idempotencyKey);
        // 5️ Agrupar errores
        List<ErrorAgrupado> erroresAgrupados = agruparErrores(erroresFila);
        long duration = System.currentTimeMillis() - start;

        log.info("Carga de pedidos finalizada. total={}, errores={}, tiempo={}ms",
                pedidos.size(),
                erroresFila.size(),
                duration);
        // 6️ Construir respuesta
        return new ResumenCargaResponse(
                pedidos.size(),
                pedidosValidos.size(),
                erroresFila.size(),
                erroresFila,
                erroresAgrupados
        );
    }

    private void validarDatosReferencia(Pedido pedido, DatosReferencia datos) {

        if (datos.getPedidosExistentes().contains(pedido.getNumeroPedido())) {
            throw new BusinessException(ErrorCode.DUPLICADO, pedido.getNumeroPedido());
        }

        if (!datos.getClientesExistentes().contains(pedido.getClienteId())) {
            throw new BusinessException(ErrorCode.CLIENTE_NO_EXISTE);
        }

        Zona zona = datos.getZonas().get(pedido.getZonaId());

        if (zona == null) {
            throw new BusinessException(ErrorCode.ZONA_NO_EXISTE);
        }

        if (Boolean.TRUE.equals(pedido.getRequiereRefrigeracion())
                && !Boolean.TRUE.equals(zona.getSoporteRefrigeracion())) {

            throw new BusinessException(
                    ErrorCode.CADENA_FRIO_NO_SOPORTADA,
                    pedido.getNumeroPedido()
            );
        }
    }

    private List<ErrorAgrupado> agruparErrores(List<ErrorFila> errores) {

        Map<TipoErrorCarga, Long> agrupados = errores.stream()
                .collect(Collectors.groupingBy(
                        ErrorFila::getTipo,
                        Collectors.counting()
                ));

        List<ErrorAgrupado> resultado = new ArrayList<>();

        for (Map.Entry<TipoErrorCarga, Long> entry : agrupados.entrySet()) {

            resultado.add(new ErrorAgrupado(
                    entry.getKey(),
                    entry.getValue().intValue()
            ));
        }

        return resultado;
    }
}






































