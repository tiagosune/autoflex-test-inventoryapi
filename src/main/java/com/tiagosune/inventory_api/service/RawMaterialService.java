package com.tiagosune.inventory_api.service;

import com.tiagosune.inventory_api.dto.RawMaterialRequest;
import com.tiagosune.inventory_api.dto.RawMaterialResponse;
import com.tiagosune.inventory_api.entity.RawMaterial;
import com.tiagosune.inventory_api.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository repository;

    public RawMaterialResponse create (RawMaterialRequest request) {

        validateStock(request.getStockQuantity());
        validateUniqueCode(request.getCode());

        RawMaterial rawMaterial = RawMaterial.builder()
                .code(request.getCode())
                .name(request.getName())
                .stockQuantity(request.getStockQuantity())
                .build();

        repository.save(rawMaterial);

        return mapToResponse(rawMaterial);
    }

    public RawMaterialResponse update (Long id, RawMaterialRequest request) {
        RawMaterial rawMaterial = findEntityById(id);
        validateStock(request.getStockQuantity());

        rawMaterial.setName(request.getName());
        rawMaterial.setStockQuantity(request.getStockQuantity());
        repository.save(rawMaterial);

        return mapToResponse(rawMaterial);
    }

    public void delete (Long id) {
        RawMaterial rawMaterial = findEntityById(id);
        repository.delete(rawMaterial);
    }

    public RawMaterialResponse findById (Long id) {
        RawMaterial rawMaterial = findEntityById(id);
        return mapToResponse(rawMaterial);
    }

    public RawMaterialResponse findByCode (String code) {
        RawMaterial rawMaterial = repository.findByCode(code);
        return mapToResponse(rawMaterial);
    }

    public List<RawMaterialResponse> findAll () {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    //private methods

    private RawMaterial findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Raw material not found"));
    }

    private void validateStock(BigDecimal stock) {
        if (stock.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
    }

    private void validateUniqueCode (String code) {
        if (repository.existsByCode(code)) {
            throw new IllegalArgumentException("Code already exists");
        }
    }

    private RawMaterialResponse mapToResponse(RawMaterial rawMaterial) {
        return RawMaterialResponse.builder()
                .id(rawMaterial.getId())
                .code(rawMaterial.getCode())
                .name(rawMaterial.getName())
                .stockQuantity(rawMaterial.getStockQuantity())
                .build();
    }

}
