package com.tiagosune.inventory_api.service;

import com.tiagosune.inventory_api.dto.production.ProductProductionResponse;
import com.tiagosune.inventory_api.dto.production.ProductionPlanResponse;
import com.tiagosune.inventory_api.entity.Product;
import com.tiagosune.inventory_api.entity.ProductRawMaterial;
import com.tiagosune.inventory_api.entity.RawMaterial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private final RawMaterialService rawMaterialService;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public ProductionPlanResponse generateProductionPlan() {

        List<Product> products = productService.findAllEntitiesOrderedByPriceDesc();
        List<RawMaterial> rawMaterials = rawMaterialService.findAllEntities();

        Map<Long, BigDecimal> stockMap = new HashMap<>();
        for (RawMaterial rm : rawMaterials) {
            stockMap.put(rm.getId(), rm.getStockQuantity());
        }

        List<ProductProductionResponse> productionList = new ArrayList<>();
        BigDecimal totalProductionValue = BigDecimal.ZERO;

        for (Product product : products) {

            BigDecimal minPossible = null;

            for (ProductRawMaterial prm : product.getRawMaterials()) {

                Long rawMaterialId = prm.getRawMaterial().getId();
                BigDecimal stockAvailable = stockMap.getOrDefault(rawMaterialId, BigDecimal.ZERO);
                BigDecimal required = prm.getRequiredQuantity();

                BigDecimal possibleUnits = stockAvailable
                        .divide(required, 0, RoundingMode.DOWN);

                if (possibleUnits.compareTo(BigDecimal.ZERO) <= 0) {
                    minPossible = BigDecimal.ZERO;
                    break;
                }

                if (minPossible == null || possibleUnits.compareTo(minPossible) < 0) {
                    minPossible = possibleUnits;
                }
            }

            if (minPossible != null && minPossible.compareTo(BigDecimal.ZERO) > 0) {

                for (ProductRawMaterial prm : product.getRawMaterials()) {

                    Long rawMaterialId = prm.getRawMaterial().getId();
                    BigDecimal required = prm.getRequiredQuantity();

                    BigDecimal usedQuantity = required.multiply(minPossible);

                    BigDecimal currentStock = stockMap.get(rawMaterialId);
                    BigDecimal newStock = currentStock.subtract(usedQuantity);

                    stockMap.put(rawMaterialId, newStock);
                }

                BigDecimal productTotalValue =
                        product.getPrice().multiply(minPossible);

                totalProductionValue =
                        totalProductionValue.add(productTotalValue);

                productionList.add(
                        ProductProductionResponse.builder()
                                .productId(product.getId())
                                .productName(product.getName())
                                .quantityProduced(minPossible)
                                .totalValue(productTotalValue)
                                .build()
                );
            }
        }

        return ProductionPlanResponse.builder()
                .products(productionList)
                .totalValue(totalProductionValue)
                .build();
    }
}
