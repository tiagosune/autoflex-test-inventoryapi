package com.tiagosune.inventory_api.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRawMaterialResponse {

    private Long rawMaterialId;
    private String rawMaterialName;
    private BigDecimal requiredQuantity;
}
