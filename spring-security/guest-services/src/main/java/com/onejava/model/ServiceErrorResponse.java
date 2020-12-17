package com.onejava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onejava.exception.ServiceErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class ServiceErrorResponse {
    private String errorMessage;
    private ServiceErrorCode errorCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime timestamp;
}
