package com.hhm.api.repository;

import com.hhm.api.model.entity.Transaction;
import com.hhm.api.repository.custom.TransactionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, TransactionRepositoryCustom {
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.transactionType = 'OUT'
          AND t.deleted = false
          AND t.userId = :userId
    """)
    BigDecimal findUserBalance(UUID userId);
}
