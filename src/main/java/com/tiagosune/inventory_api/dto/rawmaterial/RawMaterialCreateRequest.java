package com.tiagosune.inventory_api.dto.rawmaterial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RawMaterialCreateRequest {

    @NotBlank
    private String code;
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal stockQuantity;
}
