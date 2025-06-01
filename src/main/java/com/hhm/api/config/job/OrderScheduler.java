package com.hhm.api.config.job;

import com.hhm.api.model.entity.OrderItem;
import com.hhm.api.model.entity.Refund;
import com.hhm.api.repository.OrderItemRepository;
import com.hhm.api.repository.RefundRepository;
import com.hhm.api.support.enums.OrderItemStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderScheduler {
    private final OrderItemRepository orderItemRepository;
    private final RefundRepository refundRepository;

    @Scheduled(cron = "${scheduler.clear-expired-refund}")
    @Transactional
    public void doClearExpiredRefundTask() {
        log.warn("Scanning expired refund request...");

        List<OrderItem> orderItems = orderItemRepository.findAllExpiredRefund();

        orderItems.forEach(orderItem -> orderItem.setOrderItemStatus(OrderItemStatus.DELIVERED));

        List<UUID> orderItemIds = orderItems.stream()
                .map(OrderItem::getId)
                .toList();

        List<Refund> refunds = refundRepository.findAllByOrderItems(orderItemIds);

        refunds.forEach(refund -> refund.setDeleted(Boolean.TRUE));

        orderItemRepository.saveAll(orderItems);
        refundRepository.saveAll(refunds);

        log.warn("Finished clearing expired refund request!");
    }

    @Scheduled(cron = "${scheduler.complete-finished-order}")
    @Transactional
    public void doFinishOrderTask() {
        log.warn("Scanning finished order...");

        List<OrderItem> orderItems = orderItemRepository.findAllFinished();

        orderItems.forEach(orderItem -> orderItem.setOrderItemStatus(OrderItemStatus.COMPLETED));

        orderItemRepository.saveAll(orderItems);

        log.warn("Finished completing finished order!");
    }

    @Scheduled(cron = "${scheduler.cancel-pending-order}")
    @Transactional
    public void doCancelPendingOrderTask() {
        log.warn("Scanning expired pending order...");

        List<OrderItem> orderItems = orderItemRepository.findAllExpiredPending();

        orderItems.forEach(orderItem -> orderItem.setOrderItemStatus(OrderItemStatus.CANCELLED));

        orderItemRepository.saveAll(orderItems);

        log.warn("Finished cancel expired pending order!");
    }
}
