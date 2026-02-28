package com.tiagosune.inventory_api.service;

import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialCreateRequest;
import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialResponse;
import com.tiagosune.inventory_api.entity.RawMaterial;
import com.tiagosune.inventory_api.repository.RawMaterialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository repository;

    @InjectMocks
    private RawMaterialService service;

    @Test
    void shouldCreateRawMaterialSuccessfully() {
        RawMaterialCreateRequest request = new RawMaterialCreateRequest();

        request.setCode("RM01");
        request.setName("Aço");
        request.setStockQuantity(new BigDecimal("100"));

        when(repository.existsByCode(request.getCode())).thenReturn(false);

        RawMaterial saved = RawMaterial.builder()
                .id(1L)
                .code(request.getCode())
                .name(request.getName())
                .stockQuantity(request.getStockQuantity())
                .build();

        when(repository.save(any(RawMaterial.class))).thenReturn(saved);

        RawMaterialResponse response = service.create(request);
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(request.getCode(), response.getCode());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getStockQuantity(), response.getStockQuantity());

        verify(repository, times(1)).save(any(RawMaterial.class));
    }

}