package com.inventory.inventory.entity;

import com.inventory.inventory.enums.SalesLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class InventoryEntity extends BaseEntity {
    private Long productId;
    private Long quantity;
    private Date updateAt;
    private SalesLevel salesLevel;
}