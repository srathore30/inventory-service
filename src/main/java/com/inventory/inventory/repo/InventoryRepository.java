package com.inventory.inventory.repo;

import com.inventory.inventory.entity.InventoryEntity;
import com.inventory.inventory.enums.SalesLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    Page<InventoryEntity> findByProductId(Long productId, Pageable pageable);

    Page<InventoryEntity> findByClientId(Long clientId, Pageable pageable);
    @Query("SELECT o FROM InventoryEntity o WHERE o.clientId IN :clientIds")
    Page<InventoryEntity> findByClientIdList(Set<Long> clientIds, Pageable pageable);

    Optional<InventoryEntity> findByClientIdAndProductId(Long clientId, Long productId);
    Optional<InventoryEntity> findByClientIdAndProductIdAndSalesLevel(Long clientId, Long productId, SalesLevel salesLevel);
}
