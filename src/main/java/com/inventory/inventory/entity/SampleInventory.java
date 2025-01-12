package com.inventory.inventory.entity;

import com.inventory.inventory.constant.Status;
import com.inventory.inventory.enums.SalesLevel;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class SampleInventory extends BaseEntity{
    Long productId;
    Integer sampleQuantity;
    Long memberId;
    Status status;
}
