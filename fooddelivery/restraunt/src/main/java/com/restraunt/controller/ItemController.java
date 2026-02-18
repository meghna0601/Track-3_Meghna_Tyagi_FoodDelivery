package com.restraunt.controller;

import com.restraunt.dto.ItemRequest;
import com.restraunt.dto.ItemResponse;
import com.restraunt.dto.ItemUpdateQuantity;
import com.restraunt.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> additems(@RequestBody ItemRequest request) {
        log.info("Start Added Items");
        ItemResponse response = itemService.addItems(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ItemResponse> itemById(@RequestParam("id")String itemId) {
        log.info("Finding Item");
        ItemResponse response = itemService.getItem(itemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updateQuantity")
    public ResponseEntity<ItemResponse> updateQuantity(@RequestBody ItemUpdateQuantity request) {
        log.info("update quantity");
        ItemResponse response = itemService.updateQuantity(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ItemResponse> updateQuantity(@RequestParam String itemId) {
        log.info("deleting item");
        ItemResponse response = itemService.deleteQuantity(itemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
