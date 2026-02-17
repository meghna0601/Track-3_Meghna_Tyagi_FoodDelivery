package com.restraunt.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record SuspendRestrauntRequest(
        String restrauntId,
        String suspendReason
) {

}
