package com.inventory.inventory.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryBulkUpdateReq {
    List<InventoryUpdateRequest> updateRequestList;
}
