package com.project.loginproject.services.exceptions;


public class EmptyFieldsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmptyFieldsException(String obj) {
		super("The following field cannot be empty: " + obj);
		
		
	}
	
	
	
}
