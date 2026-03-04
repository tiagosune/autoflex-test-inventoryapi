package com.tiagosune.inventory_api.service;

import com.tiagosune.inventory_api.dto.production.ProductProductionResponse;
import com.tiagosune.inventory_api.dto.production.ProductionPlanResponse;
import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialCreateRequest;
import com.tiagosune.inventory_api.entity.Product;
import com.tiagosune.inventory_api.entity.ProductRawMaterial;
import com.tiagosune.inventory_api.entity.RawMaterial;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionServiceTest {

    @Mock
    RawMaterialService rawMaterialService;

    @Mock
    ProductService productService;

    @InjectMocks
    ProductionService productionService;

    @Test
    void shouldGenerateProductionPlanWithSufficientStock() {
        RawMaterial rawMaterial = RawMaterial.builder()
                .id(1L)
                .code("RM01")
                .name("Gold")
                .stockQuantity(new BigDecimal("100"))
                .build();

        Product product = Product.builder()
                .id(1L)
                .code("P01")
                .name("Chair")
                .price(new BigDecimal("50"))
                .build();

        ProductRawMaterial prm = ProductRawMaterial.builder()
                .id(1L)
                .product(product)
                .rawMaterial(rawMaterial)
                .requiredQuantity(new BigDecimal("2"))
                .build();

        product.setRawMaterials(List.of(prm));

        when(productService.findAllEntitiesOrderedByPriceDesc()).thenReturn(List.of(product));
        when(rawMaterialService.findAllEntities()).thenReturn(List.of(rawMaterial));

        ProductionPlanResponse response = productionService.generateProductionPlan();

        assertNotNull(response);
        assertEquals(1, response.getProducts().size());
        assertEquals(new BigDecimal("2500"), response.getTotalValue());

        ProductProductionResponse produced = response.getProducts().getFirst();

        assertEquals(new BigDecimal("50"), produced.getQuantityProduced());
        assertEquals(new BigDecimal("2500"), produced.getTotalValue());
    }
}