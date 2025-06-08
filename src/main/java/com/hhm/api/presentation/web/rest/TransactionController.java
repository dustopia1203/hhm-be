package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.TransactionSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.TransactionResponse;
import com.hhm.api.model.entity.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Transaction Resources")
@RequestMapping("/api/transactions")
@Validated
public interface TransactionController {
    @Operation(summary = "Search transactions")
    @GetMapping("/q")
    @PreAuthorize("hasPermission(null, 'TRANSACTION:READ')")
    PagingResponse<TransactionResponse> search(@ValidatePaging(sortModel = Transaction.class) TransactionSearchRequest request);
}
