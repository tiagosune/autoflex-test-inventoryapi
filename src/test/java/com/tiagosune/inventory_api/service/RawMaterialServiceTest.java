package com.tiagosune.inventory_api.service;

import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialCreateRequest;
import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialResponse;
import com.tiagosune.inventory_api.dto.rawmaterial.RawMaterialUpdateRequest;
import com.tiagosune.inventory_api.entity.RawMaterial;
import com.tiagosune.inventory_api.exception.BusinessException;
import com.tiagosune.inventory_api.exception.ResourceNotFoundException;
import com.tiagosune.inventory_api.repository.RawMaterialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

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

    @Test
    void shouldNotCreateRawMaterialWithDuplicateCode() {
        RawMaterialCreateRequest request = new RawMaterialCreateRequest();

        request.setCode("RM01");
        request.setName("Iron");
        request.setStockQuantity(new BigDecimal("100"));

        when(repository.existsByCode(request.getCode())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.create(request));

        assertEquals("Code already exists", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void shouldNotCreateRawMaterialWithBlankName() {
        RawMaterialCreateRequest request = new RawMaterialCreateRequest();

        request.setCode("RM01");
        request.setName("");
        request.setStockQuantity(new BigDecimal("100"));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.create(request));

        assertEquals("Name cannot be blank", exception.getMessage());

        verify(repository, never()).existsByCode(any());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldNotCreateRawMaterialWithNegativeStockQuantity() {
        RawMaterialCreateRequest request = new RawMaterialCreateRequest();

        request.setCode("RM01");
        request.setName("Diamond");
        request.setStockQuantity(new BigDecimal("-100"));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.create(request));

        assertEquals("Stock quantity cannot be negative", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateRawMaterialSuccessfully() {
        RawMaterial rawMaterial = RawMaterial.builder()
                .id(1L)
                .code("RM01")
                .name("Gold")
                .stockQuantity(new BigDecimal("100"))
                .build();

        when(repository.findById(rawMaterial.getId())).thenReturn(Optional.of(rawMaterial));
        when(repository.save(any(RawMaterial.class))).thenReturn(rawMaterial);

        RawMaterialUpdateRequest request = new RawMaterialUpdateRequest();
        request.setName("Iron");
        request.setStockQuantity(new BigDecimal("200"));

        RawMaterialResponse response = service.update(1L, request);

        assertEquals("Iron", response.getName());
        assertEquals(new BigDecimal("200"), response.getStockQuantity());

        verify(repository).findById(rawMaterial.getId());
        verify(repository).save(any(RawMaterial.class));
    }

    @Test
    void shouldNotUpdateRawMaterialWithNonExistingId() {
        RawMaterial rawMaterial = RawMaterial.builder()
                .id(1L)
                .code("RM01")
                .name("Gold")
                .stockQuantity(new BigDecimal("100"))
                .build();

        when(repository.findById(rawMaterial.getId())).thenReturn(Optional.empty());

        RawMaterialUpdateRequest request = new RawMaterialUpdateRequest();
        request.setName("Iron");
        request.setStockQuantity(new BigDecimal("200"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.update(1L, request));

        assertEquals("Raw material not found", exception.getMessage());

        verify(repository).findById(1L);
        verify(repository, never()).save(any(RawMaterial.class));
    }

    @Test
    void shouldDeleteRawMaterialSuccessfully() {
        RawMaterial rawMaterial = RawMaterial.builder()
                .id(1L)
                .code("RM01")
                .name("Gold")
                .stockQuantity(new BigDecimal("100"))
                .build();
        when(repository.findById(rawMaterial.getId())).thenReturn(Optional.of(rawMaterial));
        service.delete(1L);

        verify(repository).findById(1L);
        verify(repository).delete(rawMaterial);
    }

    @Test
    void shouldNotDeleteRawMaterialWithNonExistingId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
        assertEquals("Raw material not found", exception.getMessage());
        verify(repository).findById(1L);
        verify(repository, never()).delete(any(RawMaterial.class));
    }
}