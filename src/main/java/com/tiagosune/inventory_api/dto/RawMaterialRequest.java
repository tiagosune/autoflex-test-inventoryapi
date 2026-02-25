package com.tiagosune.inventory_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RawMaterialRequest {

    private String code;
    private String name;
    private String description;
    private BigDecimal stockQuantity;
}
