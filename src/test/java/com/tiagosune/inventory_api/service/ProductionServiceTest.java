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
import static org.mockito.Mockito.verify;
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
        assertEquals(product.getName(), produced.getProductName());
        assertEquals(product.getId(), produced.getProductId());

        verify(productService).findAllEntitiesOrderedByPriceDesc();
        verify(rawMaterialService).findAllEntities();
    }

    @Test
    void shouldPrioritizeMoreExpensiveProductsWhenStockIsLimited() {
        RawMaterial rawMaterial = RawMaterial.builder()
                .id(1L)
                .code("RM01")
                .name("Gold")
                .stockQuantity(new BigDecimal("10"))
                .build();

        Product product = Product.builder()
                .id(1L)
                .code("P01")
                .name("Chair")
                .price(new BigDecimal("100"))
                .build();

        ProductRawMaterial prm = ProductRawMaterial.builder()
                .id(1L)
                .product(product)
                .rawMaterial(rawMaterial)
                .requiredQuantity(new BigDecimal("2"))
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .code("P02")
                .name("Table")
                .price(new BigDecimal("50"))
                .build();

        ProductRawMaterial prm2 = ProductRawMaterial.builder()
                .id(2L)
                .product(product2)
                .rawMaterial(rawMaterial)
                .requiredQuantity(new BigDecimal("2"))
                .build();

        product.setRawMaterials(List.of(prm));
        product2.setRawMaterials(List.of(prm2));

        when(productService.findAllEntitiesOrderedByPriceDesc()).thenReturn(List.of(product, product2));
        when(rawMaterialService.findAllEntities()).thenReturn(List.of(rawMaterial));

        ProductionPlanResponse response = productionService.generateProductionPlan();

        assertNotNull(response);
        assertEquals(1, response.getProducts().size());

        ProductProductionResponse produced = response.getProducts().getFirst();
        assertEquals(product.getId(), produced.getProductId());
        assertEquals(new BigDecimal("5"), produced.getQuantityProduced());

        verify(productService).findAllEntitiesOrderedByPriceDesc();
        verify(rawMaterialService).findAllEntities();
    }

    @Test
    void shouldProduceBasedOnMostLimitedRawMaterial () {
        RawMaterial rawMaterial = RawMaterial.builder()
                .id(1L)
                .code("RM01")
                .name("Gold")
                .stockQuantity(new BigDecimal("100"))
                .build();

        RawMaterial rawMaterial2 = RawMaterial.builder()
                .id(2L)
                .code("RM02")
                .name("Iron")
                .stockQuantity(new BigDecimal("50"))
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
                .requiredQuantity(new BigDecimal("10"))
                .build();

        ProductRawMaterial prm2 = ProductRawMaterial.builder()
                .id(2L)
                .product(product)
                .rawMaterial(rawMaterial2)
                .requiredQuantity(new BigDecimal("10"))
                .build();

        product.setRawMaterials(List.of(prm, prm2));

        when(productService.findAllEntitiesOrderedByPriceDesc()).thenReturn(List.of(product));
        when(rawMaterialService.findAllEntities()).thenReturn(List.of(rawMaterial, rawMaterial2));

        ProductionPlanResponse response = productionService.generateProductionPlan();

        assertNotNull(response);
        assertEquals(1, response.getProducts().size());

        ProductProductionResponse produced = response.getProducts().getFirst();
        assertEquals(product.getId(), produced.getProductId());
        assertEquals(new BigDecimal("5"), produced.getQuantityProduced());
        assertEquals(new BigDecimal("250"), produced.getTotalValue());
        assertEquals(new BigDecimal("250"), response.getTotalValue());

        verify(productService).findAllEntitiesOrderedByPriceDesc();
        verify(rawMaterialService).findAllEntities();
    }

    @Test
    void shouldSkipProductWhenInsufficientRawMaterial() {
        RawMaterial rawMaterial = RawMaterial.builder()
                .id(1L)
                .code("RM01")
                .name("Gold")
                .stockQuantity(new BigDecimal("5"))
                .build();

        Product product = Product.builder()
                .id(1L)
                .code("P01")
                .name("Ring")
                .price(new BigDecimal("50"))
                .build();

        ProductRawMaterial prm = ProductRawMaterial.builder()
                .id(1L)
                .product(product)
                .rawMaterial(rawMaterial)
                .requiredQuantity(new BigDecimal("10"))
                .build();

        product.setRawMaterials(List.of(prm));

        when(productService.findAllEntitiesOrderedByPriceDesc()).thenReturn(List.of(product));
        when(rawMaterialService.findAllEntities()).thenReturn(List.of(rawMaterial));

        ProductionPlanResponse response = productionService.generateProductionPlan();

        assertNotNull(response);
        assertTrue(response.getProducts().isEmpty());
        assertEquals(BigDecimal.ZERO, response.getTotalValue());

        verify(productService).findAllEntitiesOrderedByPriceDesc();
        verify(rawMaterialService).findAllEntities();
    }
}