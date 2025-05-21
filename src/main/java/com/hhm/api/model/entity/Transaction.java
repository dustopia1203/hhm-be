package com.hhm.api.model.entity;

import com.hhm.api.support.enums.PaymentMethod;
import com.hhm.api.support.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "transaction",
        indexes = {
                @Index(name = "transaction_user_id_idx", columnList = "user_id"),
                @Index(name = "transaction_deleted_idx", columnList = "deleted"),
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends AuditableEntity {
    @Id
    @Column()
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private String referenceContext;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    @Version
    private Long version;
}
