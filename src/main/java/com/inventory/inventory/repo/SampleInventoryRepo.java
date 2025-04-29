package com.inventory.inventory.repo;

import com.inventory.inventory.entity.SampleInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SampleInventoryRepo extends JpaRepository<SampleInventory, Long> {
    Optional<SampleInventory> findByProductIdAndMemberId(Long productId, Long memberId);
    Page<SampleInventory> findByMemberId(Long memberId, Pageable pageable);
}
