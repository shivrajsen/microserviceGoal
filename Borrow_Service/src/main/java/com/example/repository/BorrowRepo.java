package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Borrow;

@Repository
public interface BorrowRepo extends JpaRepository<Borrow, String> {

	Optional<Borrow> findByUserIdAndBookIdAndReturnedFalse(String userId, String bookId);

	List<Borrow> findByUserId(String userId);

	List<Borrow> findByUserIdAndReturnedFalse(String userId);
	
}
