package com.prueba.pedidos.pedidos_service.aplicacion.dto;

import com.prueba.pedidos.pedidos_service.domain.error.TipoErrorCarga;

public class ErrorCargaResponse {

    private int fila;
    private String numeroPedido;
    private String codigo;
    private String mensaje;
    private TipoErrorCarga tipo;

    public ErrorCargaResponse(int fila,
                              String numeroPedido,
                              String codigo,
                              String mensaje,
                              TipoErrorCarga tipo) {

        this.fila = fila;
        this.numeroPedido = numeroPedido;
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public TipoErrorCarga getTipo() {
        return tipo;
    }
}