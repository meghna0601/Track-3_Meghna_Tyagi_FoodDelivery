package com.user.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record GetUserResponse(
        String message,
        GetUserDetails userDetails
) {
}
