package com.example.custom.exception;

public class UnauthorizedException  extends RuntimeException {
	 public UnauthorizedException(String message) {
	        super(message);
	    }
}
