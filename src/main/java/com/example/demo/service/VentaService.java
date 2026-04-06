package com.example.demo.service;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.repository.VentaRepository;
import com.example.demo.domain.Venta;
import com.example.demo.dto.VentaResponseDTO;

@Service
public class VentaService {
	
	@Autowired
	private VentaRepository ventarepository;
	
	public  Page<VentaResponseDTO> listarVentas ( Pageable pageable ) {
		return ventarepository.findAll(pageable)
				.map(this::convertirDTO);
	}
	
	public Page<VentaResponseDTO> listarVentasPorFechas(LocalDate fechaInicio,LocalDate fechaFin,Pageable pageable) {
		return ventarepository.findByFechaVentaBetween(fechaInicio, fechaFin, pageable)
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
