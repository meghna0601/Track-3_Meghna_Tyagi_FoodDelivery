package com.restraunt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/inventory")
public class InventoryController {

    @PostMapping
    public ResponseEntity<InventoryResponse> addItems(@RequestBody InventoryRequest inventoryRequest) {

    }
}
