package com.prueba.pedidos.pedidos_service.infraestructure.config;

import com.prueba.pedidos.pedidos_service.aplicacion.service.CargarPedidosService;
import com.prueba.pedidos.pedidos_service.aplicacion.service.HashService;
import com.prueba.pedidos.pedidos_service.aplicacion.service.parser.CsvPedidoParser;
import com.prueba.pedidos.pedidos_service.aplicacion.service.referencia.DatosReferenciaService;
import com.prueba.pedidos.pedidos_service.aplication.port.in.CargarPedidosUseCase;
import com.prueba.pedidos.pedidos_service.aplication.port.out.ClienteRepositoryPort;
import com.prueba.pedidos.pedidos_service.aplication.port.out.IdempotenciaRepositoryPort;
import com.prueba.pedidos.pedidos_service.aplication.port.out.PedidoRepositoryPort;
import com.prueba.pedidos.pedidos_service.aplication.port.out.ZonaRepositoryPort;
import com.prueba.pedidos.pedidos_service.aplication.service.PedidoValidationService;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.out.persistence.adapter.ClienteRepositoryAdapter;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.out.persistence.adapter.IdempotenciaRepositoryAdapter;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.out.persistence.adapter.PedidoRepositoryAdapter;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.out.persistence.adapter.ZonaRepositoryAdapter;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.out.persistence.repository.SpringClienteRepository;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.out.persistence.repository.SpringIdempotenciaRepository;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.out.persistence.repository.SpringPedidoRepository;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.out.persistence.repository.SpringZonaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    PedidoRepositoryPort pedidoRepository(SpringPedidoRepository repo) {
        return new PedidoRepositoryAdapter(repo);
    }

    @Bean
    ClienteRepositoryPort clienteRepository(SpringClienteRepository repo) {
        return new ClienteRepositoryAdapter(repo);
    }

    @Bean
    ZonaRepositoryPort zonaRepository(SpringZonaRepository repo) {
        return new ZonaRepositoryAdapter(repo);
    }

    @Bean
    IdempotenciaRepositoryPort idempotenciaRepository(SpringIdempotenciaRepository repo) {
        return new IdempotenciaRepositoryAdapter(repo);
    }

    @Bean
    PedidoValidationService pedidoValidationService() {
        return new PedidoValidationService();
    }

    @Bean
    CargarPedidosUseCase cargarPedidosUseCase(
            CsvPedidoParser csvParser,
            DatosReferenciaService datosReferenciaService,
            PedidoValidationService validationService,
            PedidoRepositoryPort pedidoRepo,
            IdempotenciaRepositoryPort idempotenciaRepo,
            PedidoBatchProperties batchProperties,
            HashService hashService
    ) {
        return new CargarPedidosService(
                csvParser,
                datosReferenciaService,
                validationService,
                pedidoRepo,
                idempotenciaRepo,
                batchProperties,
                hashService
        );
    }

    @Bean
    CsvPedidoParser csvPedidoParser() {
        return new CsvPedidoParser();
    }

    @Bean
    DatosReferenciaService datosReferenciaService(
            PedidoRepositoryPort pedidoRepo,
            ClienteRepositoryPort clienteRepo,
            ZonaRepositoryPort zonaRepo
    ) {
        return new DatosReferenciaService(
                pedidoRepo,
                clienteRepo,
                zonaRepo
        );
    }
}
