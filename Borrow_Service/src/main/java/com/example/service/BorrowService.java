package com.example.service;

import java.util.List;

import com.example.dto.BorrowDto;
import com.example.model.Borrow;

public interface BorrowService {

	Borrow borrowNewBook(BorrowDto borrowDto);

	Boolean returnedBook(String userId, String bookId);

	List<Borrow> getBorrowedBooksByUserId(String userId);
}
