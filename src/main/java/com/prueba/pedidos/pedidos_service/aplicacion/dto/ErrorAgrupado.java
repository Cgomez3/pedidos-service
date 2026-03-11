package com.prueba.pedidos.pedidos_service.aplicacion.dto;

import com.prueba.pedidos.pedidos_service.aplication.error.TipoErrorCarga;

public class ErrorAgrupado {

    private TipoErrorCarga tipoError;
    private int cantidad;

    public ErrorAgrupado(TipoErrorCarga tipoError, int cantidad) {
        this.tipoError = tipoError;
        this.cantidad = cantidad;
    }

    public TipoErrorCarga getTipoError() { return tipoError; }

    public int getCantidad() { return cantidad; }
}
