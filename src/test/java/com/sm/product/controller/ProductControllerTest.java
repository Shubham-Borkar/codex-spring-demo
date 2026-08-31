package com.sm.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.sm.product.dto.ProductResponse;
import com.sm.product.exception.GlobalExceptionHandler;
import com.sm.product.exception.ProductNotFoundException;
import com.sm.product.service.ProductService;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Test
	void shouldCreateProduct() throws Exception {

		ProductResponse response = new ProductResponse(
				1L,
				"Demo Product",
				new BigDecimal("35.00")
		);

		when(productService.createProduct(any())).thenReturn(response);

		mockMvc.perform(
				post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
									"name": "Demo Product",
									"price": 35.00
								}
								""")
		)
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/products/1"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Demo Product"))
				.andExpect(jsonPath("$.price").value(35.00));
	}

	@Test
	void shouldRejectInvalidProduct() throws Exception {
		mockMvc.perform(
				post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
									"name": " ",
									"price": 0
								}
								""")
		)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value("Validation failed"))
				.andExpect(jsonPath("$.errors.name").isArray())
				.andExpect(jsonPath("$.errors.price").isArray());
	}

	@Test
	void shouldReturnProductById() throws Exception {

		when(productService.getProductById(5L)).thenReturn(
				new ProductResponse(5L, "Notebook", new BigDecimal("12.75"))
		);

		mockMvc.perform(get("/products/5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(5))
				.andExpect(jsonPath("$.name").value("Notebook"))
				.andExpect(jsonPath("$.price").value(12.75));
	}

	@Test
	void shouldReturnNotFoundWhenProductIsMissing() throws Exception {

		when(productService.getProductById(99L))
				.thenThrow(new ProductNotFoundException(99L));

		mockMvc.perform(get("/products/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Product not found for id 99"))
				.andExpect(jsonPath("$.errors").isMap());
	}

	@Test
	void shouldReturnAllProducts() throws Exception {

		when(productService.getAllProducts()).thenReturn(
				List.of(
						new ProductResponse(1L, "Pen", new BigDecimal("5.00")),
						new ProductResponse(2L, "Book", new BigDecimal("15.00"))
				)
		);

		mockMvc.perform(get("/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Pen"))
				.andExpect(jsonPath("$[1].name").value("Book"));
	}
}
