package com.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder(toBuilder = true)
public record ApiResponse (
     String message,
    Boolean successStatus,
     HttpStatus httpStatus
    ) {
}
