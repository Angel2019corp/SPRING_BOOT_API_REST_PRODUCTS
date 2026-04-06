package com.example.demo.domain;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "VENTAS")
public class Venta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "ID_PEDIDO")
	private Long idPedido;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCTO_ID", nullable = false)
	private Producto producto; 
	
	@Column(nullable = false)
	private Integer cantidad;
	
	@Column(name = "PRECIO_UNITARIO", nullable = false)
	private Double precioUnitario;
	
	@Column(insertable = false, updatable = false)
	private Double total;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_VENTA", nullable = false, insertable = false, updatable = false)
	private Date fechaVenta;
	
	
	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	
	
	
}
