package com.example.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.BorrowDto;
import com.example.model.Borrow;
import com.example.repository.BorrowRepo;
import com.example.userjwt.UserClient;
import com.example.userjwt.UserDto;

@Service
public class BorrowedServiceImpl implements BorrowService {

	@Autowired
	private BorrowRepo borrowRepo;

	@Autowired
	private UserClient userClient;
	
	Logger logger = LoggerFactory.getLogger(BorrowedServiceImpl.class);

	@Override
	public Borrow borrowNewBook(BorrowDto borrowDto) {
		UserDto user = userClient.fetchUserById(borrowDto.getUserId(), borrowDto.getToken());

		if (user == null) {
			throw new RuntimeException("User not found.");
		}

		if (!user.getToken().equals(borrowDto.getToken())) {
			throw new RuntimeException("Unauthorized: Invalid token.");
		}

		if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
			throw new RuntimeException("Only students are allowed to borrow books.");
		}

		logger.info("userId" , borrowDto.getUserId());
		logger.info("bookId", borrowDto.getBookId());

		Borrow borrow = new Borrow();
		borrow.setUserId(borrowDto.getUserId());
		borrow.setBookId(borrowDto.getBookId());
		borrow.setReturn_date(borrowDto.getReturn_date());
		borrow.setReturned(false);

		return borrowRepo.save(borrow);
	}

	@Override
	public Boolean returnedBook(String userId, String bookId) {
		Optional<Borrow> borrowOptional = borrowRepo.findByUserIdAndBookIdAndReturnedFalse(userId, bookId);

		if (!borrowOptional.isPresent()) {
			logger.warn("No active borrow record found for userId: " + userId + " and bookId: " + bookId);
			return false;
		}

		Borrow borrow = borrowOptional.get();

		if (borrow.isReturned()) {
			return false;
		}

		borrow.setReturned(true);
		borrowRepo.save(borrow);
		return true;
	}

	@Override
	public List<Borrow> getBorrowedBooksByUserId(String userId) {
		List<Borrow> borrowedBooks = borrowRepo.findByUserId(userId);

		if (borrowedBooks.isEmpty()) {
			return Collections.emptyList();
		}

		return borrowedBooks;
	}

	public List<Borrow> getUnreturnedBooksByUserId(String userId) {
		return borrowRepo.findByUserIdAndReturnedFalse(userId);
	}
}
