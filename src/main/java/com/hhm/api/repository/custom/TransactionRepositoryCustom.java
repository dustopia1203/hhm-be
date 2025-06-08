package com.hhm.api.repository.custom;

import com.hhm.api.model.dto.request.TransactionSearchRequest;
import com.hhm.api.model.entity.Transaction;

import java.util.List;

public interface TransactionRepositoryCustom {
    Long count(TransactionSearchRequest request);

    List<Transaction> search(TransactionSearchRequest request);
}
