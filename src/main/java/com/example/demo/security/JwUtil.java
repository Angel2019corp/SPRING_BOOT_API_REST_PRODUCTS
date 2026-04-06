package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwUtil {
	
	@Value("${jwt.secret}")
	private  String secret; 
	
	private final long expirationMs = 1000L * 60 * 60 * 1; 
	
	private  Key key ;
	
	
	@PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
	
	public String generarToken(String userName, String role) {
		Date now = new Date(); 
		Date end = new Date(now.getTime() + expirationMs); 
		
		return Jwts.builder()
				.setSubject(userName)
				.claim("role", role)
				.setIssuedAt(now)
				.setExpiration(end)
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	
	public Jws<Claims> validarToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
	}
	
	public String obtenerUsername(String token ) {
		return validarToken(token).getBody().getSubject();
	}
	
	public String obtenerRole(String token) {
		Object role = validarToken(token).getBody().get("role");
		return role == null ? null : role.toString();
	}
	
	
	
	
}
