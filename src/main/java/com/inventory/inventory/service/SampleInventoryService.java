package com.inventory.inventory.service;

import com.inventory.inventory.constant.ApiErrorCodes;
import com.inventory.inventory.constant.Status;
import com.inventory.inventory.dto.request.SampleInventoryRequest;
import com.inventory.inventory.dto.response.InventoryResponse;
import com.inventory.inventory.dto.response.PaginatedResp;
import com.inventory.inventory.dto.response.SampleInventoryResponse;
import com.inventory.inventory.entity.InventoryEntity;
import com.inventory.inventory.entity.SampleInventory;
import com.inventory.inventory.exception.NoSuchElementFoundException;
import com.inventory.inventory.repo.SampleInventoryRepo;
import com.inventory.inventory.utill.ExternalRestService;
import com.inventory.inventory.utill.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SampleInventoryService {
    private final SampleInventoryRepo sampleInventoryRepo;
    private final ExternalRestService externalRestService;
    private final ProductServiceClient productServiceClient;

    public SampleInventoryResponse createInventory(SampleInventoryRequest sampleInventoryRequest){
        log.info("createInventory");
        SampleInventory inventory = mapToEntity(sampleInventoryRequest);
        Optional<SampleInventory> optionalSampleInventory = sampleInventoryRepo.findByProductIdAndMemberId(sampleInventoryRequest.getProductId(), sampleInventoryRequest.getMemberId());
        if(optionalSampleInventory.isEmpty()){
            return mapToDto(sampleInventoryRepo.save(inventory));
        }
        optionalSampleInventory.get().setSampleQuantity(optionalSampleInventory.get().getSampleQuantity() + sampleInventoryRequest.getSampleQuantity());
        return mapToDto(sampleInventoryRepo.save(optionalSampleInventory.get()));
    }

    public SampleInventoryResponse getSampleInventoryById(Long id){
        log.info("getSampleInventoryById by member id: {} ", id);
        Optional<SampleInventory> optionalSampleInventory = sampleInventoryRepo.findById(id);
        if(optionalSampleInventory.isEmpty() || optionalSampleInventory.get().getStatus() == Status.InActive){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalSampleInventory.get());
    }

    public SampleInventoryResponse getInventoryByMemberAndProductId(Long memberId, Long productId){
        log.info("getInventoryByMemberAndProductId by member id: {} ", memberId + productId);
        Optional<SampleInventory> optionalSampleInventory = sampleInventoryRepo.findByProductIdAndMemberId(productId, memberId);
        if(optionalSampleInventory.isEmpty() || optionalSampleInventory.get().getStatus() == Status.InActive){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalSampleInventory.get());
    }

    public void deleteInventory(Long id){
        log.info("deleteInventory by member id: {} ", id);
        Optional<SampleInventory> optionalSampleInventory = sampleInventoryRepo.findById(id);
        if(optionalSampleInventory.isEmpty() || optionalSampleInventory.get().getStatus() == Status.InActive){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        }
        optionalSampleInventory.get().setStatus(Status.InActive);
        sampleInventoryRepo.save(optionalSampleInventory.get());
    }

    public void updateSampleInventoryQuantity(Long id, Integer quantity){
        log.info("deducting Inventory q: {} ", quantity);
        Optional<SampleInventory> optionalSampleInventory = sampleInventoryRepo.findById(id);
        if(optionalSampleInventory.isEmpty() || optionalSampleInventory.get().getStatus() == Status.InActive){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVENTORY_NOT_FOUND.getErrorMessage());
        }
        optionalSampleInventory.get().setSampleQuantity(quantity);
        sampleInventoryRepo.save(optionalSampleInventory.get());
    }

    public PaginatedResp<SampleInventoryResponse> getAllSampleMemberById(Long memberId, int page, int pageSize, String sortBy, String sortDirection) {
        log.info("Fetching all Inventory by member id: {} ", memberId);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<SampleInventory> sampleInventoryPage = sampleInventoryRepo.findByMemberId(memberId, pageable);
        List<SampleInventoryResponse> sampleInventoryResponseList = sampleInventoryPage.getContent().stream().filter(sampleInventory -> sampleInventory.getStatus() != Status.InActive).map(this::mapToDto).toList();
        return new PaginatedResp<>(sampleInventoryPage.getTotalElements(), sampleInventoryPage.getTotalPages(), page, sampleInventoryResponseList);
    }

    public SampleInventory mapToEntity(SampleInventoryRequest request) {
        SampleInventory inventoryEntity = new SampleInventory();
        inventoryEntity.setProductId(request.getProductId());
        inventoryEntity.setSampleQuantity(request.getSampleQuantity());
        inventoryEntity.setMemberId(request.getMemberId());
        inventoryEntity.setStatus(Status.Active);
        return inventoryEntity;
    }

    public SampleInventoryResponse mapToDto(SampleInventory inventoryEntity) {
        SampleInventoryResponse response = new SampleInventoryResponse();
        response.setProductId(inventoryEntity.getProductId());
        response.setSampleQuantity(inventoryEntity.getSampleQuantity());
        response.setProductRes(productServiceClient.getProduct(inventoryEntity.getProductId()));
        response.setId(inventoryEntity.getId());
        response.setMemberRes(externalRestService.getMemberById(inventoryEntity.getMemberId()));
        return response;
    }
}
