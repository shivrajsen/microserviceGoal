package com.example.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Borrow;

@Repository
public interface BorrowRepo extends JpaRepository<Borrow, String> {

	 Optional<Borrow> findByBookIdAndReturnedFalse(String bookId);  

	    Optional<Borrow> findByUserIdAndReturnedFalse(String userId);
}
