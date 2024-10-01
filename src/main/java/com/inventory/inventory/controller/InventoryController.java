package com.inventory.inventory.controller;

import com.inventory.inventory.constant.UserRole;
import com.inventory.inventory.dto.request.InventoryFillRequest;
import com.inventory.inventory.dto.request.InventoryRequest;
import com.inventory.inventory.dto.request.InventoryUpdateRequest;
import com.inventory.inventory.dto.response.InventoryFillResponse;
import com.inventory.inventory.dto.response.InventoryResponse;
import com.inventory.inventory.dto.response.InventoryUpdateResponse;
import com.inventory.inventory.dto.response.PaginatedResp;
import com.inventory.inventory.interceptor.UserAuthorization;
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
//    @UserAuthorization(allowedRoles = {UserRole.Edit_Manager})
    public ResponseEntity<InventoryResponse> updateInventory(@RequestBody InventoryRequest request) {
        return new ResponseEntity<>(inventoryService.createInventory(request), HttpStatus.OK);
    }

    @PutMapping("/inventory/update/{id}")
//    @UserAuthorization(allowedRoles = {UserRole.Manager})
    public ResponseEntity<InventoryUpdateResponse> updateInventory(@PathVariable Long id, @RequestBody InventoryUpdateRequest request) {
        return new ResponseEntity<>(inventoryService.updateInventory(id, request), HttpStatus.OK);
    }


    @GetMapping("/inventory/{productId}")
    public ResponseEntity<PaginatedResp<InventoryResponse>> getInventoryByProductId(@PathVariable Long productId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection) {
        return new ResponseEntity<>(inventoryService.getInventory(productId, page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("/inventory/fill/{id}")
//    @UserAuthorization(allowedRoles = {UserRole.Manager})
    public ResponseEntity<InventoryFillResponse> fillInventory(@PathVariable Long id, @RequestBody InventoryFillRequest request) {
        return new ResponseEntity<>(inventoryService.fillInventory(id, request), HttpStatus.OK);
    }

    @GetMapping("/inventory/all")
    public ResponseEntity<PaginatedResp<InventoryResponse>> getAllInventory(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection) {
        return new ResponseEntity<>(inventoryService.getAllInventory(page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

}
