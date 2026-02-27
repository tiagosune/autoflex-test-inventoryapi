package com.tiagosune.inventory_api.controller;

import com.tiagosune.inventory_api.dto.product.*;
import com.tiagosune.inventory_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);

        return ResponseEntity.created(URI.create("/api/products/" + response.getId())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateBasicData (@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.updateProductBasicData(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById (@PathVariable Long id) {
        ProductResponse response = productService.findProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts () {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @PostMapping("/{id}/raw-materials")
    public ResponseEntity<ProductResponse> addRawMaterial (@Valid @RequestBody ProductRawMaterialRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(productService.addRawMaterials(id, request));
    }

    @PutMapping("/{id}/raw-materials/{rawMaterialId}")
    public ResponseEntity<ProductResponse> updateRequiredQuantity (@PathVariable Long id, @PathVariable Long rawMaterialId, @Valid @RequestBody UpdateRequiredQuantityRequest request) {
        return ResponseEntity.ok(productService.updateRawMaterialQuantity(id, rawMaterialId, request));
    }

    @DeleteMapping("/{id}/raw-materials/{rawMaterialId}")
    public ResponseEntity<ProductResponse> removeRawMaterial (@PathVariable Long id, @PathVariable Long rawMaterialId) {
        return ResponseEntity.ok(productService.removeRawMaterial(id, rawMaterialId));
    }

}
