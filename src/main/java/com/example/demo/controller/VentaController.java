package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.VentaDTO;
import com.example.demo.dto.VentaResponseDTO;
import com.example.demo.service.VentaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
	
	
	private final VentaService ventaService; 
	
	public VentaController(VentaService ventaService) {
		this.ventaService = ventaService;
	}
	
	
	
	@GetMapping
	public ResponseEntity<Page<VentaResponseDTO > > listarVentas(Pageable pageable ) {
		return ResponseEntity.ok(ventaService.listarVentas(pageable));
		
	} 
	
	@GetMapping("/fechas")
	public ResponseEntity<Page<VentaResponseDTO>> listarVentasPorFecha(
			@RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, 
			@RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,			
			Pageable pageable
			)
	{
		return ResponseEntity.ok(ventaService.listarVentasPorFechas(fechaInicio, fechaFin, pageable));
	}
	
	@PostMapping
	public ResponseEntity<VentaResponseDTO> registrarVenta(@RequestBody @Valid  VentaDTO venta ) {
		VentaResponseDTO vtCreada = ventaService.registrarVenta(venta);
		return ResponseEntity.status(HttpStatus.CREATED).body(vtCreada);
	}
	
}
