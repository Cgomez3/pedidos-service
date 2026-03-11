package com.prueba.pedidos.pedidos_service.infraestructure.adapter.in.rest.exception;

import com.prueba.pedidos.pedidos_service.shared.exception.BusinessException;
import com.prueba.pedidos.pedidos_service.shared.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex,
                                                            HttpServletRequest request) {
        String correlationId = (String) request.getAttribute("correlationId");
        ApiError error = new ApiError(
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage(),
                HttpStatus.CONFLICT.value(),
                List.of(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex,
                                                           HttpServletRequest request) {
        String correlationId = (String) request.getAttribute("correlationId");

        ApiError error = new ApiError(
                ErrorCode.ERROR_INTERNO.getCode(),
                ErrorCode.ERROR_INTERNO.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                List.of(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}