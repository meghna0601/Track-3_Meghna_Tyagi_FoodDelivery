package com.restraunt.service;

import com.restraunt.dto.GetItemDetails;
import com.restraunt.dto.ItemRequest;
import com.restraunt.dto.ItemResponse;
import com.restraunt.dto.ItemUpdateQuantity;

public interface ItemService {
    ItemResponse addItems(ItemRequest request);

    ItemResponse getItem(String itemId);

    ItemResponse updateQuantity(ItemUpdateQuantity request);

    ItemResponse deleteQuantity(String itemId);
}
