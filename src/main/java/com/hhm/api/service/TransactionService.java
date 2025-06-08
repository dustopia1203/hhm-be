package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.TransactionSearchRequest;
import com.hhm.api.model.dto.response.TransactionResponse;

public interface TransactionService {
    PageDTO<TransactionResponse> search(TransactionSearchRequest request);
}
