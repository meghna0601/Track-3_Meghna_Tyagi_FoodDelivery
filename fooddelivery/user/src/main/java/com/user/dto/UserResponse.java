package com.user.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserResponse(
        String userId,
        String message
) {
}
