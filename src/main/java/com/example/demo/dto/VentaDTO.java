/**
 * 
 */
package com.example.demo.dto;
import jakarta.validation.constraints.*;
/**
 * 
 */
public record VentaDTO(
		
		@NotNull(message = "El identificador del producto es obligatorio")
		Long productoId,
		
		
		@NotNull(message = "Especificar la cantidad de productos")
		@Positive
		Integer cantidad
		) {

	
}
