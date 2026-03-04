package com.tiagosune.inventory_api.dto.production;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class ProductionPlanResponse {

    private List<ProductProductionResponse> products;
    private BigDecimal totalValue;
}
