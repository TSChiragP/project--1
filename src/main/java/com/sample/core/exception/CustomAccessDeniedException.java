package com.sample.core.exception;

import java.nio.file.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {
	public CustomAccessDeniedException(String message) {
		super(message);
	}
}
