package com.restraunt.dto;

import com.restraunt.enums.ItemCategory;
import lombok.Builder;

@Builder(toBuilder = true)
public record Item(
        String itemName,
        ItemCategory itemcategory,
        Integer quantity,
        Integer price,
        Boolean isVeg
) {
}
