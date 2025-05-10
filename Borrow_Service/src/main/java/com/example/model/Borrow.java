package com.example.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Borrow {

	@Id
	@UuidGenerator
	private String id;

	@UuidGenerator
	private String userId;

	@UuidGenerator
	private String bookId;

	@CreationTimestamp
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date borrow_date;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date return_date;

	private boolean returned;
}
