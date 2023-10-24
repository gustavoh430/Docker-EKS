package com.project.loginproject.services.exceptions;


public class AuthenticationCredentialsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthenticationCredentialsException(Object obj) {
		super("Error: " + obj);
		
		
	}
	
	
	
}
