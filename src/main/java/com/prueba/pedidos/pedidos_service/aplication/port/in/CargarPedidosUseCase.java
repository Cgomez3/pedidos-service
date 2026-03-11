package com.prueba.pedidos.pedidos_service.aplication.port.in;


import com.prueba.pedidos.pedidos_service.aplicacion.dto.ResumenCargaResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CargarPedidosUseCase {

    ResumenCargaResponse cargarPedidos(MultipartFile file, String idempotencyKey);

}