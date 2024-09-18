package com.inventory.inventory.controller;

import com.inventory.inventory.dto.request.InventoryRequest;
import com.inventory.inventory.dto.request.InventoryUpdateRequest;
import com.inventory.inventory.dto.response.InventoryResponse;
import com.inventory.inventory.dto.response.InventoryUpdateResponse;
import com.inventory.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    @PostMapping("/inventory/create")
    public ResponseEntity<InventoryResponse> updateInventory(@RequestBody InventoryRequest request) {
        return new ResponseEntity<>(inventoryService.createInventory(request), HttpStatus.CREATED);
    }
    @PutMapping("/inventory/update/{id}")
    public ResponseEntity<InventoryUpdateResponse> updateInventory(@PathVariable Long id, @RequestBody InventoryUpdateRequest request) {
        return new ResponseEntity<>(inventoryService.updateInventory(id, request), HttpStatus.ACCEPTED);
    }
    @GetMapping("/inventory/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId) {
        return new ResponseEntity<>(inventoryService.getInventory(productId), HttpStatus.OK);
    }
}
