package com.example.userjwt;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserClient {

	private final WebClient webClient;
	    
	    private static final String USER_SERVICE_URL = "http://localhost:8080/auth/validate";
	    
	    public UserClient(WebClient.Builder webClientBuilder) {
	        this.webClient = webClientBuilder.build();
	    }
	    
	    public UserDto fetchUserById(String userId, String token) {
	        if (token == null) {
	            System.out.println("🔴 Error: Token is NULL before sending request!");
	            return null;
	        }
	        String finalToken = token.startsWith("Bearer ") ? token : "Bearer " + token;
	        System.out.println("🔹 Sending Token to Validation API: " + finalToken);

	        try {
	            UserDto user = webClient.get()
	                    .uri(USER_SERVICE_URL + "/{id}", userId)
	                    .header("Authorization", finalToken)
	                    .retrieve()
	                    .bodyToMono(UserDto.class)
	                    .block();

	            if (user == null) {
	                System.out.println("❌ Error: Received NULL response from User Service!");
	            } else {
	                System.out.println("✅ Success: Received User Data - " + user);
	            }

	            return user;
	        } catch (Exception e) {
	            System.out.println("❌ Exception in fetchUserById: " + e.getMessage());
	            e.printStackTrace();
	            return null;
	        }
	    }





}
