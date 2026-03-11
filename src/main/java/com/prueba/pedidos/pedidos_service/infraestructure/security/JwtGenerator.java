package com.prueba.pedidos.pedidos_service.infraestructure.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtGenerator {

    @Value("${spring.security.jwt.secret}")
    private static   String SECRET;

    public static String generarToken(){

        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        String token = Jwts.builder()
                .subject("usuario-test")
                .claim("role", "ADMIN")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("TOKEN JWT:");
        System.out.println(token);
        return token;
    }
}