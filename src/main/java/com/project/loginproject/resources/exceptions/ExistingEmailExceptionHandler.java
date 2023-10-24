package com.project.loginproject.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.project.loginproject.services.exceptions.ExistingEmailException;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ExistingEmailExceptionHandler {

	@ExceptionHandler (ExistingEmailException.class)
	public ResponseEntity<StandardError> resourceNotFound(ExistingEmailException e, HttpServletRequest request){
		
		String error = "Email in use";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}
	
	
}