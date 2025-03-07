package com.example.service;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.BorrowDto;
import com.example.model.Borrow;
import com.example.repository.BorrowRepo;
import com.example.userjwt.UserClient;
import com.example.userjwt.UserDto;

@Service
public class BorrowedServiceImpl implements BorrowService{

	@Autowired
	private BorrowRepo borrowRepo;
	
	 @Autowired
	 private UserClient userClient;
	
	@Override
	public Borrow borrowNewBook(BorrowDto borrowDto) {
	    UserDto user = userClient.fetchUserById(borrowDto.getId(), borrowDto.getToken());

	    // ✅ First, check if user is null
	    if (user == null) {
	        throw new RuntimeException("User not found.");
	    }

	    // ✅ Now, safely check the token
	    if (!user.getToken().equals(borrowDto.getToken())) {
	        throw new RuntimeException("Unauthorized: Invalid token.");
	    }

	    // ✅ Ensure only students can borrow books
	    if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
	        throw new RuntimeException("Only students are allowed to borrow books.");
	    }

	    // ✅ Create and save Borrow record
	    Borrow borrow = new Borrow();
	    borrow.setUserId(borrowDto.getId());
	    borrow.setBookId(borrowDto.getBookId());
	    borrow.setReturn_date(borrowDto.getReturn_date());
	    borrow.setReturned(false);

	    return borrowRepo.save(borrow);
	}

	@Override
	public Boolean returnedBook(String bookId) {
		 Optional<Borrow> borrowOptional = borrowRepo.findByBookIdAndReturnedFalse(bookId);

	        if (!borrowOptional.isPresent()) {
	            throw new RuntimeException("No active borrow record found for this book.");
	        }

	        Borrow borrow = borrowOptional.get();
	        borrow.setReturned(true);
	        borrowRepo.save(borrow);
	        return true;
	}

	@Override
	public Borrow getBorrowedBookByUserId(String userId) {
		 return borrowRepo.findByUserIdAndReturnedFalse(userId)
	                .orElseThrow(() -> new RuntimeException("No active borrowed books found for this user."));
	}

	
}
