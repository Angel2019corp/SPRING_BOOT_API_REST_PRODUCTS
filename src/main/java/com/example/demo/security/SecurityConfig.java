package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final JwtAuthenticationEntryPoint entryPoint;
	private final JwtAccessDeniedHandler accessDenied;

	public SecurityConfig(JwtFilter jwtFilter,JwtAuthenticationEntryPoint entryPoint,JwtAccessDeniedHandler accessDenied) {
		this.jwtFilter = jwtFilter;
		this.entryPoint = entryPoint;
		this.accessDenied = accessDenied;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/productos/**").hasAnyRole("ADMIN", "VENDOR","WAREHOUSE","USER")
						.requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/ventas/**").hasAnyRole("ADMIN", "VENDOR")
						.anyRequest().authenticated())
				.exceptionHandling(
						ex-> ex.authenticationEntryPoint(entryPoint)
						.accessDeniedHandler(accessDenied));

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
