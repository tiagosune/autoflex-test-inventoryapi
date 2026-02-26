package com.tiagosune.inventory_api.dto.rawmaterial;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RawMaterialResponse {

    private Long id;
    private String code;
    private String name;
    private BigDecimal stockQuantity;
}
