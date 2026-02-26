package com.tiagosune.inventory_api.controller;

import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialCreateRequest;
import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialResponse;
import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialUpdateRequest;
import com.tiagosune.inventory_api.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @PostMapping
    public ResponseEntity<RawMaterialResponse> createRawMaterial (@Valid @RequestBody RawMaterialCreateRequest request) {

        RawMaterialResponse response = rawMaterialService.create(request);

        return ResponseEntity.created(URI.create("/api/raw-materials/" + response.getId())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> updateRawMaterial (@PathVariable Long id, @Valid @RequestBody RawMaterialUpdateRequest request) {
        return ResponseEntity.ok(rawMaterialService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial (@PathVariable Long id) {
        rawMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> getRawMaterialById (@PathVariable Long id) {
        RawMaterialResponse response = rawMaterialService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RawMaterialResponse>> getAllRawMaterials () {
        return ResponseEntity.ok(rawMaterialService.findAll());
    }

}
