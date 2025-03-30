package com.hhm.api.model.entity;

import com.hhm.api.support.constants.ValidateConstraint;
import com.hhm.api.support.enums.ActiveStatus;
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
        name = "product",
        indexes = {
                @Index(name = "product_shop_id_idx", columnList = "shop_id"),
                @Index(name = "product_category_id_idx", columnList = "category_id"),
                @Index(name = "product_deleted_idx", columnList = "deleted"),
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AuditableEntity {
    @Id
    @Column()
    private UUID id;

    @Column(nullable = false)
    private UUID shopId;

    @Column(nullable = false)
    private UUID categoryId;

    @Column(length = ValidateConstraint.Length.NAME_MAX_LENGTH, nullable = false)
    private String name;

    @Column()
    private String description;

    @Column()
    private String contentUrls;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus status;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    @Version
    private Long version;
}
