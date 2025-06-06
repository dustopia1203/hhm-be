package com.hhm.api.repository;

import com.hhm.api.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.transactionType = 'OUT'
          AND t.deleted = false
          AND t.userId = :userId
    """)
    BigDecimal findUserBalance(UUID userId);

    @Query("SELECT t FROM Transaction t WHERE t.deleted = false ")
    List<Transaction> findAll();
}
