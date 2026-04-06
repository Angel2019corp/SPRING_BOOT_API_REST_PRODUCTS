/**
 * 
 */
package com.example.demo.dto;
import jakarta.validation.constraints.*;
/**
 * 
 */
public record UsuarioDTO(
		@NotBlank(message = "El nombre de usuario es obligatorio")
		@Size(min = 5, max = 50, message = "Longitud entre 5 y 50 para el nombre de usuario")
		String username,
		
		@NotBlank(message = "Ingresar Contraseña")
		@Size(min = 5, max = 20, message = "Longitud entre 5 y 20 para la contraseña")
		String password,
		
		@NotBlank(message = "Ingresar rol del usuario")
		@Size(min = 2, max = 10, message = "Longitud entre 2 y 10 para el rol de usuario")
		String role
	) {
}
