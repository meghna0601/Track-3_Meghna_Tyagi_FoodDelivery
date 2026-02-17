package com.restraunt.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record RestrauntRequest(
         String ownerUserId,
         String restrauntName,
         String restrauntAddress,
         Double latitude,
         Double longitude,
         Integer hours,
         Integer deliveryRadiusKm
) {
}
