package com.inventory.inventory.service;

import com.inventory.inventory.constant.ApiErrorCodes;
import com.inventory.inventory.dto.request.*;
import com.inventory.inventory.dto.response.InventoryFillResponse;
import com.inventory.inventory.dto.response.InventoryResponse;
import com.inventory.inventory.dto.response.InventoryUpdateResponse;
import com.inventory.inventory.dto.response.PaginatedResp;
import com.inventory.inventory.entity.InventoryEntity;
import com.inventory.inventory.exception.NoSuchElementFoundException;
import com.inventory.inventory.repo.InventoryRepository;
import com.inventory.inventory.utill.ExternalRestService;
import com.inventory.inventory.utill.ProductServiceClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductServiceClient productServiceClient;
    private final ExternalRestService externalRestService;

    public InventoryEntity dtoToEntity(InventoryRequest request) {
        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setProductId(request.getProductId());
        inventoryEntity.setSalesLevel(request.getSalesLevel());
        inventoryEntity.setQuantity(request.getQuantity());
        inventoryEntity.setUpdateAt(request.getUpdateAt());
        inventoryEntity.setClientId(request.getClientId());
        return inventoryEntity;
    }

    public InventoryResponse entityToDto(InventoryEntity inventoryEntity) {
        InventoryResponse response = new InventoryResponse();
        response.setProductId(inventoryEntity.getProductId());
        response.setSalesLevel(inventoryEntity.getSalesLevel());
        response.setQuantity(inventoryEntity.getQuantity());
        response.setUpdateAt(inventoryEntity.getUpdateAt());
        response.setInventoryId(inventoryEntity.getId());
        response.setClientId(inventoryEntity.getClientId());
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

    public InventoryUpdateResponse updateInventory(Long clientFmcgId, Long productId,InventoryUpdateRequest request) {
        log.info("Inventory Updated Request for Sales Level : {}", request.getSalesLevel());
        log.info("Inventory Updated Request for Quantity Sold : {}", request.getQuantitySold());
        InventoryEntity inventoryEntity = inventoryRepository.findByClientIdAndProductIdAndSalesLevel(clientFmcgId,productId, request.getSalesLevel()).orElseThrow(() ->
                new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(),ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage()));
        inventoryEntity.setSalesLevel(request.getSalesLevel());
//        if (inventoryEntity.getQuantity() == 0) {
//            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NULL.getErrorCode(),ApiErrorCodes.INVENTORY_NULL.getErrorMessage());
//        }else if (inventoryEntity.getQuantity() < request.getQuantitySold()) {
//            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_LESS_THAN_SOLD.getErrorCode(),ApiErrorCodes.INVENTORY_LESS_THAN_SOLD.getErrorMessage());
//        }
        inventoryEntity.setQuantity(inventoryEntity.getQuantity() - request.getQuantitySold());
        inventoryRepository.save(inventoryEntity);
        log.info("Inventory Updated Created : {}", inventoryEntity);
        return entityToUpdateDto(inventoryEntity);
    }

    public List<InventoryUpdateResponse> updateInventoryInBulk(InventoryBulkUpdateReq request) {
        List<InventoryUpdateResponse> inventoryResponseList = new ArrayList<>();
        for(InventoryUpdateRequest updateRequest : request.getUpdateRequestList()) {
            log.info("Inventory Updated Request for Sales Level : {}", updateRequest.getSalesLevel());
            log.info("Inventory Updated Request for Quantity Sold : {}", updateRequest.getQuantitySold());
            InventoryEntity inventoryEntity = inventoryRepository.findByClientIdAndProductIdAndSalesLevel(updateRequest.getClientId(), updateRequest.getProductId(), updateRequest.getSalesLevel()).orElseThrow(() ->
                    new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage()));
            inventoryEntity.setSalesLevel(updateRequest.getSalesLevel());
//            if (inventoryEntity.getQuantity() == 0) {
//                throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NULL.getErrorCode(), ApiErrorCodes.INVENTORY_NULL.getErrorMessage());
//            } else if (inventoryEntity.getQuantity() < updateRequest.getQuantitySold()) {
//                throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_LESS_THAN_SOLD.getErrorCode(), ApiErrorCodes.INVENTORY_LESS_THAN_SOLD.getErrorMessage());
//            }
            inventoryEntity.setQuantity(inventoryEntity.getQuantity() - updateRequest.getQuantitySold());
            inventoryRepository.save(inventoryEntity);
            log.info("Inventory Updated for particular client and product : {}", inventoryEntity);
            inventoryResponseList.add(entityToUpdateDto(inventoryEntity));
        }
        return inventoryResponseList;
    }

    @Transactional
    public InventoryResponse createInventory(InventoryRequest request) {
        log.info("Creating Inventory: {}", request);
        Long clientId = request.getClientId();
        Long productId = request.getProductId();
        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findByClientIdAndProductId(clientId, productId);
        if(optionalInventoryEntity.isEmpty()){
            InventoryEntity inventoryEntity = dtoToEntity(request);
            inventoryRepository.save(inventoryEntity);
            log.info("Inventory Created Successfully: {}", inventoryEntity);
            return entityToDto(inventoryEntity);
        }else{
            optionalInventoryEntity.get().setQuantity(optionalInventoryEntity.get().getQuantity() + request.getQuantity());
            optionalInventoryEntity.get().setSalesLevel(request.getSalesLevel());
            inventoryRepository.save(optionalInventoryEntity.get());
            log.info("Inventory Created Successfully: {}", optionalInventoryEntity.get());
            return entityToDto(optionalInventoryEntity.get());
        }

    }

    @Transactional
    public List<InventoryResponse> createInventoryInBulk(InventoryBulkReq request) {
        List<InventoryResponse> inventoryResponseList = new ArrayList<>();
        for(InventoryRequest inventoryRequest : request.getInventoryRequestList()) {
            Long clientId = inventoryRequest.getClientId();
            Long productId = inventoryRequest.getProductId();
            Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findByClientIdAndProductId(clientId, productId);
            if(optionalInventoryEntity.isEmpty()){
                InventoryEntity inventoryEntity = dtoToEntity(inventoryRequest);
                inventoryRepository.save(inventoryEntity);
                log.info("Inventory Created Successfully: {}", inventoryEntity);
                inventoryResponseList.add(entityToDto(inventoryEntity));
            }else{
                optionalInventoryEntity.get().setQuantity(optionalInventoryEntity.get().getQuantity() + inventoryRequest.getQuantity());
                optionalInventoryEntity.get().setSalesLevel(inventoryRequest.getSalesLevel());
                inventoryRepository.save(optionalInventoryEntity.get());
                log.info("Inventory Created Successfully: {}", optionalInventoryEntity.get());
                inventoryResponseList.add(entityToDto(optionalInventoryEntity.get()));
            }
        }
        return inventoryResponseList;
    }
    public PaginatedResp<InventoryResponse> getInventory(Long productId, int page, int pageSize, String sortBy, String sortDirection) {
        log.info("Fetching Inventory: {} ", productId);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<InventoryEntity> byProductId = inventoryRepository.findByProductId(productId, pageable);
        List<InventoryResponse> collect = byProductId.getContent().stream().map(this::entityToDto).collect(Collectors.toList());
        log.info("Inventory Fetched Successfully: {} ", collect);
        return PaginatedResp.<InventoryResponse>builder().totalElements(byProductId.getTotalElements()).totalPages(byProductId.getTotalPages()).page(page).content(collect).build();
    }
    private InventoryFillResponse entityToFillUpdateDto(InventoryEntity inventoryEntity) {
        InventoryFillResponse response = new InventoryFillResponse();
        response.setProductId(inventoryEntity.getProductId());
        response.setSalesLevel(inventoryEntity.getSalesLevel());
        response.setCurrentStock(inventoryEntity.getQuantity());
        response.setMessage("Inventory Filled Successfully");
        return response;
    }
    public InventoryFillResponse fillInventory(Long id, InventoryFillRequest request) {
        log.info("Filling Inventory: {} ", id);
        InventoryEntity inventoryEntity = inventoryRepository.findById(id).orElseThrow(() ->
                new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(),ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage()));
        inventoryEntity.setSalesLevel(request.getSalesLevel());
        inventoryEntity.setQuantity(inventoryEntity.getQuantity()+request.getQuantityToFill());
        inventoryRepository.save(inventoryEntity);
        log.info("Inventory Filled Successfully: {} ", inventoryEntity);
        return entityToFillUpdateDto(inventoryEntity);
    }

    public PaginatedResp<InventoryResponse> inventoryWithProductNameWithClientFmcgResponse(Long clientId, int page, int pageSize, String sortBy, String sortDirection) {
        if(clientId == 0) {
            Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, pageSize, sort);
            Page<InventoryEntity> allInventory = inventoryRepository.findAll(pageable);
            List<InventoryResponse> collect = new ArrayList<>();
            for (InventoryEntity inventoryEntity : allInventory.getContent()) {
                InventoryResponse inventoryResponse = entityToDto(inventoryEntity);
                inventoryResponse.setProductRes(productServiceClient.getProduct(inventoryEntity.getProductId()));
                inventoryResponse.setClientFMCGResponse(externalRestService.getClient(inventoryEntity.getClientId()));
                collect.add(inventoryResponse);
            }
            log.info("Fetched All Successfully: {} ", collect);
            return PaginatedResp.<InventoryResponse>builder().totalElements(allInventory.getTotalElements()).totalPages(allInventory.getTotalPages()).page(page).content(collect).build();
        }else{
            Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, pageSize, sort);
            Page<InventoryEntity> allInventory = inventoryRepository.findByClientId(clientId, pageable);
            List<InventoryResponse> collect = new ArrayList<>();
            for (InventoryEntity inventoryEntity : allInventory.getContent()) {
                InventoryResponse inventoryResponse = entityToDto(inventoryEntity);
                inventoryResponse.setProductRes(productServiceClient.getProduct(inventoryEntity.getProductId()));
                inventoryResponse.setClientFMCGResponse(externalRestService.getClient(inventoryEntity.getClientId()));
                collect.add(inventoryResponse);
            }
            log.info("Fetched All Successfully: {} ", collect);
            return PaginatedResp.<InventoryResponse>builder().totalElements(allInventory.getTotalElements()).totalPages(allInventory.getTotalPages()).page(page).content(collect).build();

        }
    }

    public PaginatedResp<InventoryResponse> inventoryWithProductName(Long clientId, int page, int pageSize, String sortBy, String sortDirection) {
        if(clientId == 0) {
            Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, pageSize, sort);
            Page<InventoryEntity> allInventory = inventoryRepository.findAll(pageable);
            List<InventoryResponse> collect = new ArrayList<>();
            for (InventoryEntity inventoryEntity : allInventory.getContent()) {
                InventoryResponse inventoryResponse = entityToDto(inventoryEntity);
                inventoryResponse.setProductRes(productServiceClient.getProduct(inventoryEntity.getProductId()));
                collect.add(inventoryResponse);
            }
            log.info("Fetched All Successfully: {} ", collect);
            return PaginatedResp.<InventoryResponse>builder().totalElements(allInventory.getTotalElements()).totalPages(allInventory.getTotalPages()).page(page).content(collect).build();
        }else{
            List<InventoryEntity> all = inventoryRepository.findAll();
            if (all.isEmpty()) {
                throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
            }
            Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, pageSize, sort);
            Page<InventoryEntity> allInventory = inventoryRepository.findByClientId(clientId, pageable);
            List<InventoryResponse> collect = new ArrayList<>();
            for (InventoryEntity inventoryEntity : allInventory.getContent()) {
                InventoryResponse inventoryResponse = entityToDto(inventoryEntity);
                inventoryResponse.setProductRes(productServiceClient.getProduct(inventoryEntity.getProductId()));
                collect.add(inventoryResponse);
            }
            log.info("Fetched All Successfully: {} ", collect);
            return PaginatedResp.<InventoryResponse>builder().totalElements(allInventory.getTotalElements()).totalPages(allInventory.getTotalPages()).page(page).content(collect).build();

        }
    }

    public PaginatedResp<InventoryResponse> getAllInventory(int page, int pageSize, String sortBy, String sortDirection) {
        List<InventoryEntity> all = inventoryRepository.findAll();
        if(all.isEmpty()){
            throw  new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(),ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        }
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<InventoryEntity> allInventory = inventoryRepository.findAll(pageable);
        List<InventoryResponse> collect = allInventory.getContent().stream().map(this::entityToDto).collect(Collectors.toList());
        log.info("Fetched All Successfully: {} ", collect);
        return PaginatedResp.<InventoryResponse>builder().totalElements(allInventory.getTotalElements()).totalPages(allInventory.getTotalPages()).page(page).content(collect).build();
    }
    public PaginatedResp<InventoryResponse> getAllInventoryByClientFmcgId(Long clientFmcgId, int page, int pageSize, String sortBy, String sortDirection) {
        log.info("Fetching Inventory: {} ", clientFmcgId);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<InventoryEntity> inventoryByClientId = inventoryRepository.findByClientId(clientFmcgId, pageable);
        if (inventoryByClientId.isEmpty()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        }
        List<InventoryResponse> inventoryList = inventoryByClientId.getContent().stream().map(this::entityToDto).toList();
        return PaginatedResp.<InventoryResponse>builder().totalElements(inventoryByClientId.getTotalElements()).totalPages(inventoryByClientId.getTotalPages()).page(page).content(inventoryList).build();
    }

    public InventoryResponse getInventoryByClientFmcgIdAndProductId(Long clientFmcgId, Long productId) {
        log.info("Fetching inventory By Client-FMCG id and ProductId");
        Optional<InventoryEntity> byClientIdAndProductId = inventoryRepository.findByClientIdAndProductId(clientFmcgId, productId);
        if (byClientIdAndProductId.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        }
        return entityToDto(byClientIdAndProductId.get());
    }

    public PaginatedResp<InventoryResponse> getAllClientInventoryByMemberId(Long memberId,  int page, int pageSize, String sortBy, String sortDirection) {
        log.info("Fetching Inventory: {} ", memberId);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Set<Long> clientIds = externalRestService.getClientIdsByMemberId(memberId);
        Page<InventoryEntity> entityPaginatedResp = inventoryRepository.findByClientIdList(clientIds, pageable);
        List<InventoryResponse> inventoryResponseList = new ArrayList<>();
        for (InventoryEntity inventoryEntity : entityPaginatedResp.getContent()) {
            InventoryResponse inventoryResponse = entityToDto(inventoryEntity);
            inventoryResponseList.add(inventoryResponse);
        }
        return new PaginatedResp<>(entityPaginatedResp.getTotalElements(), entityPaginatedResp.getTotalPages(), page, inventoryResponseList);
    }
}
