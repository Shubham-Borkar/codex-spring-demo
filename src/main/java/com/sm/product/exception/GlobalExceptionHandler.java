package com.sm.product.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleProductNotFound(
			ProductNotFoundException exception) {

		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ApiErrorResponse(
						Instant.now(),
						HttpStatus.NOT_FOUND.value(),
						exception.getMessage(),
						Map.of()
				));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidation(
			MethodArgumentNotValidException exception) {

		Map<String, List<String>> errors = new LinkedHashMap<>();

		for (FieldError fieldError : exception.getBindingResult()
				.getFieldErrors()) {
			errors.computeIfAbsent(
					fieldError.getField(),
					key -> new ArrayList<>()
			).add(fieldError.getDefaultMessage());
		}

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorResponse(
						Instant.now(),
						HttpStatus.BAD_REQUEST.value(),
						"Validation failed",
						errors
				));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
			ConstraintViolationException exception) {

		Map<String, List<String>> errors = new LinkedHashMap<>();

		exception.getConstraintViolations().forEach(violation -> {
			String path = violation.getPropertyPath().toString();
			errors.computeIfAbsent(
					path,
					key -> new ArrayList<>()
			).add(violation.getMessage());
		});

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorResponse(
						Instant.now(),
						HttpStatus.BAD_REQUEST.value(),
						"Validation failed",
						errors
				));
	}
}
