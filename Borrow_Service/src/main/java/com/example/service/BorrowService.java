package com.example.service;

import com.example.dto.BorrowDto;
import com.example.model.Borrow;

public interface BorrowService {

	Borrow borrowNewBook(BorrowDto borrowDto);
	
	Boolean returnedBook(String bookId);
	
	Borrow getBorrowedBookByUserId(String userId);
}
