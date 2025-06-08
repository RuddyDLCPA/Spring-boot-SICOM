package com.api.sicom.api.controller;

import com.api.sicom.api.dto.ArticleCreateDto; // Usar DTO de creación
import com.api.sicom.api.dto.ArticleResponseDto; // Usar DTO de respuesta
import com.api.sicom.api.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/articulos") // Endpoint cambiado a /api/articulos
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<ArticleResponseDto> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDto> getArticleById(@PathVariable Integer id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDto> createArticle(@RequestBody ArticleCreateDto articleDto) {
        try {
            ArticleResponseDto createdArticle = articleService.createArticle(articleDto);
            return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si marca o unidad de medida no existe
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponseDto> updateArticle(@PathVariable Integer id, @RequestBody ArticleCreateDto articleDto) {
        try {
            ArticleResponseDto updatedArticle = articleService.updateArticle(id, articleDto);
            if (updatedArticle != null) {
                return ResponseEntity.ok(updatedArticle);
            }
            return ResponseEntity.notFound().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si marca o unidad de medida no existe
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer id) {
        if (articleService.deleteArticle(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
