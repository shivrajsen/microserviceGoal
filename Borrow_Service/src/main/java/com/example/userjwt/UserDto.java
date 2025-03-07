package com.example.userjwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserDto {

	  private String id;
	    private String name;
	    private String role;
	    private String token;
	    
	    public String getId() { return id; }
	    public void setId(String id) { this.id = id; }

	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }

	    public String getRole() { return role; }
	    public void setRole(String role) { this.role = role; }

	    public String getToken() { return token; }
	    public void setToken(String token) { this.token = token; }
	}

