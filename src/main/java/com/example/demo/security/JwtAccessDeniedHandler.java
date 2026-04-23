/**
 * 
 */
package com.example.demo.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler{
	
	private final ObjectMapper mapper;
    
    public JwtAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    } 

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpStatus.FORBIDDEN.value());                                                                                                                                                                    
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(),                                                                                                                                                                              
            Map.of("error", "Acceso denegado. Rol insuficiente."));
		
	}

}
