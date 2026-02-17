package com.restraunt.dto;

import com.restraunt.enums.RestrauntStatus;
import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateRestrauntRequest(
        RestrauntStatus status
) {
}
