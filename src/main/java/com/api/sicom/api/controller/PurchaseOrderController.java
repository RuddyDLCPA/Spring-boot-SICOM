package com.api.sicom.api.controller;

import com.api.sicom.api.dto.PurchaseOrderCreateDto;
import com.api.sicom.api.dto.PurchaseOrderResponseDto;
import com.api.sicom.api.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/ordenescompra") // Endpoint cambiado a /api/ordenescompra
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping
    public List<PurchaseOrderResponseDto> getAllPurchaseOrders() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponseDto> getPurchaseOrderById(@PathVariable Integer id) {
        return purchaseOrderService.getPurchaseOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderResponseDto> createPurchaseOrder(@RequestBody PurchaseOrderCreateDto purchaseOrderDto) {
        try {
            PurchaseOrderResponseDto createdOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si la solicitud no existe
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el ID de la solicitud es nulo
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponseDto> updatePurchaseOrder(@PathVariable Integer id, @RequestBody PurchaseOrderCreateDto purchaseOrderDto) {
        try {
            PurchaseOrderResponseDto updatedOrder = purchaseOrderService.updatePurchaseOrder(id, purchaseOrderDto);
            if (updatedOrder != null) {
                return ResponseEntity.ok(updatedOrder);
            }
            return ResponseEntity.notFound().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si la solicitud no existe
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el ID de la solicitud es nulo
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Integer id) {
        if (purchaseOrderService.deletePurchaseOrder(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
