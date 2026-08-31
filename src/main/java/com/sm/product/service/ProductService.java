package com.sm.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sm.product.dto.ProductRequest;
import com.sm.product.dto.ProductResponse;
import com.sm.product.entity.Product;
import com.sm.product.exception.ProductNotFoundException;
import com.sm.product.repository.ProductRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse createProduct(
			@Valid @NotNull ProductRequest request) {

		Product product = new Product(
				normalizeName(request.getName()),
				request.getPrice()
		);

		Product savedProduct = productRepository.save(product);

		return toResponse(savedProduct);
	}

	@Transactional(readOnly = true)
	public ProductResponse getProductById(
			@NotNull Long productId) {
		return productRepository.findById(productId)
				.map(this::toResponse)
				.orElseThrow(() -> new ProductNotFoundException(productId));
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getAllProducts() {
		return productRepository.findAll()
				.stream()
				.map(this::toResponse)
				.toList();
	}

	private ProductResponse toResponse(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getPrice()
		);
	}

	private String normalizeName(String name) {
		if (name == null) {
			return null;
		}

		return name.trim();
	}
}
