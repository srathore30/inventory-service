package com.inventory.inventory.dto.request;

import com.inventory.inventory.enums.SalesLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SampleInventoryRequest {
    private Long productId;
    private Integer sampleQuantity;
    private Long memberId;
}