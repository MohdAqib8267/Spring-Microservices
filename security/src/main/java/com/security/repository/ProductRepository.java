package com.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.modal.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{

	Optional<Product> findByName(String name);
	
}
