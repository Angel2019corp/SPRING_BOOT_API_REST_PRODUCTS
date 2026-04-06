package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.domain.*;
import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponseDTO;

@Service
public class ProductoService {
	
	@Autowired
	private ProductoRepository repo;
	
	@Autowired 
	private CategoriaRepository catRepo; 

    public Page<ProductoResponseDTO> listarProductos(Pageable pageable) {
        return repo.findAll(pageable)
        		.map(this::convertirDTO);
    }
    
    public Page<ProductoResponseDTO> getProductosByCategory(Long categoriaId,  Pageable pageable) {
    	 return repo.findByCategoriaId( categoriaId, pageable )
    			 .map(this::convertirDTO);
    	
    } 

    public Producto obtenerPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Producto guardar(ProductoRequest req) {
    	Producto productoNew = new Producto();
    	productoNew.setNombre(req.getNombre());
    	productoNew.setDescripcion(req.getDescripcion());
    	productoNew.setPrecio(req.getPrecio());
    	productoNew.setStock(req.getStock());
    	
    	if (req.getCategoriaId() != null) {
    		Categoria cat = catRepo.findById(req.getCategoriaId())
    				.orElseThrow(  () -> new RuntimeException("Categoria incorrecta") );
    		System.out.println("Asignando categoría: " + cat.getId());

    		productoNew.setCategoria(cat);
    				
    		
    	}
    	
        return repo.save(productoNew);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
    
    private ProductoResponseDTO convertirDTO(Producto producto ) {
    	return new ProductoResponseDTO(
    				producto.getId(),
    				producto.getNombre(),
    				producto.getDescripcion(),
    				producto.getPrecio(),
    				producto.getStock(),
    				producto.getCategoria().getNombre()
    			);
    }
}