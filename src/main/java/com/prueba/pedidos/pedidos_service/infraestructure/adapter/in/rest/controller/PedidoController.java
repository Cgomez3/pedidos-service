package com.prueba.pedidos.pedidos_service.infraestructure.adapter.in.rest.controller;

import com.prueba.pedidos.pedidos_service.aplication.port.in.CargarPedidosUseCase;
import com.prueba.pedidos.pedidos_service.aplicacion.dto.ResumenCargaResponse;
import com.prueba.pedidos.pedidos_service.infraestructure.security.JwtGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final CargarPedidosUseCase useCase;
    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

    @PostMapping(value = "/cargar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Carga masiva de pedidos desde archivo CSV",
            description = "Procesa un archivo CSV de pedidos aplicando validaciones e idempotencia"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carga procesada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "401", description = "Token inválido"),
            @ApiResponse(responseCode = "409", description = "Carga duplicada")
    })
    public ResponseEntity<ResumenCargaResponse> cargarPedidos(
            @Parameter(description = "Clave de idempotencia para evitar reprocesos", required = true)
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Parameter(description = "Archivo CSV con los pedidos")
            @RequestParam("file") MultipartFile file) {

        log.info("Solicitud carga pedidos recibida. idempotencyKey={}, fileName={}, size={}",
                idempotencyKey,
                file.getOriginalFilename(),
                file.getSize());

        ResumenCargaResponse response = useCase.cargarPedidos(file, idempotencyKey);

        log.info("Carga pedidos procesada. total={}, exitosos={}, errores={}",
                response.getTotalProcesados(),
                response.getGuardados(),
                response.getConError());

        return ResponseEntity.ok(
                response
        );
    }

    @GetMapping("/token")
    public ResponseEntity<String> generarToken() {
        return ResponseEntity.ok(JwtGenerator.generarToken());
    }
}
