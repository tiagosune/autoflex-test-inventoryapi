package com.tiagosune.inventory_api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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

    }
}