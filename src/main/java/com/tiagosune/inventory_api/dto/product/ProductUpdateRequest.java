package com.tiagosune.inventory_api.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private BigDecimal price;
}
