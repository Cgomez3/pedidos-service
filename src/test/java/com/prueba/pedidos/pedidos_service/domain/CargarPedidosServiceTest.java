package com.prueba.pedidos.pedidos_service.domain;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import com.prueba.pedidos.pedidos_service.aplicacion.dto.DatosReferencia;
import com.prueba.pedidos.pedidos_service.aplicacion.dto.ResumenCargaResponse;
import com.prueba.pedidos.pedidos_service.aplicacion.service.CargarPedidosService;
import com.prueba.pedidos.pedidos_service.aplicacion.service.HashService;
import com.prueba.pedidos.pedidos_service.aplicacion.service.parser.CsvPedidoParser;
import com.prueba.pedidos.pedidos_service.aplicacion.service.referencia.DatosReferenciaService;
import com.prueba.pedidos.pedidos_service.domain.model.EstadoPedido;
import com.prueba.pedidos.pedidos_service.domain.model.Pedido;
import com.prueba.pedidos.pedidos_service.domain.model.Zona;
import com.prueba.pedidos.pedidos_service.domain.port.out.IdempotenciaRepositoryPort;
import com.prueba.pedidos.pedidos_service.domain.port.out.PedidoRepositoryPort;
import com.prueba.pedidos.pedidos_service.domain.service.PedidoValidationService;
import com.prueba.pedidos.pedidos_service.infraestructure.config.PedidoBatchProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

class CargarPedidosServiceTest {

    @Mock
    private CsvPedidoParser csvParser;

    @Mock
    private HashService hashService;

    @Mock
    private IdempotenciaRepositoryPort idempotenciaRepository;

    @Mock
    private DatosReferenciaService datosReferenciaService;

    @Mock
    private PedidoValidationService validationService;

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private PedidoBatchProperties batchProperties;

    private CargarPedidosService useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(batchProperties.getBatchSize()).thenReturn(10);

        useCase = new CargarPedidosService(
                csvParser,
                datosReferenciaService,
                validationService,
                pedidoRepository,
                idempotenciaRepository,
                batchProperties,
                hashService
        );
    }

    @Test
    void deberiaGuardarPedidosValidosConIdempotencia() throws Exception {
        // CSV en memoria
        String csvContent = "numeroPedido,clienteId,fechaEntrega,estado,zonaId,requiereRefrigeracion\n" +
                "P001,CLI-123,2025-08-10,PENDIENTE,Z1,true\n" +
                "P002,CLI-999,2025-08-12,ENTREGADO,Z2,false";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getOriginalFilename()).thenReturn("pedidos.csv");
        when(file.getSize()).thenReturn((long) csvContent.length());

        // Crear pedidos simulados
        Pedido pedido1 = new Pedido();
        pedido1.setNumeroPedido("P001");
        pedido1.setClienteId("CLI-123");
        pedido1.setZonaId("Z1");
        pedido1.setFechaEntrega(LocalDate.parse("2025-08-10"));
        pedido1.setEstado(EstadoPedido.PENDIENTE);
        pedido1.setRequiereRefrigeracion(true);

        Pedido pedido2 = new Pedido();
        pedido2.setNumeroPedido("P002");
        pedido2.setClienteId("CLI-999");
        pedido2.setZonaId("Z2");
        pedido2.setFechaEntrega(LocalDate.parse("2025-08-12"));
        pedido2.setEstado(EstadoPedido.ENTREGADO);
        pedido2.setRequiereRefrigeracion(false);

        List<Pedido> pedidosProcesados = List.of(pedido1,pedido2);
        when(csvParser.parse(file)).thenReturn(pedidosProcesados);
        // Mock HashService
        when(hashService.calcularHash(file)).thenReturn("hash123");

        // Mock IdempotenciaRepositoryPort
        when(idempotenciaRepository.buscarPorClave("key")).thenReturn(Optional.empty());
        doNothing().when(idempotenciaRepository).guardarCarga(any());

        // Mock DatosReferenciaService
        DatosReferencia datos = new DatosReferencia(
                Set.of(), // pedidos existentes
                Set.of("CLI-123", "CLI-999"), // clientes existentes
                Map.of(
                        "Z1", new Zona("Z1", true),
                        "Z2", new Zona("Z2", true)
                )
        );

        when(datosReferenciaService.cargarDatos(pedidosProcesados)).thenReturn(datos);

        // Mock ValidationService
        doNothing().when(validationService).validarPedido(any());

        // Ejecutar UseCase
        ResumenCargaResponse response = useCase.cargarPedidos(file, "000223");

        // Validaciones
        assertEquals(2, response.getTotalProcesados());
        assertEquals(2, response.getGuardados());
        assertEquals(0, response.getConError());

        // Verificaciones
        verify(csvParser).parse(any());
        verify(hashService).calcularHash(file);
        verify(idempotenciaRepository).buscarPorClave("000223");
        verify(idempotenciaRepository).guardarCarga(any());
        verify(datosReferenciaService).cargarDatos(pedidosProcesados);
        verify(validationService, times(2)).validarPedido(any());
        verify(pedidoRepository).guardarTodos(anyList());
    }
}