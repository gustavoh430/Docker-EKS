package com.project.loginproject.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.project.loginproject.services.exceptions.EmptyFieldsException;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class EmptyFieldsExeceptionHandler {

	@ExceptionHandler (EmptyFieldsException.class)
	public ResponseEntity<StandardError> resourceNotFound(EmptyFieldsException e, HttpServletRequest request){
		
		String error = "Empty Fields";
		HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}
	
	
}