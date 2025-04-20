package com.hhm.api.model.dto.request;

import com.hhm.api.support.enums.SortOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PagingRequest extends Request {
    protected String keyword;

    @Min(value = 1, message = "PAGE_INDEX_MIN")
    @Max(value = 200, message = "PAGE_INDEX_MAX")
    protected int pageIndex = 1;

    @Min(value = 1, message = "PAGE_SIZE_MIN")
    @Max(value = 200, message = "PAGE_SIZE_MAX")
    protected int pageSize = 10;

    protected String sortBy;

    protected SortOrder sortOrder;
}
