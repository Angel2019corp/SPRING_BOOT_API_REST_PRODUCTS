package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Usuario;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.security.JwUtil;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {
	private final UsuarioService usuarioService; 
	private final JwUtil jwUtil; 
	
	public AuthController( UsuarioService usuarioService, JwUtil jwUtil  ) {
		this.jwUtil = jwUtil;
		this.usuarioService = usuarioService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register (@RequestBody @Valid UsuarioDTO newUser, HttpServletRequest request) {
		
		if (  usuarioService.existeUsuario(newUser) ) {
			return ResponseEntity.badRequest().body(  Map.of( "error", "usuario existente" ));
		}
		
		Usuario usrSaved = usuarioService.registrar(newUser, request.getRemoteAddr());
		
		return ResponseEntity.ok(  Map.of("respuesta","usuario creado", "username", usrSaved.getUsername()  ));
		
		
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login ( @RequestBody Map<String, String> body ,HttpServletRequest request ) {
		String username = body.get("username"); 
		String password = body.get("pass");
		if ( username == null  || password == null  ) {
			return ResponseEntity.badRequest().body( Map.of( "Error", "faltan datos"));
		}
		return usuarioService.buscarPorUsername(username)
				.map( usr -> {
					if ( usuarioService.validarPassword( password, usr.getPasswordHash())  ) {
						usuarioService.actualizarLastAcces(username, request);
						String token = jwUtil.generarToken(usr.getUsername(), usr.getRole());
						return ResponseEntity.ok(  Map.of( "token", token) );
					} else {
						return ResponseEntity.status(401).body( Map.of("error", "datos incorrectos"));
					}
					
				})
				.orElseGet( ()  -> ResponseEntity.status(401).body(Map.of("error","datos incorrectos")));
	}
}
