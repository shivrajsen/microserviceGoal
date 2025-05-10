package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.custom.exception.BadRequestException;
import com.example.custom.exception.ForbiddenException;
import com.example.custom.exception.ResourceNotFoundException;
import com.example.custom.exception.UnauthorizedException;
import com.example.dto.BorrowDto;
import com.example.model.Borrow;
import com.example.service.BorrowedServiceImpl;
import com.example.userjwt.UserClient;
import com.example.userjwt.UserDto;

@RestController
public class BorrowController {

	@Autowired
	private BorrowedServiceImpl borrowService;

	@Autowired
	private UserClient userClient;

	Logger logger = LoggerFactory.getLogger(BorrowController.class);

	@PostMapping("/borrow")
	public ResponseEntity<?> borrowBook(@RequestHeader("Authorization") String token,
			@RequestBody BorrowDto borrowDto) {

		borrowDto.setToken(token.replace("Bearer ", ""));

		if (token == null || token.isEmpty()) {
			throw new UnauthorizedException("No token provided.");
		}

		String userId = borrowDto.getUserId();

		logger.info("userId", userId);
		if (userId == null || userId.isEmpty()) {
			throw new BadRequestException("User ID is missing.");
		}

		UserDto userdto = userClient.fetchUserById(userId, token);

		logger.info("Fetched User from UserService: ", userdto);

		if (userdto == null) {
			throw new UnauthorizedException("User Service returned NULL.");
		}

		logger.info("User found: " + userdto.getName());

		if (!userdto.getToken().equals(token.replace("Bearer ", ""))) {
			throw new UnauthorizedException("Invalid token.");
		}

		if (!"STUDENT".equalsIgnoreCase(userdto.getRole())) {
			throw new ForbiddenException("Only students can borrow books.");
		}

		Borrow borrowedBook = borrowService.borrowNewBook(borrowDto);
		return ResponseEntity.ok(borrowedBook);
	}

	@PutMapping("/return/{userId}")
	public ResponseEntity<?> returnBook(@RequestHeader("Authorization") String token, @PathVariable String userId,
			@PathVariable String bookId) {
		if (token == null || token.isEmpty()) {
			throw new UnauthorizedException("No token provided.");
		}

		boolean isValidToken = userClient.validateToken(token);
		if (!isValidToken) {
			throw new UnauthorizedException("Invalid Token.");
		}

		bookId = bookId.replace("\"", "").trim();

		if (bookId == null || bookId.isEmpty()) {
			throw new BadRequestException("Book ID is missing.");
		}

		boolean isReturned = borrowService.returnedBook(userId, bookId);
		if (!isReturned) {
			throw new BadRequestException("This book is already returned.");
		}

		return ResponseEntity.ok("Book returned successfully.");
	}

	@GetMapping("/borrowed/{userId}")
	public ResponseEntity<?> getBorrowedBook(@RequestHeader("Authorization") String token,
			@PathVariable String userId) {
		if (token == null || token.isEmpty()) {
			throw new UnauthorizedException("No token provided.");
		}

		boolean isValidToken = userClient.validateToken(token);
		if (!isValidToken) {
			throw new UnauthorizedException("Invalid Token.");
		}

		List<Borrow> borrowedBooks = borrowService.getBorrowedBooksByUserId(userId);
		if (borrowedBooks.isEmpty()) {
			throw new ResourceNotFoundException("No active borrowed books found for this user.");
		}

		return ResponseEntity.ok(borrowedBooks);
	}

	@GetMapping("/unreturned/{userId}")
	public ResponseEntity<?> getUnreturnedBooks(@RequestHeader("Authorization") String token,
			@PathVariable String userId) {
		if (token == null || token.isEmpty()) {
			throw new UnauthorizedException("No token provided.");
		}

		// Validate token using UserClient
		boolean isValidToken = userClient.validateToken(token);
		if (!isValidToken) {
			throw new UnauthorizedException("Invalid Token.");
		}

		List<Borrow> unreturnedBooks = borrowService.getUnreturnedBooksByUserId(userId);

		if (unreturnedBooks.isEmpty()) {
			throw new ResourceNotFoundException("No pending books to be returned.");
		}

		return ResponseEntity.ok(unreturnedBooks);
	}

}
