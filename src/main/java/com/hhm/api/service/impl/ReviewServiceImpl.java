package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.mapper.AutoMapper;
import com.hhm.api.model.dto.request.ReviewCreateRequest;
import com.hhm.api.model.dto.request.ReviewSearchRequest;
import com.hhm.api.model.dto.response.ReviewResponse;
import com.hhm.api.model.entity.Review;
import com.hhm.api.model.entity.User;
import com.hhm.api.model.entity.UserInformation;
import com.hhm.api.repository.ReviewRepository;
import com.hhm.api.repository.UserInformationRepository;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.service.ReviewService;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
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

            response.setImages(review.getContentUrls().split(";"));

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
    public Review createMy(ReviewCreateRequest request) {
        return null;
    }
}
