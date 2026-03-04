package com.tiagosune.inventory_api.dto.production;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ProductProductionResponse {

    private Long productId;
    private String productName;
    private BigDecimal quantityProduced;
    private BigDecimal totalValue;
}
