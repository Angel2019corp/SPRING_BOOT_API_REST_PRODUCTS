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
		
		@NotBlank(message = "Contraseña obligatoria")
		@Size(min = 5, max = 20, message = "Longitud entre 5 y 20 para la contraseña")
		String password,
		
		@NotBlank(message = "Ingresar rol del usuario")
		@Pattern(regexp="^(USER|VENDOR|WAREHOUSE)$", message = "Rol restringido, contactar a soporte")
		String role
	) {
}
