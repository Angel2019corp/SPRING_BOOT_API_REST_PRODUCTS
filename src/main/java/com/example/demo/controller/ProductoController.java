package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Producto;
import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponseDTO;
import com.example.demo.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
	
	
	private final ProductoService productoService;
	
	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

    @GetMapping
    public ResponseEntity< Page<ProductoResponseDTO>  > listarProductos(Pageable pageable){ 
    	return ResponseEntity.ok(productoService.listarProductos(pageable));
    }
    
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity< Page<ProductoResponseDTO> > getProductsByCategory( 
    		@PathVariable Long categoriaId,
    		Pageable pageable ) {
    	return ResponseEntity.ok(productoService.getProductosByCategory(categoriaId, pageable));
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO obtener(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody ProductoRequest producto) {
    	
    	Producto productoNew = productoService.guardar(producto);
    	return ResponseEntity.status(HttpStatus.CREATED).body(productoNew);
    }

   /* @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto p) {
        p.setId(id);
        return service.guardar(p);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }*/

}
