package com.example.userapi.adapter.in.handler;

import com.example.userapi.domain.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("Mensagem:", ex.getMessage());
        body.put("Status:", ex.getStatus().value());
        return ResponseEntity.status(ex.getStatus().value()).body(body);

    }
}
