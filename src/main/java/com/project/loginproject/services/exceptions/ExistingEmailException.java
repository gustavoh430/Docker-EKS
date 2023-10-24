package com.project.loginproject.services.exceptions;


public class ExistingEmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistingEmailException() {
		super("This email is already in use");
		
		
	}
	
	
	
}
