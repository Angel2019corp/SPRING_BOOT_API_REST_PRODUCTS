package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProductoRepository;

import jakarta.persistence.EntityNotFoundException;

import com.example.demo.domain.*;
import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponseDTO;

@Service
public class ProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired 
	private CategoriaRepository categoriaRepository; 

    public Page<ProductoResponseDTO> listarProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
        		.map(this::convertirDTO);
    }
    
    public Page<ProductoResponseDTO> getProductosByCategory(Long categoriaId,  Pageable pageable) {
    	 return productoRepository.findByCategoriaId( categoriaId, pageable )
    			 .map(this::convertirDTO);
    	
    } 

    public ProductoResponseDTO obtenerPorId(Long id) {
        return productoRepository.findById(id)
        		.map(this::convertirDTO)
        		.orElseThrow(
        		() -> new EntityNotFoundException( "El producto con id: " + id + " no existe " )
        		);
    }

    public Producto guardar(ProductoRequest req) {
    	Producto productoNew = new Producto();
    	productoNew.setNombre(req.getNombre());
    	productoNew.setDescripcion(req.getDescripcion());
    	productoNew.setPrecio(req.getPrecio());
    	productoNew.setStock(req.getStock());
    	
    	if (req.getCategoriaId() != null) {
    		Categoria cat = categoriaRepository.findById(req.getCategoriaId())
    				.orElseThrow(  () -> new RuntimeException("La categoria con el id: " + req.getCategoriaId()  + " no existe") );
    		productoNew.setCategoria(cat);	
    	}
    	
        return productoRepository.save(productoNew);
    }

    public void eliminar(Long id) {
    	productoRepository.deleteById(id);
    }
    
    
    private ProductoResponseDTO convertirDTO(Producto producto ) {
    	String categoria = producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin categoria";    	
    	return new ProductoResponseDTO(
    				producto.getId(),
    				producto.getNombre(),
    				producto.getDescripcion(),
    				producto.getPrecio(),
    				producto.getStock(),
    				categoria
    			);
    }
}