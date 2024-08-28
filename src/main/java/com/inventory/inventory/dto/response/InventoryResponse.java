package com.inventory.inventory.dto.response;

import com.inventory.inventory.constant.SalesLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private Long productId;
    private SalesLevel salesLevel;
    private Long remainingStock;
    private String message;
}
