package com.hhm.api.service;

import com.hhm.api.model.dto.response.TransactionResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getAll();
}
