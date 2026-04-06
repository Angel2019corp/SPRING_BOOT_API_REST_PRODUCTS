package com.example.demo.dto;

public class ProductoRequest {
	private String nombre; 
	private String descripcion; 
	private Double precio; 
	private Integer stock; 
	private Long categoriaId; 
	
	public ProductoRequest(
			 String nombre, 
			 String descripcion, 
			 Double precio, 
			 Integer stock, 
			 Long categoriaId 
			) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio; 
		this.stock = stock; 
		this.categoriaId = categoriaId;
	}
	
	public  String getNombre() {return nombre;} 
	public  String getDescripcion() {return descripcion;} 
	public  Double getPrecio() {return precio;} 
	public  Integer getStock() {return stock;} 
	public  Long getCategoriaId() {return categoriaId;} 

}
