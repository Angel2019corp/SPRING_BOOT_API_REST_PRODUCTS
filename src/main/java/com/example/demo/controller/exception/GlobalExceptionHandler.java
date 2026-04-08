/**
 * 
 */
package com.example.demo.controller.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

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
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> validacionEntidades(EntityNotFoundException ex) {
		LOG.warn("No se encontro la entidad: {}",  ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Errores",ex.getMessage()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> validacionGeneral(Exception ex) {
		LOG.error("No se encontro la entidad: {}",  ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("error","Ocurrio un error. Contactar a soporte"));
		
	}
	

}
