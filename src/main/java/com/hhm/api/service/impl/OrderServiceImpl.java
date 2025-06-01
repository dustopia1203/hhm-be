package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.mapper.AutoMapper;
import com.hhm.api.model.dto.request.OrderCreateRequest;
import com.hhm.api.model.dto.request.OrderItemCreateRequest;
import com.hhm.api.model.dto.request.OrderItemSearchRequest;
import com.hhm.api.model.dto.request.RefundRequest;
import com.hhm.api.model.dto.request.SolanaOrderCreateRequest;
import com.hhm.api.model.dto.request.VNPayOrderCreateRequest;
import com.hhm.api.model.dto.response.OrderItemResponse;
import com.hhm.api.model.entity.OrderItem;
import com.hhm.api.model.entity.Product;
import com.hhm.api.model.entity.Refund;
import com.hhm.api.model.entity.Shipping;
import com.hhm.api.model.entity.Shop;
import com.hhm.api.model.entity.Transaction;
import com.hhm.api.repository.OrderItemRepository;
import com.hhm.api.repository.ProductRepository;
import com.hhm.api.repository.RefundRepository;
import com.hhm.api.repository.ShippingRepository;
import com.hhm.api.repository.ShopRepository;
import com.hhm.api.repository.TransactionRepository;
import com.hhm.api.service.OrderService;
import com.hhm.api.support.enums.OrderItemStatus;
import com.hhm.api.support.enums.PaymentMethod;
import com.hhm.api.support.enums.TransactionStatus;
import com.hhm.api.support.enums.TransactionType;
import com.hhm.api.support.enums.error.BadRequestError;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import com.hhm.api.support.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderItemRepository orderItemRepository;
    private final ShippingRepository shippingRepository;
    private final ProductRepository productRepository;
    private final AutoMapper autoMapper;
    private final ShopRepository shopRepository;
    private final RefundRepository refundRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public PageDTO<OrderItemResponse> searchOrderItem(OrderItemSearchRequest request) {
        return queryOrderItem(request);
    }

    @Override
    public PageDTO<OrderItemResponse> searchMyOrderItem(OrderItemSearchRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        request.setUserIds(List.of(userId));

        return queryOrderItem(request);
    }

    @Override
    public PageDTO<OrderItemResponse> searchMyShopOrderItem(OrderItemSearchRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<Shop> shopOptional = shopRepository.findByUser(userId);

        if (shopOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
        }

        Shop shop = shopOptional.get();

        request.setShopIds(List.of(shop.getId()));

        return queryOrderItem(request);
    }

    @Override
    @Transactional
    public List<OrderItem> codPaymentMyOrder(OrderCreateRequest request) {
        return createOrder(request, PaymentMethod.CASH, null);
    }

    @Override
    @Transactional
    public List<OrderItem> vnPayPaymentMyOrder(VNPayOrderCreateRequest request) {
        return createOrder(request, PaymentMethod.VN_PAY, request.getTransactionNumber());
    }

    @Override
    public List<OrderItem> solanaPaymentMyOrder(SolanaOrderCreateRequest request) {
        return createOrder(request, PaymentMethod.CRYPTO, request.getReference());
    }

    @Override
    @Transactional
    public void refundMy(UUID id, RefundRequest request) {
        OrderItem orderItem = getMyOrderItem(id);

        if (!Objects.equals(orderItem.getOrderItemStatus(), OrderItemStatus.DELIVERED)) {
            throw new ResponseException(BadRequestError.ORDER_ITEM_ACTION_INVALID);
        }

        orderItem.setOrderItemStatus(OrderItemStatus.REFUND_PROGRESSING);

        Refund refund = Refund.builder()
                .id(IdUtils.nextId())
                .orderItemId(id)
                .reason(request.getReason())
                .images(request.getImages())
                .deleted(Boolean.FALSE)
                .build();

        refundRepository.save(refund);
        orderItemRepository.save(orderItem);
    }

    @Override
    public void completedMy(UUID id) {
        OrderItem orderItem = getMyOrderItem(id);

        if (!Objects.equals(orderItem.getOrderItemStatus(), OrderItemStatus.DELIVERED)) {
            throw new ResponseException(BadRequestError.ORDER_ITEM_ACTION_INVALID);
        }

        orderItem.setOrderItemStatus(OrderItemStatus.COMPLETED);

        orderItemRepository.save(orderItem);
    }

    private PageDTO<OrderItemResponse> queryOrderItem(OrderItemSearchRequest request) {
        Long count = orderItemRepository.count(request);

        if (Objects.equals(count, 0L)) {
            return PageDTO.empty(request.getPageIndex(), request.getPageSize());
        }

        List<OrderItem> orderItems = orderItemRepository.search(request);

        List<UUID> productIds = orderItems.stream()
                .map(OrderItem::getProductId)
                .toList();

        List<Product> products = productRepository.findByIds(productIds);

        List<UUID> shopIds = orderItems.stream()
                .map(OrderItem::getShopId)
                .toList();

        List<Shop> shops = shopRepository.findByIds(shopIds);

        List<OrderItemResponse> responses = new ArrayList<>();

        orderItems.forEach(orderItem -> {
            OrderItemResponse response = autoMapper.toResponse(orderItem);

            Optional<Shop> shopOptional = shops.stream()
                    .filter(shop -> Objects.equals(shop.getId(), orderItem.getShopId()))
                    .findFirst();

            if (shopOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
            }

            Shop shop = shopOptional.get();

            Optional<Product> productOptional = products.stream()
                    .filter(product -> Objects.equals(product.getId(), orderItem.getProductId()))
                    .findFirst();

            if (productOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.PRODUCT_NOT_FOUND);
            }

            Product product = productOptional.get();

            response.setShopName(shop.getName());
            response.setProductName(product.getName());
            response.setProductImage(product.getContentUrls().split(";")[0]);

            responses.add(response);
        });

        return PageDTO.of(responses, request.getPageIndex(), request.getPageSize(), count);
    }

    private OrderItem getMyOrderItem(UUID id) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<OrderItem> orderItemOptional = orderItemRepository.findByIdAndUser(id, userId);

        if (orderItemOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.ORDER_ITEM_NOT_FOUND);
        }

        return orderItemOptional.get();
    }

    private List<OrderItem> createOrder(OrderCreateRequest request, PaymentMethod paymentMethod, String referenceContext) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<Shipping> shippingOptional = shippingRepository.findById(request.getShippingId());

        if (shippingOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.SHIPPING_NOT_FOUND);
        }

        Shipping shipping = shippingOptional.get();

        List<UUID> productIds = request.getOrderItemCreateRequests().stream()
                .map(OrderItemCreateRequest::getProductId)
                .toList();

        List<Product> products = productRepository.findByIds(productIds);

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalAmount = request.getOrderItemCreateRequests().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Transaction transaction = Transaction.builder()
                .id(IdUtils.nextId())
                .userId(userId)
                .amount(totalAmount)
                .paymentMethod(paymentMethod)
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.IN)
                .referenceContext(Objects.isNull(referenceContext) ? RandomStringUtils.randomAlphabetic(10) : referenceContext)
                .deleted(Boolean.FALSE)
                .build();

        request.getOrderItemCreateRequests().forEach(item -> {
            Optional<Product> productOptional = products.stream()
                    .filter(product -> Objects.equals(product.getId(), item.getProductId()))
                    .findFirst();

            if (productOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.PRODUCT_NOT_FOUND);
            }

            Product product = productOptional.get();

            OrderItem orderItem = OrderItem.builder()
                    .id(IdUtils.nextId())
                    .userId(userId)
                    .productId(product.getId())
                    .shopId(product.getShopId())
                    .shippingId(shipping.getId())
                    .price(item.getPrice())
                    .shippingPrice(shipping.getPrice())
                    .amount(item.getAmount())
                    .address(request.getAddress())
                    .orderItemStatus(OrderItemStatus.PENDING)
                    .transactionId(transaction.getId())
                    .deleted(Boolean.FALSE)
                    .build();

            orderItems.add(orderItem);
        });

        transactionRepository.save(transaction);
        orderItemRepository.saveAll(orderItems);

        return orderItems;
    }
}
