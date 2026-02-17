package com.restraunt.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder(toBuilder = true)
public record ApiResponse(
     String message,
    Boolean successStatus,
     HttpStatus httpStatus
    ) {
}
