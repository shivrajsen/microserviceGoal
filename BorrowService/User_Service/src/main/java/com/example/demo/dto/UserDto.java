package com.example.demo.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

	private String id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String role;
	
	 public UserDto(String id, String name, String email, String role) {
		    this.id = id;
	        this.name = name;
	        this.email = email;
	        this.role = role;
	    }

}
