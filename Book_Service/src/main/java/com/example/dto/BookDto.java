package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	
    private String title;
	
	private String category;
	
	private String isbn;
	
	private int available_copies;
}
