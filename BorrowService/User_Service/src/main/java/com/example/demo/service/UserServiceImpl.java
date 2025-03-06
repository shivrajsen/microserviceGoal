package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService, UserDetailsService  {

	@Autowired
	private UserRepo repo;
	
	@Autowired
    private PasswordEncoder encoder;
	
	private UserDto userToDto(User user) {
	    return new UserDto(user.getId().toString(), user.getName(), user.getEmail(), user.getRole());
	}

	
	
	public User dtoToUser(UserDto dto) {
		
		User user = new User();
		
		user.setName(dto.getName());
		user.setEmail(dto.getEmail().toLowerCase());
		user.setPassword(dto.getPassword());
		user.setRole(dto.getRole());
		
		return user;
	}
	
	@Override
	public User createUser(UserDto dto) {
		 if (dto == null) {
		        return null;
		    } else {
		      
		        if (!Role.isValidRole(dto.getRole())) {
		            throw new IllegalArgumentException("Invalid role: " + dto.getRole());
		        }

		        User user = dtoToUser(dto);

		        user.setPassword(encoder.encode(dto.getPassword()));
		        user.setRole(dto.getRole().toUpperCase());

		        return repo.save(user);
		    }

	}

	@Override
	public User loginUser(String email, long password) {
		
		return null;
	}

	@Override
	public User GetUserById(String id) throws Exception {
	    // Check if ID is valid
	    if (id == null || id.isEmpty()) {
	        throw new IllegalArgumentException("User ID cannot be null or empty.");
	    }

	    try {
	        // Use findById for better handling (Optional<User>)
	        Optional<User> userOptional = repo.findById(id);

	        // If the user is not found, throw a custom exception
	        if (!userOptional.isPresent()) {
	            throw new Exception("User not found with ID: " + id);
	        }

	        // Return the user
	        return userOptional.get();
	    } catch (IllegalArgumentException e) {
	        // Handle invalid ID format (e.g., non-numeric ID for a numeric field)
	        throw new Exception("User ID format is invalid: " + id, e);
	    } catch (Exception e) {
	        // General catch block for any other exceptions
	        throw new Exception("Error retrieving user with ID: " + id, e);
	    }
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    Optional<User> user = repo.findByEmail(email.toLowerCase());
	    System.out.println(user);
	    
	    if (user.isEmpty()) { 
	       
	        throw new UsernameNotFoundException("User not found");
	    }
	    
	    return new UserInfoDetails(user.get());
	}

	@Override
	public List<UserDto> getAllUsers() {
	    return repo.findAll()
	               .stream()
	               .map(this::userToDto) // Convert each User entity to UserDto
	               .collect(Collectors.toList());
	}




	public Optional<User> getUserByEmail(String email) {
		return   repo.findByEmail(email.toLowerCase());
	     
	}


	

}
