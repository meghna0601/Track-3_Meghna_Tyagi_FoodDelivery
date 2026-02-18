package com.restraunt.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record ItemResponse(
        String message,
        GetItemDetails itemDetails
) {
}
