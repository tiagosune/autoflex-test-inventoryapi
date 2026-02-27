package com.tiagosune.inventory_api.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotBlank
    private BigDecimal price;

    @NotBlank
    private Long rawMaterialId;

}
