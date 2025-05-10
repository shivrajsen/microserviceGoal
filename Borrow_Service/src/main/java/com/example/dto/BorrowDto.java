package com.example.dto;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDto {

	private String userId;

	private String bookId;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date return_date;

	private boolean returned;

	private String token;
}
