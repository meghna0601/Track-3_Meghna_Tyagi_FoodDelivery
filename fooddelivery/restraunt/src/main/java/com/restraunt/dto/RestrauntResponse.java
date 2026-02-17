package com.restraunt.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record RestrauntResponse(
        String restrauntId,
        String message,
        RestrauntRequest details
) {
}
