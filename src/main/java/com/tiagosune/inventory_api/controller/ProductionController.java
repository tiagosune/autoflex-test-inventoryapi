package com.tiagosune.inventory_api.controller;

import com.tiagosune.inventory_api.dto.production.ProductionPlanResponse;
import com.tiagosune.inventory_api.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService productionService;

    @GetMapping("/plan")
    public ResponseEntity<ProductionPlanResponse> generateProductionPlan() {
        return ResponseEntity.ok(productionService.generateProductionPlan());
    }
}
