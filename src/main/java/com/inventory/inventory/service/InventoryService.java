package com.inventory.inventory.service;

import com.inventory.inventory.constant.ApiErrorCodes;
import com.inventory.inventory.dto.request.InventoryRequest;
import com.inventory.inventory.dto.request.InventoryUpdateRequest;
import com.inventory.inventory.dto.response.InventoryResponse;
import com.inventory.inventory.dto.response.InventoryUpdateResponse;
import com.inventory.inventory.dto.response.PaginatedResp;
import com.inventory.inventory.entity.InventoryEntity;
import com.inventory.inventory.exception.NoSuchElementFoundException;
import com.inventory.inventory.repo.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryEntity dtoToEntity(InventoryRequest request) {
        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setProductId(request.getProductId());
        inventoryEntity.setSalesLevel(request.getSalesLevel());
        inventoryEntity.setQuantity(request.getQuantity());
        inventoryEntity.setUpdateAt(request.getUpdateAt());
        return inventoryEntity;
    }

    public InventoryResponse entityToDto(InventoryEntity inventoryEntity) {
        InventoryResponse response = new InventoryResponse();
        response.setProductId(inventoryEntity.getProductId());
        response.setSalesLevel(inventoryEntity.getSalesLevel());
        response.setQuantity(inventoryEntity.getQuantity());
        response.setUpdateAt(inventoryEntity.getUpdateAt());
        return response;
    }
    private InventoryUpdateResponse entityToUpdateDto(InventoryEntity inventoryEntity) {
        InventoryUpdateResponse response = new InventoryUpdateResponse();
        response.setProductId(inventoryEntity.getProductId());
        response.setSalesLevel(inventoryEntity.getSalesLevel());
        response.setRemainingStock(inventoryEntity.getQuantity());
        response.setMessage("Inventory Updated Successfully");
        return response;
    }
    public InventoryUpdateResponse updateInventory(Long productId,InventoryUpdateRequest request) {
        log.info("Inventory Updated Request for Sales Level : {}", request.getSalesLevel());
        log.info("Inventory Updated Request for Quantity Sold : {}", request.getQuantitySold());
        InventoryEntity inventoryEntity = inventoryRepository.findByProductIdAndSalesLevel(productId, request.getSalesLevel()).orElseThrow(() ->
                new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(),ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage()));
        inventoryEntity.setSalesLevel(request.getSalesLevel());
        if (inventoryEntity.getQuantity() == 0) {
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NULL.getErrorCode(),ApiErrorCodes.INVENTORY_NULL.getErrorMessage());
        }else if (inventoryEntity.getQuantity() < request.getQuantitySold()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_LESS_THAN_SOLD.getErrorCode(),ApiErrorCodes.INVENTORY_LESS_THAN_SOLD.getErrorMessage());
        }
        inventoryEntity.setQuantity(inventoryEntity.getQuantity() - request.getQuantitySold());
        inventoryRepository.save(inventoryEntity);
        log.info("Inventory Updated Created : {}", inventoryEntity);
        return entityToUpdateDto(inventoryEntity);
    }

    public InventoryResponse createInventory(InventoryRequest request) {
        log.info("Creating Inventory: {}", request);
        InventoryEntity inventoryEntity = dtoToEntity(request);
        inventoryRepository.save(inventoryEntity);
        log.info("Inventory Created Successfully: {}", inventoryEntity);
        return entityToDto(inventoryEntity);
    }
    public PaginatedResp<InventoryResponse> getInventory(Long productId, int page, int pageSize, String sortBy, String sortDirection) {
        log.info("Fetching Inventory: {} ", productId);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<InventoryEntity> byProductId = inventoryRepository.findByProductId(productId, pageable);
        if(byProductId.isEmpty()){
            throw  new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(),ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        }
        List<InventoryResponse> collect = byProductId.getContent().stream().map(this::entityToDto).collect(Collectors.toList());
        log.info("Inventory Fetched Successfully: {} ", collect);
        return PaginatedResp.<InventoryResponse>builder().totalElements(byProductId.getTotalElements()).totalPages(byProductId.getTotalPages()).page(page).content(collect).build();
    }
}
