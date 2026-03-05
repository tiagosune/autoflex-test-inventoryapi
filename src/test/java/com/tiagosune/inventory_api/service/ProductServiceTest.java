package com.tiagosune.inventory_api.service;

import com.tiagosune.inventory_api.dto.product.ProductRawMaterialRequest;
import com.tiagosune.inventory_api.dto.product.ProductRequest;
import com.tiagosune.inventory_api.dto.product.ProductResponse;
import com.tiagosune.inventory_api.entity.Product;
import com.tiagosune.inventory_api.entity.RawMaterial;
import com.tiagosune.inventory_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    RawMaterialService rawMaterialService;
    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductService service;

    @Test
    void shouldCreateProductSuccessfully() {

        RawMaterial rawMaterial = RawMaterial.builder()
                .id(1L)
                .code("RM01")
                .name("Gold")
                .stockQuantity(new BigDecimal("100"))
                .build();

        ProductRawMaterialRequest prmRequest = new ProductRawMaterialRequest();
        prmRequest.setRawMaterialId(rawMaterial.getId());
        prmRequest.setRequiredQuantity(new BigDecimal("20"));

        ProductRequest request = new ProductRequest();
        request.setCode("P01");
        request.setName("Ring");
        request.setPrice(new BigDecimal("1000"));
        request.setRawMaterials(List.of(prmRequest));

        when(repository.existsByCode(request.getCode())).thenReturn(false);
        when(rawMaterialService.findEntityByIdOrThrow(rawMaterial.getId())).thenReturn(rawMaterial);

        ProductResponse response = service.createProduct(request);

        assertNotNull(response);
        assertEquals(request.getCode(), response.getCode());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(1, response.getRawMaterials().size());
        assertEquals(new BigDecimal("20"), response.getRawMaterials().getFirst().getRequiredQuantity());

        verify(repository).save(any(Product.class));
        verify(rawMaterialService).findEntityByIdOrThrow(rawMaterial.getId());
        verify(repository).existsByCode(request.getCode());
    }

}