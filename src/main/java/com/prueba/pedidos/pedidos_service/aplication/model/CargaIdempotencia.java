package com.prueba.pedidos.pedidos_service.aplication.model;

import java.time.LocalDateTime;

public class CargaIdempotencia {
    private String id;

    private String idempotencyKey;

    private String archivoHash;

    private LocalDateTime createdAt;

    public CargaIdempotencia() {
    }

    public CargaIdempotencia(String idempotencyKey, String archivoHash, LocalDateTime createdAt) {
        this.idempotencyKey = idempotencyKey;
        this.archivoHash = archivoHash;
        this.createdAt = createdAt;
    }

    public CargaIdempotencia(String id, String idempotencyKey, String archivoHash, LocalDateTime createdAt) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.archivoHash = archivoHash;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getArchivoHash() {
        return archivoHash;
    }

    public void setArchivoHash(String archivoHash) {
        this.archivoHash = archivoHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
