package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;                                                                                                                                                
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.domain.Venta;
import com.example.demo.dto.VentaResponseDTO;
import com.example.demo.repository.VentaRepository;

@Service
public class VentaService {
	
	
	private final VentaRepository ventaRepository;
	
	private static final String ROLE_VENDOR = "ROLE_VENDOR";
	
	
	public VentaService(VentaRepository ventaRepository) {
		this.ventaRepository = ventaRepository;
	}
	
	public  Page<VentaResponseDTO> listarVentas ( Pageable pageable ) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Page<Venta> vts = isVendor(auth) 
				? ventaRepository.findByUsuarioUsername(auth.getName(), pageable)
						: ventaRepository.findAll(pageable);
		return vts.map(this::convertirDTO);
	}
	
	public Page<VentaResponseDTO> listarVentasPorFechas(LocalDate fechaInicio,LocalDate fechaFin,Pageable pageable) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Page<Venta> vts = isVendor(auth)
				? ventaRepository.findByFechaVentaBetweenAndUsername(fechaInicio, fechaFin, auth.getName(), pageable)
						:  ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin, pageable);
		return vts.map(this::convertirDTO);
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
	private boolean isVendor(Authentication auth) {
		if (auth == null || auth.getAuthorities() == null) {
			return false;
		}
		for(GrantedAuthority grandAuth : auth.getAuthorities()) {
			if(ROLE_VENDOR.equals(grandAuth.getAuthority())) {
				return true;
			}
		}
		return false;
	}

}
