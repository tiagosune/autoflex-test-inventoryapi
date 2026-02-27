package com.tiagosune.inventory_api.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductResponse {

    private Long id;
    private String code;
    private String name;
    private BigDecimal price;
    private List<ProductRawMaterialResponse> rawMaterials;

}
