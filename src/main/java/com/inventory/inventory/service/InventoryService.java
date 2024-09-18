package com.inventory.inventory.service;

import com.inventory.inventory.constant.ApiErrorCodes;
import com.inventory.inventory.dto.request.InventoryRequest;
import com.inventory.inventory.dto.request.InventoryUpdateRequest;
import com.inventory.inventory.dto.response.InventoryResponse;
import com.inventory.inventory.dto.response.InventoryUpdateResponse;
import com.inventory.inventory.entity.InventoryEntity;
import com.inventory.inventory.exception.NoSuchElementFoundException;
import com.inventory.inventory.repo.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        inventoryEntity.setWarehouseStock(request.getWarehouseStock());
        inventoryEntity.setRetailStock(request.getRetailStock());
        inventoryEntity.setStockistStock(request.getStockistStock());
        inventoryEntity.setUnitOfMeasure(request.getUnitOfMeasure());
        return inventoryEntity;
    }

    public InventoryResponse entityToDto(InventoryEntity inventoryEntity) {
        InventoryResponse response = new InventoryResponse();
        response.setProductId(inventoryEntity.getProductId());
        response.setSalesLevel(inventoryEntity.getSalesLevel());
        response.setQuantity(inventoryEntity.getQuantity());
        response.setUpdateAt(inventoryEntity.getUpdateAt());
        response.setWarehouseStock(inventoryEntity.getWarehouseStock());
        response.setRetailStock(inventoryEntity.getRetailStock());
        response.setStockistStock(inventoryEntity.getStockistStock());
        response.setUnitOfMeasure(inventoryEntity.getUnitOfMeasure());
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

    public InventoryUpdateResponse updateInventory(Long productId, InventoryUpdateRequest request) {
        Optional<InventoryEntity> byProductId = inventoryRepository.findByProductId(productId);
        if (byProductId.isEmpty()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.ORDER_NOT_FOUND.getErrorCode(), ApiErrorCodes.ORDER_NOT_FOUND.getErrorMessage());
        } InventoryEntity inventoryEntity = byProductId.get();
        switch (request.getSalesLevel()) {
            case RETAILER:
                inventoryEntity.setRetailStock(inventoryEntity.getRetailStock() - request.getQuantitySold());
                break;
            case STOCKIST:
                inventoryEntity.setStockistStock(inventoryEntity.getStockistStock() - request.getQuantitySold());
                break;
            case WAREHOUSE:
                inventoryEntity.setWarehouseStock(inventoryEntity.getWarehouseStock() - request.getQuantitySold());
                break;
        }
        inventoryEntity.setSalesLevel(request.getSalesLevel());
        inventoryEntity.setQuantity(inventoryEntity.getQuantity() - request.getQuantitySold());
        inventoryRepository.save(inventoryEntity);
        return entityToUpdateDto(inventoryEntity);
    }

    public InventoryResponse createInventory(InventoryRequest request) {
        InventoryEntity inventoryEntity = dtoToEntity(request);
        inventoryRepository.save(inventoryEntity);
        return entityToDto(inventoryEntity);
    }

    public InventoryResponse getInventory(Long productId) {
        Optional<InventoryEntity> byProductId = inventoryRepository.findByProductId(productId);
        if (byProductId.isEmpty()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        } InventoryEntity inventoryEntity = byProductId.get();
        return entityToDto(inventoryEntity);
    }

}
