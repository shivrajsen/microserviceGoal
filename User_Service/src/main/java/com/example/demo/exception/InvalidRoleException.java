package com.example.demo.exception;

public class InvalidRoleException extends RuntimeException  {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRoleException(String role) {
	        super("Invalid role: " + role);
	    }
	
}
