package com.example.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, UUID> {

	Book findByTitle(String title);
}
