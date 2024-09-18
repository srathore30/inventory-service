package com.inventory.inventory.dto.request;

import com.inventory.inventory.enums.SalesLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class InventoryRequest {
    private Long productId;
    private Long quantity;
    private Date updateAt;
    private SalesLevel salesLevel;
    private Long warehouseStock;
    private Long retailStock;
    private Long stockistStock;
    private String unitOfMeasure;
}
