package com.prueba.pedidos.pedidos_service.domain.model;

public class Zona {

    private String id;
    private Boolean soporteRefrigeracion;

    public Zona(String id, Boolean soporteRefrigeracion) {
        this.id = id;
        this.soporteRefrigeracion = soporteRefrigeracion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSoporteRefrigeracion() {
        return soporteRefrigeracion;
    }

    public void setSoporteRefrigeracion(Boolean soporteRefrigeracion) {
        this.soporteRefrigeracion = soporteRefrigeracion;
    }
}
