package com.inventory.inventory.dto.response;

import com.inventory.inventory.enums.SalesLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SampleInventoryResponse {
    private Long id;
    private Long productId;
    private Integer sampleQuantity;
    private MemberResponse memberRes;
    private ProductRes productRes;
}
