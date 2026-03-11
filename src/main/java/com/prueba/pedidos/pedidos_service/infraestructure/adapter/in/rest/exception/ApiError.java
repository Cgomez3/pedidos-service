package com.prueba.pedidos.pedidos_service.infraestructure.adapter.in.rest.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private final String code;
    private final String message;
    private final int status;
    private final List<ErrorDetail> details;
    private final String correlationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    public ApiError(String code,
                    String message,
                    int status,
                    List<ErrorDetail> details,
                    String correlationId) {

        this.code = code;
        this.message = message;
        this.status = status;
        this.details = details;
        this.correlationId = correlationId;
        this.timestamp = LocalDateTime.now();
    }

    public String getCode() { return code; }

    public String getMessage() { return message; }

    public int getStatus() { return status; }

    public List<ErrorDetail> getDetails() { return details; }

    public String getCorrelationId() { return correlationId; }

    public LocalDateTime getTimestamp() { return timestamp; }
}