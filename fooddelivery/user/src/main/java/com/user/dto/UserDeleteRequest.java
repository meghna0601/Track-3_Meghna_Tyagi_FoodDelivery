package com.user.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserDeleteRequest(
        String userId
) {
}
