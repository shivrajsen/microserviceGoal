package com.example.util;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class JwtUtil {

	 private final WebClient webClient;

	    private static final String USER_SERVICE_VALIDATE_URL = "http://localhost:8080/auth/validation";

	    public JwtUtil(WebClient.Builder webClientBuilder) {
	        this.webClient = webClientBuilder.build();
	    }

	    public boolean validateToken(String token) {
	        try {
	            Boolean isValid = webClient.get()
	                    .uri(USER_SERVICE_VALIDATE_URL)
	                    .header("Authorization", "Bearer " + token)
	                    .retrieve()
	                    .bodyToMono(Boolean.class)
	                    .block();

	            return Boolean.TRUE.equals(isValid);
	        } catch (Exception e) {
	            return false;
	        }
	    }
	
}
