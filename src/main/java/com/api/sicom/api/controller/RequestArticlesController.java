package com.api.sicom.api.controller;

import com.api.sicom.api.dto.RequestArticlesCreateDto;
import com.api.sicom.api.dto.RequestArticlesResponseDto;
import com.api.sicom.api.service.RequestArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes") // Endpoint cambiado a /api/solicitudes
public class RequestArticlesController {

    @Autowired
    private RequestArticlesService requestArticlesService;

    @GetMapping
    public List<RequestArticlesResponseDto> getAllRequestArticles() {
        return requestArticlesService.getAllRequestArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestArticlesResponseDto> getRequestArticlesById(@PathVariable Integer id) {
        return requestArticlesService.getRequestArticlesById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RequestArticlesResponseDto> createRequestArticles(@RequestBody RequestArticlesCreateDto requestArticlesDto) {
        try {
            RequestArticlesResponseDto createdRequest = requestArticlesService.createRequestArticles(requestArticlesDto);
            return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el empleado no existe
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el ID del empleado es nulo
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestArticlesResponseDto> updateRequestArticles(@PathVariable Integer id, @RequestBody RequestArticlesCreateDto requestArticlesDto) {
        try {
            RequestArticlesResponseDto updatedRequest = requestArticlesService.updateRequestArticles(id, requestArticlesDto);
            if (updatedRequest != null) {
                return ResponseEntity.ok(updatedRequest);
            }
            return ResponseEntity.notFound().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el empleado no existe
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el ID del empleado es nulo
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestArticles(@PathVariable Integer id) {
        if (requestArticlesService.deleteRequestArticles(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
