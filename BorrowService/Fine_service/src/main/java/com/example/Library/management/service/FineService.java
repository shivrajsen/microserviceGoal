package com.example.Library.management.service;



import com.example.Library.management.entity.Fine;
import com.example.Library.management.repository.FineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FineService {

    private final FineRepository fineRepository;

    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

   public List<Fine> getPendingFines(UUID userId) {
        return fineRepository.findByUserIdAndPaidFalse(userId);
    }



    @Transactional
    public BigDecimal payFine(UUID userId) {
        List<Fine> pendingFines = fineRepository.findByUserIdAndPaidFalse(userId);

        BigDecimal totalFine = pendingFines.stream()
                .map(Fine::getFineAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pendingFines.forEach(fine -> fine.setPaid(true));
        fineRepository.saveAll(pendingFines);

        return totalFine;
    }

    @Transactional
    public void calculateFine(UUID fineId,double finePerDay, LocalDate returnDate) {
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new RuntimeException("Fine not found"));

        LocalDate dueDate = fine.getDueDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        fine.setReturnDate(Date.from(returnDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));


        if (!returnDate.isBefore(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine.setFineAmount(BigDecimal.valueOf(daysLate * finePerDay));
        } else {
            fine.setFineAmount(BigDecimal.ZERO);
        }

        fineRepository.save(fine);
    }
}
