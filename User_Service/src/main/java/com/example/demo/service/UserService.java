package com.example.demo.service;


import java.util.List;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;

public interface UserService {

	 User createUser(UserDto dto);
	
	User loginUser(String email, long password);
	
	User GetUserById(String id)  throws Exception;
	
	List<UserDto> getAllUsers();
	
}
