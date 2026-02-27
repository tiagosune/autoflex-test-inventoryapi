package com.tiagosune.inventory_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    //private methods

    private Product findEntityById (Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private void validateUniqueCode (String code) {
        if (repository.existsByCode(code)) {
            throw new BusinessException("Code already exists");
        }
    }

    private void validateName (String name) {
        if (name.isBlank()) {
            throw new BusinessException("Name cannot be blank");
        }
    }

    private void validatePrice (BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Price cannot be negative");
        }
    }

    private void validateRawMaterials (List<ProductRawMaterialRequest> rawMaterial) {
        if (rawMaterial == null || rawMaterial.isEmpty()) {
            throw new BusinessException("Raw materials cannot be empty");
        }
    }

    private void validateDuplicatedRawMaterials (List<ProductRawMaterialRequest> rawMaterialIds) {
        List<Long> ids = rawMaterialIds.stream()
                .map(ProductRawMaterialRequest::getRawMaterialId)
                .toList();

        Set<Long> uniqueIds = new HashSet<>(ids);

        if (uniqueIds.size() != ids.size()) {
            throw new BusinessException("Raw materials must be unique");
        }
    }
}
