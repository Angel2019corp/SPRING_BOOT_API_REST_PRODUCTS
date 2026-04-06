/**
 * 
 */
package com.example.demo.controller.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Clase que implementa el procesamiento de las excepciones 
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validacionesController(MethodArgumentNotValidException ex) {
		List<String> mensajesError = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(  error -> "Error en el campo " +  error.getField() + ", no cumple con: " + error.getDefaultMessage() )
				.collect(Collectors.toList());
		LOG.warn("No cumple validacion: {}", mensajesError);
		return ResponseEntity.badRequest().body(Map.of("Errores", mensajesError));
		
	}

}
