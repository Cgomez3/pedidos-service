package com.prueba.pedidos.pedidos_service.aplicacion.dto;

import com.prueba.pedidos.pedidos_service.domain.model.Zona;

import java.util.Map;
import java.util.Set;

public class DatosReferencia {

    private Set<String> pedidosExistentes;
    private Set<String> clientesExistentes;
    private Map<String, Zona> zonas;

    public DatosReferencia(Set<String> pedidosExistentes,
                           Set<String> clientesExistentes,
                           Map<String, Zona> zonas) {

        this.pedidosExistentes = pedidosExistentes;
        this.clientesExistentes = clientesExistentes;
        this.zonas = zonas;
    }

    public Set<String> getPedidosExistentes() {
        return pedidosExistentes;
    }

    public Set<String> getClientesExistentes() {
        return clientesExistentes;
    }

    public Map<String, Zona> getZonas() {
        return zonas;
    }
}
