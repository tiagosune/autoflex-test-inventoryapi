package com.tiagosune.inventory_api.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateRequiredQuantityRequest {

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)
    private BigDecimal requiredQuantity;
}
