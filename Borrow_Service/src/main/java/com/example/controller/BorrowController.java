package com.example.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BorrowDto;
import com.example.model.Borrow;
import com.example.service.BorrowService;
import com.example.service.BorrowedServiceImpl;
import com.example.userjwt.UserClient;
import com.example.userjwt.UserDto;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

	 @Autowired
	 private BorrowedServiceImpl borrowService;
	 

	    @Autowired
	    private UserClient userClient;

	    // ✅ Borrow a new book (with authentication)
	    @PostMapping
	    public ResponseEntity<?> borrowBook(@RequestHeader("Authorization") String token, 
	                                        @RequestBody BorrowDto borrowDto) {
	        System.out.println("🔹 Received Token: " + token); 

	        if (token == null) {
	            return ResponseEntity.status(401).body("Unauthorized: No token provided.");
	        }

	        String userId = borrowDto.getId();
	        if (userId == null || userId.isEmpty()) {
	            return ResponseEntity.status(400).body("Bad Request: User ID is missing.");
	        }
     
	        System.out.println(borrowDto.getToken()+borrowDto.getBookId()+borrowDto.getId());
	        UserDto userdto = userClient.fetchUserById(userId, borrowDto.getToken());
	        System.out.println("Fetched User from UserService: " + userdto);
	        if (userdto == null) {
	            return ResponseEntity.status(401).body("Unauthorized: User Service returned NULL.");
	        }

	        System.out.println("✅ User found: " + userdto.getName());

	        if (!userdto.getToken().equals(token.replace("Bearer ", ""))) {
	            return ResponseEntity.status(401).body("Unauthorized: Invalid token.");
	        }

	        if (!"STUDENT".equalsIgnoreCase(userdto.getRole())) {
	            return ResponseEntity.status(403).body("Forbidden: Only students can borrow books.");
	        }

	        // ✅ Instead of calling `fetchUserById()` again, pass `userdto` directly
	        Borrow borrowedBook = borrowService.borrowNewBook(borrowDto);
	        return ResponseEntity.ok(borrowedBook);
	    }
}
