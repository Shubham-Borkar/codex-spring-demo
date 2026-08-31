package com.sm.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sm.product.dto.ProductRequest;
import com.sm.product.dto.ProductResponse;
import com.sm.product.entity.Product;
import com.sm.product.exception.ProductNotFoundException;
import com.sm.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void shouldCreateProduct() {

		ProductRequest request = new ProductRequest();
		request.setName("  Demo Product  ");
		request.setPrice(new BigDecimal("25.50"));

		Product savedProduct = new Product("Demo Product", new BigDecimal("25.50"));
		savedProduct.setId(1L);

		when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class)))
				.thenReturn(savedProduct);

		ProductResponse response = productService.createProduct(request);

		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		verify(productRepository).save(productCaptor.capture());

		assertEquals("Demo Product", productCaptor.getValue().getName());
		assertEquals(new BigDecimal("25.50"), response.getPrice());
		assertEquals(1L, response.getId());
	}

	@Test
	void shouldReturnProductWhenItExists() {

		Product product = new Product("Demo Product", new BigDecimal("15.00"));
		product.setId(7L);

		when(productRepository.findById(7L)).thenReturn(Optional.of(product));

		ProductResponse response = productService.getProductById(7L);

		assertEquals(7L, response.getId());
		assertEquals("Demo Product", response.getName());
	}

	@Test
	void shouldThrowWhenProductDoesNotExist() {

		when(productRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(
				ProductNotFoundException.class,
				() -> productService.getProductById(99L)
		);
	}

	@Test
	void shouldReturnAllProducts() {

		Product first = new Product("First", new BigDecimal("10.00"));
		first.setId(1L);

		Product second = new Product("Second", new BigDecimal("20.00"));
		second.setId(2L);

		when(productRepository.findAll()).thenReturn(List.of(first, second));

		List<ProductResponse> response = productService.getAllProducts();

		assertEquals(2, response.size());
		assertEquals("First", response.get(0).getName());
		assertEquals("Second", response.get(1).getName());
	}
}
