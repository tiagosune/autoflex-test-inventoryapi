package com.tiagosune.inventory_api.repository;

import com.tiagosune.inventory_api.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {

    boolean existsByCode(String code);
}
