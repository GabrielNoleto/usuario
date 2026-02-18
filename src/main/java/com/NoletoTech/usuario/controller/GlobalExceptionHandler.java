package com.NoletoTech.usuario.controller;


import com.NoletoTech.usuario.infrastructure.exceptions.ConflictException;
import com.NoletoTech.usuario.infrastructure.exceptions.IllegalArgumentException;
import com.NoletoTech.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.NoletoTech.usuario.infrastructure.exceptions.UnauthorizedException;
import com.NoletoTech.usuario.infrastructure.exceptions.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
            return  ResponseEntity.status( HttpStatus.NOT_FOUND).body(buildError(
                    HttpStatus.NOT_FOUND.value(),
                    ex.getMessage(),
                    request.getRequestURI(),
                    "Not Found"));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflictException (ConflictException ex, HttpServletRequest request){
        return  ResponseEntity.status( HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Not Found"));
    }

    @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    private ErrorResponseDTO buildError(int status, String mensagem, String path, String error) {
        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(mensagem)
                .path(path)
                .build();
    }
    }
