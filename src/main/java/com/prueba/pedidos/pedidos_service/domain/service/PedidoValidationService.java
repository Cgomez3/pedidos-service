package com.prueba.pedidos.pedidos_service.domain.service;


import com.prueba.pedidos.pedidos_service.domain.model.Pedido;
import com.prueba.pedidos.pedidos_service.shared.error.ErrorCode;
import com.prueba.pedidos.pedidos_service.shared.exception.BusinessException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

public class PedidoValidationService {

    private static final Set<String> ESTADOS_VALIDOS =
            Set.of("PENDIENTE", "CONFIRMADO", "ENTREGADO");

    public void validarPedido(Pedido pedido) {
        if(pedido.getNumeroPedido() == null || pedido.getNumeroPedido().isBlank())
            throw new BusinessException(ErrorCode.ARCHIVO_INVALIDO);

        // numeroPedido
        if (pedido.getNumeroPedido() == null ||
                !pedido.getNumeroPedido().matches("^[a-zA-Z0-9]+$")) {

            throw new BusinessException(ErrorCode.PEDIDO_ALFANUMERICO);
        }

        // cliente
        if (pedido.getClienteId() == null ||
                pedido.getClienteId().isBlank()) {

            throw new BusinessException(ErrorCode.CLIENTE_OBLIGATORIO);
        }

        // zona
        if (pedido.getZonaId() == null ||
                pedido.getZonaId().isBlank()) {

            throw new BusinessException(ErrorCode.ZONA_OBLIGATORIO);
        }

        // fechaEntrega
        if(pedido.getFechaEntrega() == null)
            throw new BusinessException(ErrorCode.FECHA_INVALIDA);
        LocalDate hoy = LocalDate.now(
                ZoneId.of("America/Lima"));

        if (pedido.getFechaEntrega() == null ||
                pedido.getFechaEntrega().isBefore(hoy)) {

            throw new BusinessException(ErrorCode.FECHA_INVALIDA);
        }

        // estado
        if (pedido.getEstado() == null ||
                !ESTADOS_VALIDOS.contains(pedido.getEstado().name())) {

            throw new BusinessException(ErrorCode.ESTADO_INVALIDO);
        }
    }

}