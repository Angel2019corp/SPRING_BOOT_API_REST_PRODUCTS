package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Venta;
import com.example.demo.dto.VentaResponseDTO;
import com.example.demo.repository.VentaRepository;

@Service
public class VentaService {
	
	
	private VentaRepository ventaRepository;
	
	public VentaService(VentaRepository ventaRepository) {
		this.ventaRepository = ventaRepository;
	}
	
	public  Page<VentaResponseDTO> listarVentas ( Pageable pageable ) {
		return ventaRepository.findAll(pageable)
				.map(this::convertirDTO);
	}
	
	public Page<VentaResponseDTO> listarVentasPorFechas(LocalDate fechaInicio,LocalDate fechaFin,Pageable pageable) {
		return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin, pageable)
				.map(this::convertirDTO);
	}
	
	private VentaResponseDTO convertirDTO(Venta venta) {
		return new VentaResponseDTO(
				venta.getIdPedido(),
				venta.getProducto().getDescripcion(),
				venta.getCantidad(),
				venta.getPrecioUnitario(),
				venta.getTotal(),
				venta.getFechaVenta()
				);
	} 

}
