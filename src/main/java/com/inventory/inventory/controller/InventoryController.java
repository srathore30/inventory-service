package com.inventory.inventory.controller;

import com.inventory.inventory.constant.UserRole;
import com.inventory.inventory.dto.request.*;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/inventory/create")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<InventoryResponse> createInventory(@RequestBody InventoryRequest request) {
        return new ResponseEntity<>(inventoryService.createInventory(request), HttpStatus.OK);
    }

    @PutMapping("/inventory/update/{productId}/{clientFmcgId}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<InventoryUpdateResponse> updateInventory(@PathVariable Long productId, @PathVariable Long clientFmcgId, @RequestBody InventoryUpdateRequest request) {
        return new ResponseEntity<>(inventoryService.updateInventory(clientFmcgId,productId, request), HttpStatus.OK);
    }

    @GetMapping("/inventory/{productId}")
//    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<PaginatedResp<InventoryResponse>> getInventoryByProductId(@PathVariable Long productId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection) {
        return new ResponseEntity<>(inventoryService.getInventory(productId, page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("/inventory/fill/{id}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<InventoryFillResponse> fillInventory(@PathVariable Long id, @RequestBody InventoryFillRequest request) {
        return new ResponseEntity<>(inventoryService.fillInventory(id, request), HttpStatus.OK);
    }

    @GetMapping("/inventory/all")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<PaginatedResp<InventoryResponse>> getAllInventory(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection) {
        return new ResponseEntity<>(inventoryService.getAllInventory(page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("/inventoryWithProductName/all/{clientId}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<PaginatedResp<InventoryResponse>> inventoryWithProductName(@PathVariable Long clientId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection) {
        return new ResponseEntity<>(inventoryService.inventoryWithProductName(clientId, page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("/inventoryWithProductNameWithClientFmcgResponse/all/{clientId}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<PaginatedResp<InventoryResponse>> inventoryWithProductNameWithClientFmcgResponse(@PathVariable Long clientId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection) {
        return new ResponseEntity<>(inventoryService.inventoryWithProductNameWithClientFmcgResponse(clientId, page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("getAllInventoryByClinetFmcgId/{clientFmcgId}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<PaginatedResp<InventoryResponse>> getAllInventoryByClientFmcgId(@PathVariable Long clientFmcgId,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection){
        return new ResponseEntity<>(inventoryService.getAllInventoryByClientFmcgId(clientFmcgId,page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }


    @PostMapping("/createInventoryInBulk")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<List<InventoryResponse>> createInventoryInBulk(@RequestBody InventoryBulkReq inventoryBulkReq){
        List<InventoryResponse> inventoryResponseList = inventoryService.createInventoryInBulk(inventoryBulkReq);
        return new ResponseEntity<>(inventoryResponseList, HttpStatus.OK);
    }

    @PutMapping("/updateInventoryInBulk")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<List<InventoryUpdateResponse>> updateInventoryInBulk(@RequestBody InventoryBulkUpdateReq inventoryBulkReq){
        List<InventoryUpdateResponse> inventoryResponseList = inventoryService.updateInventoryInBulk(inventoryBulkReq);
        return new ResponseEntity<>(inventoryResponseList, HttpStatus.OK);
    }

    @GetMapping("getInventoryByClientFmcgIdAndProductId")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<InventoryResponse> getInventoryByClientFmcgIdAndProductId(@RequestParam(defaultValue = "clientFmcgId") Long clientFmcgId,@RequestParam(defaultValue = "productId") Long productId){
        return new ResponseEntity<>(inventoryService.getInventoryByClientFmcgIdAndProductId(clientFmcgId,productId), HttpStatus.OK);
    }

    @GetMapping("getAllClientInventoryByMemberId/{memberId}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<PaginatedResp<InventoryResponse>> getAllClientInventoryByMemberId(@PathVariable Long memberId,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection){
        return new ResponseEntity<>(inventoryService.getAllClientInventoryByMemberId(memberId,page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

}
