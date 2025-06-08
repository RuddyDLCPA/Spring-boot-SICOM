package com.api.sicom.api.service;

import com.api.sicom.api.model.Article;
import com.api.sicom.api.model.Brand; // Necesario para buscar marca
import com.api.sicom.api.model.Unit;   // Necesario para buscar unidad
import com.api.sicom.api.repository.ArticleRepository;
import com.api.sicom.api.repository.BrandRepository; // Necesario
import com.api.sicom.api.repository.UnitRepository;   // Necesario
import com.api.sicom.api.dto.ArticleCreateDto; // Importar DTOs
import com.api.sicom.api.dto.ArticleResponseDto;
import com.api.sicom.api.dto.BrandDto;
import com.api.sicom.api.dto.UnitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private UnitRepository unitRepository;

    // Convertir entidad a DTO de respuesta
    private ArticleResponseDto convertToDto(Article article) {
        if (article == null) return null;
        BrandDto brandDto = null;
        if (article.getBrand() != null) {
            brandDto = new BrandDto(article.getBrand().getId(), article.getBrand().getDescripcion(), article.getBrand().getIsActive());
        }
        UnitDto unitDto = null;
        if (article.getUnit() != null) {
            unitDto = new UnitDto(article.getUnit().getId(), article.getUnit().getDescripcion(), article.getUnit().getIsActive());
        }
        return new ArticleResponseDto(
                article.getId(),
                article.getDescripcion(),
                brandDto,
                unitDto,
                article.getExistencia(),
                article.getIsActive()
        );
    }

    public List<ArticleResponseDto> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ArticleResponseDto> getArticleById(Integer id) {
        return articleRepository.findById(id)
                .map(this::convertToDto);
    }

    public ArticleResponseDto createArticle(ArticleCreateDto articleDto) {
        Article article = new Article();
        article.setDescripcion(articleDto.getDescripcion());
        article.setExistencia(articleDto.getExistencia() != null ? articleDto.getExistencia() : 0);
        article.setIsActive(articleDto.getIsActive() != null ? articleDto.getIsActive() : true);

        // Buscar marca
        Brand brand = brandRepository.findById(articleDto.getMarcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + articleDto.getMarcaId() + " no encontrada"));
        article.setBrand(brand);

        // Buscar unidad de medida
        Unit unit = unitRepository.findById(articleDto.getUnidadMedidaId())
                .orElseThrow(() -> new EntityNotFoundException("Unidad de Medida con ID " + articleDto.getUnidadMedidaId() + " no encontrada"));
        article.setUnit(unit);

        Article createdArticle = articleRepository.save(article);
        return convertToDto(createdArticle);
    }

    public ArticleResponseDto updateArticle(Integer id, ArticleCreateDto articleDto) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article existingArticle = optionalArticle.get();
            existingArticle.setDescripcion(articleDto.getDescripcion());
            existingArticle.setExistencia(articleDto.getExistencia());
            existingArticle.setIsActive(articleDto.getIsActive());

            // Actualizar marca si se proporciona un nuevo ID
            if (articleDto.getMarcaId() != null && !articleDto.getMarcaId().equals(existingArticle.getBrand().getId())) {
                Brand brand = brandRepository.findById(articleDto.getMarcaId())
                        .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + articleDto.getMarcaId() + " no encontrada"));
                existingArticle.setBrand(brand);
            }

            // Actualizar unidad de medida si se proporciona un nuevo ID
            if (articleDto.getUnidadMedidaId() != null && !articleDto.getUnidadMedidaId().equals(existingArticle.getUnit().getId())) {
                Unit unit = unitRepository.findById(articleDto.getUnidadMedidaId())
                        .orElseThrow(() -> new EntityNotFoundException("Unidad de Medida con ID " + articleDto.getUnidadMedidaId() + " no encontrada"));
                existingArticle.setUnit(unit);
            }

            Article updatedArticle = articleRepository.save(existingArticle);
            return convertToDto(updatedArticle);
        }
        return null;
    }

    public boolean deleteArticle(Integer id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
