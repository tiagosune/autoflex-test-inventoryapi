package com.tiagosune.inventory_api.repository;

import com.tiagosune.inventory_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long>{


    boolean existsByCode(String code);
}
