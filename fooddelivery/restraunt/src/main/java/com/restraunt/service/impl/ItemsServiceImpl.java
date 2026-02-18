package com.restraunt.service.impl;

import com.restraunt.dto.GetItemDetails;
import com.restraunt.dto.Item;
import com.restraunt.dto.ItemRequest;
import com.restraunt.dto.ItemResponse;
import com.restraunt.exception.ItemNotFoundException;
import com.restraunt.exception.RestrauntNotFoundException;
import com.restraunt.model.Inventory;
import com.restraunt.model.Items;
import com.restraunt.repository.InventoryRepository;
import com.restraunt.repository.ItemsRepository;
import com.restraunt.repository.RestrauntRepository;
import com.restraunt.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ItemsServiceImpl implements ItemService {

    private RestrauntRepository restrauntRepository;
    private InventoryRepository inventoryRepository;
    private ItemsRepository itemsRepository;

    @Override
    public ItemResponse addItems(ItemRequest request) {
        Boolean isExist = restrauntRepository.existsById(request.restrauntId());
        List<Items> itemsData = new LinkedList<>();
        List<Inventory> inventoryData = new LinkedList<>();
        if(isExist) {
            request.item().forEach(item ->
            {
               String itemId = UUID.randomUUID().toString();
               String inventoryId = UUID.randomUUID().toString();
                itemsData.add(Items.builder()
                        .itemId(itemId)
                        .itemName(item.itemName())
                        .createdAt(Date.from(Instant.now()))
                        .price(item.price())
                        .isOutOfStock(false)
                        .itemcategory(item.itemcategory())
                        .quantity(item.quantity())
                        .isVeg(item.isVeg()).build());
                inventoryData.add(Inventory.builder()
                        .inventoryId(inventoryId)
                        .restrauntId(itemId)
                        .restrauntId(request.restrauntId()).build());
            });

            // adding into DB if data is available
            if(!itemsData.isEmpty()) {
                itemsRepository.saveAll(itemsData);
                inventoryRepository.saveAll(inventoryData);
                log.info("Item data Saved Successfully");
                return ItemResponse.builder().message("Items Saved Successfully").build();

            } else {
                log.info("Item data is not available");
               return ItemResponse.builder()
                       .message("Something Went Wrong!!").build();
            }
        } else {
            throw new RestrauntNotFoundException("Restraunt Does not Exist");
        }
    }

    @Override
    public ItemResponse getItem(String itemId) {
        Optional<Items> item = itemsRepository.findById(itemId);
        if(item.isPresent()) {
           Items itemData = item.get();
          return ItemResponse.builder()
                   .itemDetails(GetItemDetails
                           .builder()
                           .itemId(itemData.getItemId())
                           .isOutOfStock(itemData.getIsOutOfStock())
                           .itemName(itemData.getItemName())
                           .isVeg(itemData.getIsVeg())
                           .price(itemData.getPrice())
                           .itemcategory(itemData.getItemcategory())
                           .build())
                   .message("Item Fetch Successfully").build();

        }

        throw new ItemNotFoundException("Item does not exists");
    }
}
