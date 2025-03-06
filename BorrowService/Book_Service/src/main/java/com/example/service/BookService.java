package com.example.service;

import com.example.dto.BookDto;
import com.example.model.Book;

public interface BookService {

	Book addBook(String token, BookDto bookDto);
	
	Book searchBook(String bookName, String token);
	
	Book deleteBook(String id, String token);
}
