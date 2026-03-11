package com.prueba.pedidos.pedidos_service.shared.error;

import com.prueba.pedidos.pedidos_service.aplication.error.TipoErrorCarga;

public enum ErrorCode {

    CARGA_DUPLICADA(
            "PED-001",
            "La carga ya fue procesada anteriormente",
            TipoErrorCarga.OTROS
    ),

    ARCHIVO_INVALIDO(
            "PED-002",
            "El archivo enviado no es válido",
            TipoErrorCarga.OTROS
    ),

    DUPLICADO(
            "PED-003",
            "El numeroPedido ya existe: %s",
            TipoErrorCarga.DUPLICADO
    ),

    CLIENTE_NO_EXISTE(
            "PED-004",
            "Cliente no existe",
            TipoErrorCarga.CLIENTE_NO_ENCONTRADO
    ),

    ZONA_NO_EXISTE(
            "PED-005",
            "Zona no existe",
            TipoErrorCarga.ZONA_INVALIDA
    ),

    FECHA_INVALIDA(
            "PED-006",
            "La fecha de entrega no puede ser pasada",
            TipoErrorCarga.FECHA_INVALIDA
    ),

    ESTADO_INVALIDO(
            "PED-007",
            "El estado del pedido es inválido",
            TipoErrorCarga.ESTADO_INVALIDO
    ),

    CADENA_FRIO_NO_SOPORTADA(
            "PED-008",
            "La zona no soporta cadena de frío para el pedido: %s",
            TipoErrorCarga.CADENA_FRIO_NO_SOPORTADA
    ),

    ZONA_OBLIGATORIO(
            "PED-009",
            "Zona es obligatoria",
            TipoErrorCarga.OTROS
    ),

    CLIENTE_OBLIGATORIO(
            "PED-010",
            "Cliente es obligatorio",
            TipoErrorCarga.OTROS
    ),

    PEDIDO_ALFANUMERICO(
            "PED-011",
            "numeroPedido debe ser alfanumérico",
            TipoErrorCarga.OTROS
    ),

    CSV_INVALIDO(
            "PED-012",
            "Error leyendo CSV",
            TipoErrorCarga.OTROS
    ),

    ERROR_INTERNO(
            "PED-500",
            "Error interno del sistema",
            TipoErrorCarga.OTROS
    );

    private final String code;
    private final String message;
    private final TipoErrorCarga tipoError;

    ErrorCode(String code, String message, TipoErrorCarga tipoError) {
        this.code = code;
        this.message = message;
        this.tipoError = tipoError;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public TipoErrorCarga getTipoError() {
        return tipoError;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}