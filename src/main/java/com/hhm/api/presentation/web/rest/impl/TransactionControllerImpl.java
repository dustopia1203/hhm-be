package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.TransactionResponse;
import com.hhm.api.presentation.web.rest.TransactionController;
import com.hhm.api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionControllerImpl implements TransactionController {
    private final TransactionService transactionService;

    @Override
    public Response<List<TransactionResponse>> getAll() {
        return Response.of(transactionService.getAll());
    }
}
