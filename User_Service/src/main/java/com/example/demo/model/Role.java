package com.example.demo.model;

public enum Role{

	STUDENT, TEACHER, ADMIN;
	
	  public static boolean isValidRole(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}
