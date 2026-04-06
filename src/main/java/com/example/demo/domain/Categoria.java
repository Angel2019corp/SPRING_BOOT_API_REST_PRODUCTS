package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorias_seq")
	@SequenceGenerator(name = "cateorgias_seq", sequenceName = "SEQ_CATEGORIAS", allocationSize = 1)
	private Long id;
	
	@Column(name = "NOMBRE", nullable = false, length = 100)
	private String nombre; 
	
	@Column(name = "DESCRIPCION",  length = 300)
	private String descripcion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
