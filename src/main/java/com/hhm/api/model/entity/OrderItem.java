package com.hhm.api.model.entity;

import com.hhm.api.support.enums.OrderItemStatus;
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
        name = "order_item",
        indexes = {
                @Index(name = "order_item_user_id_idx", columnList = "user_id"),
                @Index(name = "order_item_product_id_idx", columnList = "product_id"),
                @Index(name = "order_item_shop_id_idx", columnList = "shop_id"),
                @Index(name = "order_item_shipping_id_idx", columnList = "shipping_id"),
                @Index(name = "order_item_transaction_id_idx", columnList = "transaction_id"),
                @Index(name = "order_item_deleted_idx", columnList = "deleted"),
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends AuditableEntity {
    @Id
    @Column()
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID shopId;

    @Column(nullable = false)
    private UUID shippingId;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal shippingPrice;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderItemStatus orderItemStatus;

    @Column(nullable = false)
    private UUID transactionId;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    @Version
    private Long version;
}
