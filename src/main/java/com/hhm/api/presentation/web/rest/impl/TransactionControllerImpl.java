package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.TransactionSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.TransactionResponse;
import com.hhm.api.presentation.web.rest.TransactionController;
import com.hhm.api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionControllerImpl implements TransactionController {
    private final TransactionService transactionService;

    @Override
    public PagingResponse<TransactionResponse> search(TransactionSearchRequest request) {
        return PagingResponse.of(transactionService.search(request));
    }
}
