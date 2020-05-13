package com.ubs.creditrisk.zeus.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ubs.creditrisk.zeus.model.ApiError;

@ControllerAdvice
public class GlobalExceptionHandler {

	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	public ResponseEntity<?> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler
	public ResponseEntity<?> userNotFoundException(UserNotFoundException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<?> handleAccessDeniedException(Exception ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	 @ExceptionHandler({ Exception.class })
	 public ResponseEntity<?> handleAll(Exception ex, WebRequest request) {
	 ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
	 ex.getLocalizedMessage(), "error occurred");
	 return new ResponseEntity<ApiError>(apiError, new HttpHeaders(),
	 apiError.getStatus());
	 }

}
