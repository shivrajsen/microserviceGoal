package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.InvalidRoleException;
import com.example.demo.model.User;
import com.example.demo.request.AuthRequest;
import com.example.demo.response.LoginResponse;
import com.example.demo.response.MicroserviceResponce;
import com.example.demo.response.UserResponse;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserServiceImpl serviceImpl;

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @GetMapping("/test")
    public String test() {
        return "Testing message";
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto) {
        try {
            User user = serviceImpl.createUser(userDto);
            
            String userId = user.getId() != null ? user.getId().toString() : null;

            return new ResponseEntity<>(new UserResponse(userId, user.getName(), user.getEmail(), user.getRole(), user.getCreated_at()), HttpStatus.CREATED);
        
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Email is already in use. Please choose a different one.", HttpStatus.BAD_REQUEST);
        
        } catch (InvalidRoleException | IllegalArgumentException e) {  
        	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<Object> loginAndGetToken(@RequestBody AuthRequest req) {
        try {
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

             
            
            if (authentication.isAuthenticated()) {
               
            	
                System.out.println("User authenticated successfully: " + authentication.getName());
            	
                User user = serviceImpl.getUserByEmail(req.getEmail()).orElse(null);
                
                String token = jwtService.generateToken(user);

                
                LoginResponse response = new LoginResponse(req.getEmail(), token);

               
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing the login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/validation")
    public ResponseEntity<Boolean> validation(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.ok(false);
            }
            
            String jwt = token.substring(7); // Remove "Bearer "
            boolean isValid = jwtService.validateToken(jwt); // Call the existing validation method
            
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.ok(false); // Return false if token is invalid
        }
    }
    
    @GetMapping("/validate/{id}") // ✅ Add userId as a path variable
    public ResponseEntity<?> validateToken(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) { // ✅ Accept userId in URL
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String jwt = token.substring(7); // Remove "Bearer "

            // Validate token
            if (!jwtService.validateToken(jwt)) {
                System.out.println("Token Invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Extract user ID from token and compare
            String extractedUserId = jwtService.extractUserId(jwt);
           
            if (!id.equals(extractedUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 🚨 Forbidden if IDs do not match
            }

            // Fetch user details
            User user = serviceImpl.GetUserById(id);
            MicroserviceResponce res = new MicroserviceResponce();
            res.setId(user.getId());
            res.setName(user.getName());
            res.setRole(user.getRole());
            res.setToken(jwt);

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized if any issue occurs
        }
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable String id) {
        try {
            
            Optional<User> userOpt = Optional.ofNullable(serviceImpl.GetUserById(id));
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String userId = user.getId() != null ? user.getId().toString() : null;
               
                return new ResponseEntity<>(new UserResponse(userId, user.getName(), user.getEmail(), user.getRole(), user.getCreated_at()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = serviceImpl.getAllUsers();
        return ResponseEntity.ok(users);
    }
    }
