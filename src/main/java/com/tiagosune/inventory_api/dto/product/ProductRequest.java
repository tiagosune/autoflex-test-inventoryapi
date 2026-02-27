package com.tiagosune.inventory_api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal price;

    @NotEmpty
    private List<ProductRawMaterialRequest> rawMaterials;

}
