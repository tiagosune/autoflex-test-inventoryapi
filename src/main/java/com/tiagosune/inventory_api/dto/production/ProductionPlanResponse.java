package com.tiagosune.inventory_api.dto.production;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public class ProductionPlanResponse {

    private List<ProductProductionResponse> products;
    private BigDecimal totalValue;
}
