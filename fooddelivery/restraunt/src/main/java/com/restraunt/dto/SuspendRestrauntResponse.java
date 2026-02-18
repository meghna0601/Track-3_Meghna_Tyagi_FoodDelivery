package com.restraunt.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record SuspendRestrauntResponse(
        String message,
        String reason
) {
}
