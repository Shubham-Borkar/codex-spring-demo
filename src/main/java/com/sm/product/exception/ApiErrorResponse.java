package com.sm.product.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ApiErrorResponse {

	private final Instant timestamp;
	private final int status;
	private final String message;
	private final Map<String, List<String>> errors;

	public ApiErrorResponse(
			Instant timestamp,
			int status,
			String message,
			Map<String, List<String>> errors) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public Map<String, List<String>> getErrors() {
		return errors;
	}
}
