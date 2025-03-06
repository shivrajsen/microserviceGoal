package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BookDto;
import com.example.model.Book;
import com.example.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;
	
	  
	 @PostMapping
	    public ResponseEntity<?> addBook(@RequestBody BookDto bookDto, @RequestHeader("Authorization") String token) {
	        try {
	        	
	            Book addedBook = bookService.addBook(token.substring(7), bookDto); // Remove "Bearer "
	            return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid Token");
	        }
	    }

	
	
	 @DeleteMapping("/{id}")
	 public ResponseEntity<?> deleteBook(@PathVariable String id, @RequestHeader("Authorization") String token) {
	     try {
	         // Extract the token (removing "Bearer ")
	         String actualToken = token.substring(7);
	         Book deletedBook = bookService.deleteBook(id, actualToken);
	         
	         if (deletedBook == null) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 - Not Found
	         }
	         
	         return ResponseEntity.status(HttpStatus.OK).body(deletedBook); // 200 - OK
	     } catch (RuntimeException e) {
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid Token");
	     }
	 }

    
	 @GetMapping("/search")
	 public ResponseEntity<?> searchBook(@RequestParam String bookName, @RequestHeader("Authorization") String token) {
	     try {
	         // Extract the token (removing "Bearer ")
	         String actualToken = token.substring(7);
	         if (bookName == null || bookName.trim().isEmpty()) {
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 - Bad Request (invalid input)
	         }

	         Book foundBook = bookService.searchBook(bookName, actualToken);

	         if (foundBook == null) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 - Not Found (book not found)
	         }

	         return ResponseEntity.status(HttpStatus.OK).body(foundBook); // 200 - OK
	     } catch (RuntimeException e) {
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid Token");
	     }
	 }
}
