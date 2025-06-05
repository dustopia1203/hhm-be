package com.hhm.api.model.entity;

import com.hhm.api.support.enums.ActiveStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "shipping",
        indexes = {
                @Index(name = "shipping_deleted_idx", columnList = "deleted"),
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Shipping extends AuditableEntity {
    @Id
    @Column()
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column()
    private String avatarUrl;

    @Column()
    private String duration;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus status;
}
