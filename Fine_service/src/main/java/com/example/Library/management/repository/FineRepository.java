package com.example.Library.management.repository;

import com.example.Library.management.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FineRepository extends JpaRepository<Fine, UUID> {
    List<Fine> findByUserIdAndPaidFalse(UUID userId);
}
