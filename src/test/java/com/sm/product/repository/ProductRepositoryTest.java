package com.sm.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.sm.product.entity.Product;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@ActiveProfiles("demo")
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	void shouldPersistProduct() {

		Product product = new Product("Notebook", new BigDecimal("19.99"));

		Product savedProduct = productRepository.saveAndFlush(product);

		assertNotNull(savedProduct.getId());
		assertEquals("Notebook", savedProduct.getName());
	}

	@Test
	void shouldRejectBlankName() {
		Product product = new Product(" ", new BigDecimal("19.99"));

		assertThrows(
				ConstraintViolationException.class,
				() -> productRepository.saveAndFlush(product)
		);
	}

	@Test
	void shouldRejectNonPositivePrice() {
		Product product = new Product("Notebook", BigDecimal.ZERO);

		assertThrows(
				ConstraintViolationException.class,
				() -> productRepository.saveAndFlush(product)
		);
	}
}
