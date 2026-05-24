package com.example.demo.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.Venta;

public interface VentaRepository extends JpaRepository<Venta,Long> {
	
	@Query(value = "SELECT * FROM VENTAS v " +
            "WHERE TRUNC(v.FECHA_VENTA) BETWEEN :fechaInicio AND :fechaFin",
    countQuery = "SELECT count(*) FROM VENTAS v " +
                 "WHERE TRUNC(v.FECHA_VENTA) BETWEEN :fechaInicio AND :fechaFin",
    nativeQuery = true)	
	Page<Venta> findByFechaVentaBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
	
	
	@Query(value = "SELECT * FROM VENTAS v "
			+ "WHERE v.ID_USUARIO IN ( SELECT u.ID FROM USUARIOS u WHERE u.USERNAME = :username) "
			+ "AND TRUNC(v.FECHA_VENTA) BETWEEN :fechaInicio AND :fechaFin",
	countQuery  = "SELECT count(*) FROM VENTAS v "
			+ "WHERE v.ID_USUARIO IN ( SELECT u.ID FROM USUARIOS u WHERE u.USERNAME = :username) "
			+ "AND TRUNC(v.FECHA_VENTA) BETWEEN :fechaInicio AND :fechaFin",
			nativeQuery = true)
	Page<Venta> findByFechaVentaBetweenAndUsername(LocalDate fechaInicio, LocalDate fechaFin,  String username, Pageable pageable);
	
	
	Page<Venta> findByUsuarioUsername( String username, Pageable pageable);
	
}
