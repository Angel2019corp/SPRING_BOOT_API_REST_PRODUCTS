package com.example.demo.dto;

import java.util.Date;

public class VentaResponseDTO {
	private Long idPedido;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;
    private Date fechaVenta;

    // Constructor
    public VentaResponseDTO(Long idPedido, String nombreProducto, Integer cantidad,
                            Double precioUnitario, Double total, Date fechaVenta) {
        this.idPedido = idPedido;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = total;
        this.fechaVenta = fechaVenta;
    }

    // Getters
    public Long getIdPedido() { return idPedido; }
    public String getNombreProducto() { return nombreProducto; }
    public Integer getCantidad() { return cantidad; }
    public Double getPrecioUnitario() { return precioUnitario; }
    public Double getTotal() { return total; }
    public Date getFechaVenta() { return fechaVenta; }

}
