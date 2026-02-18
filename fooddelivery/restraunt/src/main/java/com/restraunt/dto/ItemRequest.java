package com.restraunt.dto;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ItemRequest(
        String restrauntId,
        List<Item> item
) {
}
