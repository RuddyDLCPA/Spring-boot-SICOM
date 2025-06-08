package com.api.sicom.api.controller;

import com.api.sicom.api.dto.RequestArticleItemCreateDto;
import com.api.sicom.api.dto.RequestArticleItemResponseDto;
import com.api.sicom.api.service.RequestArticleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-items") // Endpoint para ítems de solicitud
public class RequestArticleItemController {

    @Autowired
    private RequestArticleItemService requestArticleItemService;

    @GetMapping
    public List<RequestArticleItemResponseDto> getAllRequestArticleItems() {
        return requestArticleItemService.getAllRequestArticleItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestArticleItemResponseDto> getRequestArticleItemById(@PathVariable Integer id) {
        return requestArticleItemService.getRequestArticleItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RequestArticleItemResponseDto> createRequestArticleItem(@RequestBody RequestArticleItemCreateDto itemDto) {
        try {
            RequestArticleItemResponseDto createdItem = requestArticleItemService.createRequestArticleItem(itemDto);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si la solicitud, artículo o unidad no existe
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestArticleItemResponseDto> updateRequestArticleItem(@PathVariable Integer id, @RequestBody RequestArticleItemCreateDto itemDto) {
        try {
            RequestArticleItemResponseDto updatedItem = requestArticleItemService.updateRequestArticleItem(id, itemDto);
            if (updatedItem != null) {
                return ResponseEntity.ok(updatedItem);
            }
            return ResponseEntity.notFound().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si la solicitud, artículo o unidad no existe
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestArticleItem(@PathVariable Integer id) {
        if (requestArticleItemService.deleteRequestArticleItem(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
