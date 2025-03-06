package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;



@Repository
public interface UserRepo extends JpaRepository<User, String>{

	 Optional<User> findByEmail(String email);

	 Optional<User> findByName(String name);
}
