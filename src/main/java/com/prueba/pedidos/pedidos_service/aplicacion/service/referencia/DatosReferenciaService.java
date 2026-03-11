package com.prueba.pedidos.pedidos_service.aplicacion.service.referencia;

import com.prueba.pedidos.pedidos_service.aplicacion.dto.DatosReferencia;
import com.prueba.pedidos.pedidos_service.aplication.model.Pedido;
import com.prueba.pedidos.pedidos_service.aplication.model.Zona;
import com.prueba.pedidos.pedidos_service.aplication.port.out.ClienteRepositoryPort;
import com.prueba.pedidos.pedidos_service.aplication.port.out.PedidoRepositoryPort;
import com.prueba.pedidos.pedidos_service.aplication.port.out.ZonaRepositoryPort;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DatosReferenciaService {

    private final PedidoRepositoryPort pedidoRepository;
    private final ClienteRepositoryPort clienteRepository;
    private final ZonaRepositoryPort zonaRepository;

    public DatosReferenciaService(
            PedidoRepositoryPort pedidoRepository,
            ClienteRepositoryPort clienteRepository,
            ZonaRepositoryPort zonaRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.zonaRepository = zonaRepository;
    }

    public DatosReferencia cargarDatos(List<Pedido> pedidos) {

        Set<String> numerosPedido = pedidos.stream()
                .map(Pedido::getNumeroPedido)
                .collect(Collectors.toSet());

        Set<String> clientesIds = pedidos.stream()
                .map(Pedido::getClienteId)
                .collect(Collectors.toSet());

        Set<String> zonasIds = pedidos.stream()
                .map(Pedido::getZonaId)
                .collect(Collectors.toSet());

        Set<String> pedidosExistentes = pedidoRepository.obtenerNumerosPedidoExistentes(numerosPedido);

        Set<String> clientesExistentes = clienteRepository.obtenerClientesExistentes(clientesIds);

        Map<String, Zona> zonas = zonaRepository.obtenerZonasPorIds(zonasIds);

        return new DatosReferencia(
                pedidosExistentes,
                clientesExistentes,
                zonas
        );
    }
}