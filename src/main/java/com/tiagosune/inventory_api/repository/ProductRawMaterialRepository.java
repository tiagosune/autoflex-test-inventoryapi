package com.tiagosune.inventory_api.repository;

import com.tiagosune.inventory_api.entity.ProductRawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRawMaterialRepository extends JpaRepository<ProductRawMaterial, Long> {
}
