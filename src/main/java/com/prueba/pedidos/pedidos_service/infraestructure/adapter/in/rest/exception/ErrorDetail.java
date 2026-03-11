package com.prueba.pedidos.pedidos_service.infraestructure.adapter.in.rest.exception;

public class ErrorDetail {

    private final String field;
    private final String error;

    public ErrorDetail(String field, String error) {
        this.field = field;
        this.error = error;
    }

    public String getField() {
        return field;
    }

    public String getError() {
        return error;
    }
}
