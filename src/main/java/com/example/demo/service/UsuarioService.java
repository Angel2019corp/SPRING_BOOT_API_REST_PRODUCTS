package com.example.demo.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Usuario;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class UsuarioService {
	
	private final UsuarioRepository usuarioRepository; 
	
	private final PasswordEncoder passwordEncoder; 
	
	public UsuarioService( UsuarioRepository usuarioRepository,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository; 
		this.passwordEncoder = passwordEncoder;
	}
	
	public Usuario registrar( UsuarioDTO usrDto,  String client) {
		Usuario usr = new Usuario(); 
		usr.setUsername(usrDto.username().toLowerCase());
		usr.setPasswordHash(passwordEncoder.encode(usrDto.password()));
		usr.setRole(usrDto.role());
		usr.setIpCreated(client);
		usr.setLastAccess(new Timestamp(System.currentTimeMillis()));
		return usuarioRepository.save(usr);
		
	}
	public boolean existeUsuario(UsuarioDTO usr) {
		return usuarioRepository.existsByUsernameAndRole(usr.username().toLowerCase(), usr.role());
		
	}
	public Optional<Usuario> buscarPorUsername(String username) {
		return usuarioRepository.findByUsername(username);
	}
	
	@Transactional
	public void actualizarLastAcces(String username, HttpServletRequest request) {
		usuarioRepository.findByUsername(username).ifPresent(  u -> {
			u.setLastAccess(new Timestamp(System.currentTimeMillis()));
			u.setIpLastAccess(request.getRemoteAddr());
			usuarioRepository.save(u);
		});
	}
	
	public boolean validarPassword (String rawPass, String hash) {
		return passwordEncoder.matches(rawPass, hash);
	}

}
