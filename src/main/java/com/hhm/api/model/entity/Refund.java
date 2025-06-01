package com.hhm.api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "refund",
        indexes = {
                @Index(name = "refund_order_item_id_idx", columnList = "order_item_id"),
                @Index(name = "refund_deleted_idx", columnList = "deleted"),
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Refund extends AuditableEntity {
    @Id
    @Column()
    private UUID id;

    @Column(nullable = false)
    private UUID orderItemId;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String images;

    @Column()
    private String note;

    @Column(nullable = false)
    private Boolean deleted;
}
