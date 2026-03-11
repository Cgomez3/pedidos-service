package com.prueba.pedidos.pedidos_service.infraestructure.config;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.pedidos.pedidos_service.infraestructure.adapter.in.rest.exception.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final String SECRET;


    public SecurityConfig(@Value("${spring.security.jwt.secret}") String secret) {
        this.SECRET = secret;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/pedidos/token",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"

                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt -> {
                                })
                                .authenticationEntryPoint((request, response, ex) -> {

                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json");
                                    String correlationId = request.getHeader("correlationId");
                                    if (correlationId == null || correlationId.isEmpty()) {
                                        correlationId = UUID.randomUUID().toString();
                                    }

                                    ApiError error = new ApiError(
                                            "TOKEN_INVALIDO",
                                            "El token JWT es inválido o expirado",
                                            HttpServletResponse.SC_UNAUTHORIZED,
                                            List.of(),
                                            correlationId
                                    );

                                    ObjectMapper mapper = new ObjectMapper();
                                    mapper.findAndRegisterModules();
                                    response.getWriter().write(mapper.writeValueAsString(error));
                                })
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKeySpec secretKey = new SecretKeySpec(
                SECRET.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}