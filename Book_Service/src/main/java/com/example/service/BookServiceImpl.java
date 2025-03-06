package com.example.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.BookDto;
import com.example.model.Book;
import com.example.repository.BookRepo;
import com.example.util.JwtUtil;


@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepo bookRepo;
	
//	@Autowired
//	private UserService userService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	public Book BookdtoToBook(BookDto dto) {
		
		Book book = new Book();
		
		book.setTitle(dto.getTitle());
		book.setIsbn(dto.getIsbn());
		book.setAvailable_copies(dto.getAvailable_copies());
		book.setCategory(dto.getCategory());
		
		return book;
	}
	
	 public Book addBook(String token, BookDto bookDto) {
		
	        if (!jwtUtil.validateToken(token)) {
	            throw new RuntimeException("Unauthorized: Invalid Token");
	        }

	        Book book = new Book();
	        book.setTitle(bookDto.getTitle());
	        book.setAvailable_copies(bookDto.getAvailable_copies());
	        book.setCategory(bookDto.getCategory());
	        book.setIsbn(bookDto.getIsbn());
	        return bookRepo.save(book);
	    }


	 public Book deleteBook(String id, String token) {
	       
	        if (!jwtUtil.validateToken(token)) {
	            throw new RuntimeException("Unauthorized: Invalid Token");
	        }

	        if (id == null || id.trim().isEmpty()) {
	            return null;
	        }

	        try {
	            UUID uId = UUID.fromString(id);  // Convert the string to UUID
	            Optional<Book> bookOptional = bookRepo.findById(uId);  // Assuming your repo uses UUID as ID

	            if (!bookOptional.isPresent()) {
	                return null;  // Book not found
	            }

	            Book bookToDelete = bookOptional.get();
	            bookRepo.delete(bookToDelete);

	            return bookToDelete;  
	        } catch (IllegalArgumentException e) {
	            return null; 
	        }
	    }

	    // Modified searchBook method to use the token for validation
	    public Book searchBook(String bookName, String token) {
	        // Validate the token before proceeding
	        if (!jwtUtil.validateToken(token)) {
	            throw new RuntimeException("Unauthorized: Invalid Token");
	        }

	        if (bookName == null || bookName.trim().isEmpty()) {
	            return null;
	        }
	        // Search for a book by its name using the repository
	        return bookRepo.findByTitle(bookName);  // Assuming findByTitle is a custom query method in your BookRepo
	    }

}
