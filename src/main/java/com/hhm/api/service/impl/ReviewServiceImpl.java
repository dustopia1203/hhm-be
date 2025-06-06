package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.mapper.AutoMapper;
import com.hhm.api.model.dto.request.ReviewCreateRequest;
import com.hhm.api.model.dto.request.ReviewSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.ReviewResponse;
import com.hhm.api.model.entity.OrderItem;
import com.hhm.api.model.entity.Review;
import com.hhm.api.model.entity.User;
import com.hhm.api.model.entity.UserInformation;
import com.hhm.api.repository.OrderItemRepository;
import com.hhm.api.repository.ReviewRepository;
import com.hhm.api.repository.UserInformationRepository;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.service.ReviewService;
import com.hhm.api.support.enums.OrderItemStatus;
import com.hhm.api.support.enums.error.AuthorizationError;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import com.hhm.api.support.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final UserInformationRepository userInformationRepository;
    private final AutoMapper autoMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public PageDTO<ReviewResponse> search(ReviewSearchRequest request) {
        Long count = reviewRepository.count(request);

        if (Objects.equals(count, 0L)) {
            return PageDTO.empty(request.getPageIndex(), request.getPageSize());
        }

        List<Review> reviews = reviewRepository.search(request);

        List<UUID> userIds = reviews.stream()
                .map(Review::getUserId)
                .toList();

        List<User> users = userRepository.findByIds(userIds);

        List<UserInformation> userInformations = userInformationRepository.findByIds(userIds);

        List<ReviewResponse> responses = new ArrayList<>();

        reviews.forEach(review -> {
            ReviewResponse response = autoMapper.toResponse(review);

            if (Objects.nonNull(review.getContentUrls())) {
                response.setImages(review.getContentUrls().split(";"));
            }

            Optional<User> userOptional = users.stream()
                    .filter(user -> Objects.equals(user.getId(), review.getUserId()))
                    .findFirst();

            if (userOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.USER_NOT_FOUND);
            }

            User user = userOptional.get();

            response.setUsername(user.getUsername());

            Optional<UserInformation> userInformationOptional = userInformations.stream()
                    .filter(userInformation -> Objects.equals(userInformation.getUserId(), review.getUserId()))
                    .findFirst();

            userInformationOptional.ifPresent(userInformation -> response.setUserAvatar(userInformation.getAvatarUrl()));

            responses.add(response);
        });

        return PageDTO.of(responses, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    @Transactional
    public Review createMy(ReviewCreateRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(request.getOrderItemId());

        if (orderItemOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.ORDER_ITEM_NOT_FOUND);
        }

        OrderItem orderItem = orderItemOptional.get();

        if (!Objects.equals(orderItem.getUserId(), userId)) {
            throw new ResponseException(AuthorizationError.ACCESS_DENIED);
        }

        Review review = Review.builder()
                .id(IdUtils.nextId())
                .userId(userId)
                .shopId(orderItem.getShopId())
                .productId(orderItem.getProductId())
                .orderItemId(orderItem.getId())
                .rating(request.getRating())
                .description(request.getDescription())
                .contentUrls(request.getContentUrls())
                .deleted(Boolean.FALSE)
                .build();

        orderItem.setOrderItemStatus(OrderItemStatus.REVIEWED);

        reviewRepository.save(review);
        orderItemRepository.save(orderItem);

        return review;
    }

    @Override
    public PageDTO<ReviewResponse> findAllByShopId(UUID shopId) {
        List<Review> reviews = reviewRepository.findAllByShopId(shopId);

        long count = reviews.size();

        if(count==0){
            return PageDTO.empty(0,0);
        }

        List<ReviewResponse> responses = new ArrayList<>();

        reviews.forEach(review -> {
            ReviewResponse response = autoMapper.toResponse(review);

            if (Objects.nonNull(review.getContentUrls())) {
                response.setImages(review.getContentUrls().split(";"));
            }

            Optional<User> userOptional = userRepository.findById(review.getUserId());

            if (userOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.USER_NOT_FOUND);
            }

            User user = userOptional.get();

            response.setUsername(user.getUsername());

            Optional<UserInformation> userInformationOptional = userInformationRepository.findByUserId(user.getId());

            userInformationOptional.ifPresent(userInformation -> response.setUserAvatar(userInformation.getAvatarUrl()));

            responses.add(response);
        });

        int pageSize = (int) count/10;

        return PageDTO.of(responses, 0, pageSize+1, count);
    }
}
