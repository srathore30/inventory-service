package com.inventory.inventory.dto.request;

import com.inventory.inventory.constant.SalesLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequest {
    private Long productId;
    private SalesLevel salesLevel;
    private Long quantitySold;
}
