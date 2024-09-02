package com.inventory.inventory.dto.request;

import com.inventory.inventory.enums.SalesLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateRequest {
    private Long productId;
    private SalesLevel salesLevel;
    private Long quantitySold;
}
