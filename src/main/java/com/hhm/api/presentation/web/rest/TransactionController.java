package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Transaction Resources")
@RequestMapping("/api/transactions")
@Validated
public interface TransactionController {
    @Operation(summary = "Get all Transaction")
    @GetMapping("/")
    Response<List<TransactionResponse>> getAll();
}
