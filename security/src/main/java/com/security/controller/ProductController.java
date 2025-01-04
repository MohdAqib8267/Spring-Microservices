package com.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.exception.ProductNotFoundException;
import com.security.modal.Product;
import com.security.service.ProductService;


@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/add")
	public ResponseEntity<Product> addProduct(@RequestBody Product product){
		Product createdProduct = productService.addProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
	}
	
	 @GetMapping("/product/{id}")
	 public ResponseEntity<?> getProductById(@PathVariable int id) {
	     Product product = productService.findProductById(id)
	                .orElseThrow(() -> new ProductNotFoundException("Product not found with this id: " + id));
	     return ResponseEntity.status(HttpStatus.FOUND).body(product);
	 }
	
	 @GetMapping("/getProductByName/{name}")
	 public ResponseEntity<?> getProductByName(@PathVariable String name){
		 Product product = productService.findProductByName(name)
				 .orElseThrow(()-> new ProductNotFoundException("Product not found with this name: "+name));
		 return ResponseEntity.status(HttpStatus.FOUND).body(product);
	 }
	 
	 @GetMapping("/allProduct")
	 public ResponseEntity<?> getAllProduct(){
		 List<Product> products = productService.findAllProducts();
		 
		 if(products.isEmpty()) {
			 return ResponseEntity.noContent().build(); // Return 204 No Content
		 }
		 return ResponseEntity.ok(products);
	 }
}
