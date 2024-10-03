package com.inventory.inventory.dto.request;

import com.inventory.inventory.enums.SalesLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryFillRequest {
    private SalesLevel salesLevel;
    private Long quantityToFill;
}
