package com.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.modal.Product;
import com.security.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository prodRepo;
	
	public Product addProduct(Product product) {
		if(product.getName()!=null && !product.getName().isEmpty()) {
			Product newProduct = prodRepo.save(product);
			return newProduct;
		}
		else {
			  throw new RuntimeException("Invalid Name Of Product");
		}
	}

	public Optional<Product> findProductById(int id) {
		
		return prodRepo.findById(id);
	}
	
	public Optional<Product> findProductByName(String name){
		return prodRepo.findByName(name);
	}

	public List<Product> findAllProducts() {
		return prodRepo.findAll();
	}
}
