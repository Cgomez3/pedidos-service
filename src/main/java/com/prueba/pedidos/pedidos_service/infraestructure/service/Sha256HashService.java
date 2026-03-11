package com.prueba.pedidos.pedidos_service.infraestructure.service;

import com.prueba.pedidos.pedidos_service.aplicacion.service.HashService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;

@Service
public class Sha256HashService implements HashService {

    @Override
    public String calcularHash(MultipartFile file) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(file.getBytes());

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generando hash SHA-256", e);
        }
    }
}