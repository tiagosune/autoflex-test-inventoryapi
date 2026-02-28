package com.tiagosune.inventory_api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal price;
}
