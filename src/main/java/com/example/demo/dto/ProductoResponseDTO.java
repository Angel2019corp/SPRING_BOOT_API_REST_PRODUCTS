package com.example.demo.dto;

public class ProductoResponseDTO {
	private Long id; 
	
	private String nombre;
	
	private String descripcion;
	
	private Double precio;
	
	private Integer stock;
	
	private String categoria;
	
	public ProductoResponseDTO (
			Long id,
			String nombre,
			String descripcion,
			Double precio,
			Integer stock,
			String categoria			
			) {
		this.id = id; 
		this.nombre = nombre; 
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock; 
		this.categoria = categoria; 
	}
	public Long getId() {return id;}
	public String getNombre() {return nombre; }
	public String getDescripcion() {return descripcion;}
	public Double getPrecio() {return precio;}
	public Integer getStock() {return stock;}
	public String getCategoria() {return categoria;}
	
	

}
