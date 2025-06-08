package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.mapper.AutoMapper;
import com.hhm.api.model.dto.request.TransactionSearchRequest;
import com.hhm.api.model.dto.response.TransactionResponse;
import com.hhm.api.model.entity.Transaction;
import com.hhm.api.model.entity.User;
import com.hhm.api.repository.TransactionRepository;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.service.TransactionService;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AutoMapper autoMapper;
    private final UserRepository userRepository;

    @Override
    public PageDTO<TransactionResponse> search(TransactionSearchRequest request) {
        Long count = transactionRepository.count(request);

        if (Objects.equals(count, 0L)) {
            return PageDTO.empty(request.getPageIndex(), request.getPageSize());
        }

        List<Transaction> transactions = transactionRepository.search(request);

        List<UUID> userIds = transactions.stream()
                .map(Transaction::getUserId)
                .toList();

        List<User> users = userRepository.findByIds(userIds);

        List<TransactionResponse> responses = new ArrayList<>();

        transactions.forEach(transaction -> {
            TransactionResponse response = autoMapper.toResponse(transaction);

            User user = users.stream()
                    .filter(item -> Objects.equals(item.getId(), transaction.getUserId()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

            response.setUsername(user.getUsername());

            responses.add(response);
        });

        return PageDTO.of(responses, request.getPageIndex(), request.getPageSize(), count);
    }
}
