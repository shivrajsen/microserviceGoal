package com.example.Library.management.controller;


import com.example.Library.management.entity.Fine;
import com.example.Library.management.service.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("fine")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Fine>> getPendingFines(@PathVariable UUID userId) {
        List<Fine> fines = fineService.getPendingFines(userId);
        return ResponseEntity.ok(fines);
    }

    @PostMapping("/pay-fine/{userId}")
    public ResponseEntity<String> payFine(@PathVariable UUID userId) {
        BigDecimal totalPaid = fineService.payFine(userId);
        return ResponseEntity.ok("Fine of " + totalPaid + " paid successfully.");
    }

    @PostMapping("/calculate/{fineId}")
    public ResponseEntity<String> calculateFine(@PathVariable UUID fineId,@PathVariable double finePerDay, @RequestParam String returnDate) {
        fineService.calculateFine(fineId, finePerDay,LocalDate.parse(returnDate));
        return ResponseEntity.ok("Fine calculated successfully.");
    }
}
