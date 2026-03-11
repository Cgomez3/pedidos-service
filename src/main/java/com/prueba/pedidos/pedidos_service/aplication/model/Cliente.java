package com.prueba.pedidos.pedidos_service.aplication.model;


public class Cliente {

    private String id;

    private Boolean activo;

    public Cliente() {
    }

    public Cliente(String id, Boolean activo) {
        this.id = id;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}