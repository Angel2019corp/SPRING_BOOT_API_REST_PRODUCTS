package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.ProductoService;
import com.example.demo.domain.Producto;
import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponseDTO;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
	
	@Autowired
	private ProductoService service;

    @GetMapping
    public ResponseEntity< Page<ProductoResponseDTO>  > listarProductos(Pageable pageable){ 
    	return ResponseEntity.ok(service.listarProductos(pageable));
    }
    
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity< Page<ProductoResponseDTO> > getProductsByCategory( 
    		@PathVariable Long categoriaId,
    		Pageable pageable ) {
    	return ResponseEntity.ok(service.getProductosByCategory(categoriaId, pageable));
    }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody ProductoRequest producto) {
    	
    	Producto productoNew = service.guardar(producto);
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
