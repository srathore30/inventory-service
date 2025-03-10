package com.inventory.inventory.dto.response;

import com.inventory.inventory.enums.SalesLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class InventoryResponse {
    private Long inventoryId;
    private Long productId;
    private Long quantity;
    private Date updateAt;
    private SalesLevel salesLevel;
    private Long clientId;
    private ClientFMCGResponse clientFMCGResponse;
    private ProductRes productRes;
}
