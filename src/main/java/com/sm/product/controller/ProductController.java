package com.sm.product.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sm.product.dto.ProductRequest;
import com.sm.product.dto.ProductResponse;
import com.sm.product.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(
			@Valid @RequestBody ProductRequest request) {

		ProductResponse response = productService.createProduct(request);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.getId())
				.toUri();

		return ResponseEntity.created(location).body(response);
	}

	@GetMapping("/{productId}")
	public ProductResponse getProduct(
			@PathVariable Long productId) {
		return productService.getProductById(productId);
	}

	@GetMapping
	public List<ProductResponse> getProducts() {
		return productService.getAllProducts();
	}
}
