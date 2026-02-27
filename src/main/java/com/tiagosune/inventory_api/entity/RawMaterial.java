package com.tiagosune.inventory_api.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "raw_material")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = true)
    private String code;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private BigDecimal stockQuantity;

    @OneToMany(mappedBy = "rawMaterial")
    private List<ProductRawMaterial> products;
}
