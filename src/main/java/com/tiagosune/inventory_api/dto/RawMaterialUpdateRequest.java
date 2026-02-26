package com.tiagosune.inventory_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RawMaterialUpdateRequest {

    private String name;
    private BigDecimal stockQuantity;

}
