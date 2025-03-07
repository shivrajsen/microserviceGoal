package com.example.userjwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserClient {

	private final WebClient webClient;
	
    private static final Logger logger = LoggerFactory.getLogger(UserClient.class);


	private static final String USER_SERVICE_URL = "http://localhost:8080/auth";
	private static final String USER_SERVICE_VALIDATE_URL = "http://localhost:8080/auth/validation";

	public UserClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	public UserDto fetchUserById(String userId, String token) {
		if (token == null || token.isEmpty()) {
            logger.error("Error: Token is NULL or empty before sending request!");
            throw new IllegalArgumentException("Authorization token cannot be null or empty.");
        
		}
		String finalToken = token.startsWith("Bearer ") ? token : "Bearer " + token;

		String requestUrl = "/validate/" + userId;
		logger.info("Sending Request to: {}", USER_SERVICE_URL + requestUrl);
        logger.info("Authorization Header: {}", finalToken);
		try {
			UserDto user = webClient.get().uri(USER_SERVICE_URL + "/validate/{id}", userId) // ✅ Ensure correct path
					.header("Authorization", finalToken).retrieve().bodyToMono(UserDto.class).block();

			if (user == null) {
				logger.error("Error: Received NULL response from User Service!");
			} else {
				logger.info("Success: Received User Data - " + user);
			}
			return user;
		} catch (Exception e) {
			logger.error("Exception in fetchUserById: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public boolean validateToken(String token) {
		try {
			String finalToken = token.startsWith("Bearer ") ? token : "Bearer " + token;

			logger.info("Sending Token for Validation: {}", finalToken);

			Boolean isValid = webClient.get().uri(USER_SERVICE_VALIDATE_URL).header("Authorization", finalToken)
					.retrieve().bodyToMono(Boolean.class).block();
			return Boolean.TRUE.equals(isValid);
		} catch (Exception e) {
			logger.error("Exception in validateToken: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

}
