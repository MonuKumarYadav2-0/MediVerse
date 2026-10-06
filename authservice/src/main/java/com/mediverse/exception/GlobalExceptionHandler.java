package com.mediverse.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse(LocalDateTime.now(),
				HttpStatus.CONFLICT.value(),
				"USER_ALREADY_EXISTS",
				ex.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse(LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"USER_NOT_FOUND",
				ex.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentials(
	        BadCredentialsException ex,
	        HttpServletRequest request
	) {

	    ErrorResponse error = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.UNAUTHORIZED.value(),
	            "INVALID_CREDENTIALS",
	            "Invalid phone number or password",
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(
	        MethodArgumentNotValidException ex,
	        HttpServletRequest request
	) {

	    String message = ex.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .map(error -> error.getField() + ": " + error.getDefaultMessage())
	            .findFirst()
	            .orElse("Invalid request");

	    ErrorResponse error = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "VALIDATION_ERROR",
	            message,
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(error);
	}
}