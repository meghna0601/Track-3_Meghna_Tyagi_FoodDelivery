package com.restraunt.dto;

import com.restraunt.enums.ItemCategory;
import lombok.Builder;

@Builder(toBuilder = true)
public record GetItemDetails(
        String itemId,
        String itemName,
        ItemCategory itemcategory,
        Integer price,
        Boolean isVeg,
        Boolean isOutOfStock
) {
}
