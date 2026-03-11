package com.prueba.pedidos.pedidos_service.aplicacion.service;


import org.springframework.web.multipart.MultipartFile;

public interface HashService {

    String calcularHash(MultipartFile file);

}
