package com.project.loginproject.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.project.loginproject.services.exceptions.AuthenticationCredentialsException;
import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class AuthenticationCredentialsExceptionHandler {

	@ExceptionHandler (AuthenticationCredentialsException.class)
	public ResponseEntity<StandardError> resourceNotFound(AuthenticationCredentialsException e, HttpServletRequest request){
		
		String error = "JWT is incorrect or expired";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}
	
	
}