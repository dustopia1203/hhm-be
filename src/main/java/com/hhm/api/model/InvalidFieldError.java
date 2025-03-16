package com.hhm.api.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class InvalidFieldError implements Serializable {
    private String field;
    private String objectName;
    private String message;
}
