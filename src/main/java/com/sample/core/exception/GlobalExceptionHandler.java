package com.sample.core.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(exception = BookNotFoundException.class)
	public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@ExceptionHandler(exception = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> hadleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		Map<String, String> errorMap = new HashMap<String, String>();

		exception.getBindingResult().getFieldErrors().forEach(e -> errorMap.put(e.getField(), e.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
	}
}
