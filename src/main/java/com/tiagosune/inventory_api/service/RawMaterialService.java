package com.tiagosune.inventory_api.service;

import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialCreateRequest;
import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialResponse;
import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialUpdateRequest;
import com.tiagosune.inventory_api.entity.RawMaterial;
import com.tiagosune.inventory_api.exception.BusinessException;
import com.tiagosune.inventory_api.exception.ResourceNotFoundException;
import com.tiagosune.inventory_api.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RawMaterialService {

    private final RawMaterialRepository repository;

    public RawMaterialResponse create (RawMaterialCreateRequest request) {

        validateUniqueCode(request.getCode());
        validateName(request.getName());
        validateStock(request.getStockQuantity());

        RawMaterial rawMaterial = RawMaterial.builder()
                .code(request.getCode())
                .name(request.getName())
                .stockQuantity(request.getStockQuantity())
                .build();

        RawMaterial saved = repository.save(rawMaterial);

        return mapToResponse(saved);
    }

    public RawMaterialResponse update (Long id, RawMaterialUpdateRequest request) {
        RawMaterial rawMaterial = findEntityById(id);

        validateName(request.getName());
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

    public RawMaterialResponse findRawMaterialById(Long id) {
        RawMaterial rawMaterial = findEntityById(id);
        return mapToResponse(rawMaterial);
    }

    public List<RawMaterialResponse> findAllRawMaterials() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RawMaterial findEntityByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found"));
    }

    public List<RawMaterial> findAllEntities() {
        return repository.findAll();
    }

    //private methods

    private RawMaterial findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found"));
    }

    private void validateStock(BigDecimal stock) {
        if (stock.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Stock quantity cannot be negative");
        }
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

    private RawMaterialResponse mapToResponse(RawMaterial rawMaterial) {
        return RawMaterialResponse.builder()
                .id(rawMaterial.getId())
                .code(rawMaterial.getCode())
                .name(rawMaterial.getName())
                .stockQuantity(rawMaterial.getStockQuantity())
                .build();
    }
}
