package com.tiagosune.inventory_api.service;

import com.tiagosune.inventory_api.dto.product.*;
import com.tiagosune.inventory_api.entity.Product;
import com.tiagosune.inventory_api.entity.ProductRawMaterial;
import com.tiagosune.inventory_api.entity.RawMaterial;
import com.tiagosune.inventory_api.exception.BusinessException;
import com.tiagosune.inventory_api.exception.ResourceNotFoundException;
import com.tiagosune.inventory_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final RawMaterialService rawMaterialService;

    public ProductResponse createProduct(ProductRequest request) {

        validateRawMaterials(request.getRawMaterials());
        validateDuplicatedRawMaterials(request.getRawMaterials());
        validateUniqueCode(request.getCode());
        validateName(request.getName());
        validatePrice(request.getPrice());

        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .price(request.getPrice())
                .build();

        List<ProductRawMaterial> compositions = new ArrayList<>();

        for (ProductRawMaterialRequest item : request.getRawMaterials()) {
            RawMaterial rawMaterial = rawMaterialService.findEntityByIdOrThrow(item.getRawMaterialId());

            validateRequiredQuantity(item);

            ProductRawMaterial prm = ProductRawMaterial.builder()
                    .product(product)
                    .rawMaterial(rawMaterial)
                    .requiredQuantity(item.getRequiredQuantity())
                    .build();

            compositions.add(prm);
        }

        product.setRawMaterials(compositions);

        repository.save(product);

        return mapToResponse(product);
    }

    public ProductResponse updateProductBasicData(Long id, ProductUpdateRequest request) {
        Product product = findEntityById(id);

        validateName(request.getName());
        validatePrice(request.getPrice());

        product.setName(request.getName());
        product.setPrice(request.getPrice());

        repository.save(product);

        return mapToResponse(product);
    }

    public ProductResponse addRawMaterials(Long productId, ProductRawMaterialRequest request) {
        Product product = findEntityById(productId);
        RawMaterial rawMaterialEntity = rawMaterialService.findEntityByIdOrThrow(request.getRawMaterialId());

        if (product.getRawMaterials().stream().anyMatch(prm -> prm
                .getRawMaterial()
                .getId()
                .equals(rawMaterialEntity.getId()))) {
            throw new BusinessException("Raw material already exists in the composition");
        }

        validateRequiredQuantity(request);

        ProductRawMaterial prm = ProductRawMaterial.builder()
                .product(product)
                .rawMaterial(rawMaterialEntity)
                .requiredQuantity(request.getRequiredQuantity())
                .build();

        product.getRawMaterials().add(prm);
        repository.save(product);

        return mapToResponse(product);
    }

    public void deleteProduct(Long id) {
        Product product = findEntityById(id);
        repository.delete(product);
    }

    public ProductResponse findProductById(Long id) {
        Product product = findEntityById(id);

        return mapToResponse(product);
    }

    public List<ProductResponse> findAllProducts() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    //private methods

    private Product findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private void validateUniqueCode(String code) {
        if (repository.existsByCode(code)) {
            throw new BusinessException("Code already exists");
        }
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new BusinessException("Name cannot be blank");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Price cannot be negative");
        }
    }

    private void validateRawMaterials(List<ProductRawMaterialRequest> rawMaterial) {
        if (rawMaterial == null || rawMaterial.isEmpty()) {
            throw new BusinessException("Raw materials cannot be empty");
        }
    }

    private ProductResponse mapToResponse(Product product) {

        List<ProductRawMaterialResponse> compositions =
                product.getRawMaterials()
                        .stream()
                        .map(prm -> ProductRawMaterialResponse.builder()
                                .rawMaterialId(prm.getRawMaterial().getId())
                                .rawMaterialName(prm.getRawMaterial().getName())
                                .requiredQuantity(prm.getRequiredQuantity())
                                .build()
                        )
                        .toList();

        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice())
                .rawMaterials(compositions)
                .build();
    }

    private void validateDuplicatedRawMaterials(List<ProductRawMaterialRequest> rawMaterialIds) {
        List<Long> ids = rawMaterialIds.stream()
                .map(ProductRawMaterialRequest::getRawMaterialId)
                .toList();

        Set<Long> uniqueIds = new HashSet<>(ids);

        if (uniqueIds.size() != ids.size()) {
            throw new BusinessException("Raw materials must be unique");
        }
    }

    private void validateRequiredQuantity (ProductRawMaterialRequest request){
        if (request.getRequiredQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Required quantity must be greater than zero");
        }
    }
}
