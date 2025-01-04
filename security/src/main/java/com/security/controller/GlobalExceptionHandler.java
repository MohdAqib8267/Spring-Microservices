package com.security.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.security.exception.ProductNotFoundException;
import com.security.modal.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException exception){
		ErrorResponse error = new ErrorResponse(LocalDateTime.now(),exception.getMessage(),"Product Not Found");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(ArrayIndexOutOfBoundsException.class)
	public ResponseEntity<?> handleArrayIndexOutOfBoundException(ArrayIndexOutOfBoundsException exception){
		 ErrorResponse productNotFound = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), "Product Not Found");
	     return new ResponseEntity<>(productNotFound, HttpStatus.NOT_FOUND);
	}
}
