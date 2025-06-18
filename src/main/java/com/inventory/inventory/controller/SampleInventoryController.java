package com.inventory.inventory.controller;

import com.inventory.inventory.constant.UserRole;
import com.inventory.inventory.dto.request.SampleInventoryRequest;
import com.inventory.inventory.dto.request.UpdateCustomInventoryReq;
import com.inventory.inventory.dto.response.PaginatedResp;
import com.inventory.inventory.dto.response.SampleInventoryResponse;
import com.inventory.inventory.interceptor.UserAuthorization;
import com.inventory.inventory.service.SampleInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sample-inventory")
public class SampleInventoryController {
    private final SampleInventoryService sampleInventoryService;

    @PostMapping("/create")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG, UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager, UserRole.View_Manager, UserRole.Manager, UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<SampleInventoryResponse> createInventory(@RequestBody SampleInventoryRequest request) {
        return new ResponseEntity<>(sampleInventoryService.createInventory(request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG, UserRole.View_Manager, UserRole.Manager, UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<SampleInventoryResponse> getSampleInventoryById(@PathVariable Long id) {
        return new ResponseEntity<>(sampleInventoryService.getSampleInventoryById(id), HttpStatus.OK);
    }
    @GetMapping("/getInventoryByMemberAndProductId/{memberId}/{productId}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG, UserRole.View_Manager, UserRole.Manager, UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<SampleInventoryResponse> getInventoryByMemberAndProductId(@PathVariable Long memberId, @PathVariable Long productId) {
        return new ResponseEntity<>(sampleInventoryService.getInventoryByMemberAndProductId(memberId, productId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG, UserRole.Delete_Manager, UserRole.Super_Admin})
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        sampleInventoryService.deleteInventory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateCustomInventory")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG,UserRole.Create_Manager, UserRole.Edit_Manager, UserRole.Delete_Manager,UserRole.View_Manager,UserRole.Manager,UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<Void> updateCustomInventory(@RequestBody List<UpdateCustomInventoryReq> updateCustomInventoryReqList) {
        sampleInventoryService.updateCustomInventory(updateCustomInventoryReqList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateSampleInventoryQuantity/{id}/{quantity}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG, UserRole.Edit_Manager, UserRole.Manager, UserRole.Super_Admin})
    public ResponseEntity<SampleInventoryResponse> updateSampleInventoryQuantity(@PathVariable Long id, @PathVariable Integer quantity) {
        SampleInventoryResponse sampleInventoryResponse = sampleInventoryService.updateSampleInventoryQuantity(id, quantity);
        return new ResponseEntity<>(sampleInventoryResponse, HttpStatus.OK);
    }

    @GetMapping("/getAllSampleMemberById/{memberId}")
    @UserAuthorization(allowedRoles = {UserRole.ClientFMCG, UserRole.View_Manager, UserRole.Manager, UserRole.Reporting_Manager, UserRole.Super_Admin})
    public ResponseEntity<PaginatedResp<SampleInventoryResponse>> getAllSampleMemberById(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return new ResponseEntity<>(sampleInventoryService.getAllSampleMemberById(memberId, page, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
}
