package com.api.sicom.api.controller;

import com.api.sicom.api.dto.PurchaseOrderItemCreateDto;
import com.api.sicom.api.dto.PurchaseOrderItemResponseDto;
import com.api.sicom.api.service.PurchaseOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/ordenescompra-items") // Endpoint para ítems de orden de compra
public class PurchaseOrderItemController {

    @Autowired
    private PurchaseOrderItemService purchaseOrderItemService;

    @GetMapping
    public List<PurchaseOrderItemResponseDto> getAllPurchaseOrderItems() {
        return purchaseOrderItemService.getAllPurchaseOrderItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderItemResponseDto> getPurchaseOrderItemById(@PathVariable Integer id) {
        return purchaseOrderItemService.getPurchaseOrderItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderItemResponseDto> createPurchaseOrderItem(@RequestBody PurchaseOrderItemCreateDto itemDto) {
        try {
            PurchaseOrderItemResponseDto createdItem = purchaseOrderItemService.createPurchaseOrderItem(itemDto);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si la orden, artículo, unidad o marca no existe
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderItemResponseDto> updatePurchaseOrderItem(@PathVariable Integer id, @RequestBody PurchaseOrderItemCreateDto itemDto) {
        try {
            PurchaseOrderItemResponseDto updatedItem = purchaseOrderItemService.updatePurchaseOrderItem(id, itemDto);
            if (updatedItem != null) {
                return ResponseEntity.ok(updatedItem);
            }
            return ResponseEntity.notFound().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si la orden, artículo, unidad o marca no existe
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderItem(@PathVariable Integer id) {
        if (purchaseOrderItemService.deletePurchaseOrderItem(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
