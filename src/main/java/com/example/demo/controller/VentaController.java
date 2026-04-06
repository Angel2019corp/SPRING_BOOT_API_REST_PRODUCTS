package com.example.demo.controller;

import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.VentaResponseDTO;
import com.example.demo.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
	
	@Autowired
	private  VentaService service; 
	
	@GetMapping
	public ResponseEntity<Page<VentaResponseDTO > > listarVentas(Pageable pageable ) {
		return ResponseEntity.ok(service.listarVentas(pageable));
		
	} 
	
	@GetMapping("fechas")
	public ResponseEntity<Page<VentaResponseDTO>> listarVentasPorFecha(
			@RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, 
			@RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,			
			Pageable pageable
			)
	{
		System.out.println("fecha inicio: " + fechaInicio + " fecha fin: " + fechaFin);
		return ResponseEntity.ok(service.listarVentasPorFechas(fechaInicio, fechaFin, pageable));
	}
	
}
