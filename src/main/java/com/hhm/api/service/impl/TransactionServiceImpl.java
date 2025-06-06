package com.hhm.api.service.impl;

import com.hhm.api.model.dto.response.TransactionResponse;
import com.hhm.api.model.entity.Transaction;
import com.hhm.api.repository.TransactionRepository;
import com.hhm.api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private  final TransactionRepository transactionRepository;
    @Override
    public List<TransactionResponse> getAll() {
        List<Transaction> transactions = transactionRepository.findAll();

        List<TransactionResponse> responses = new ArrayList<>();

        for(Transaction x : transactions){
            TransactionResponse response = TransactionResponse.builder()
                    .id(x.getId())
                    .createdAt(x.getCreatedAt())
                    .amount(x.getAmount())
                    .paymentMethod(x.getPaymentMethod())
                    .transactionStatus(x.getTransactionStatus())
                    .transactionType(x.getTransactionType())
                    .userId(x.getUserId())
                    .build();

            responses.add(response);

        }

        return responses;
    }
}
