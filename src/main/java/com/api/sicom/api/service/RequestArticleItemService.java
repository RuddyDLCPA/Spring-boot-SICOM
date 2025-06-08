package com.api.sicom.api.service;

import com.api.sicom.api.model.RequestArticleItem;
import com.api.sicom.api.model.RequestArticles;
import com.api.sicom.api.model.Article;
import com.api.sicom.api.model.Unit;
import com.api.sicom.api.repository.RequestArticleItemRepository;
import com.api.sicom.api.repository.RequestArticlesRepository;
import com.api.sicom.api.repository.ArticleRepository;
import com.api.sicom.api.repository.UnitRepository;
import com.api.sicom.api.dto.RequestArticleItemCreateDto;
import com.api.sicom.api.dto.RequestArticleItemResponseDto;
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
public class RequestArticleItemService {

    @Autowired
    private RequestArticleItemRepository requestArticleItemRepository;
    @Autowired
    private RequestArticlesRepository requestArticlesRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UnitRepository unitRepository;

    // Convertir entidad a DTO de respuesta
    public RequestArticleItemResponseDto convertToDto(RequestArticleItem item) {
        if (item == null) return null;

        ArticleResponseDto articleDto = null;
        if (item.getArticle() != null) {
            articleDto = new ArticleResponseDto(
                    item.getArticle().getId(),
                    item.getArticle().getDescripcion(),
                    new BrandDto(item.getArticle().getBrand().getId(), item.getArticle().getBrand().getDescripcion(), item.getArticle().getBrand().getIsActive()),
                    new UnitDto(item.getArticle().getUnit().getId(), item.getArticle().getUnit().getDescripcion(), item.getArticle().getUnit().getIsActive()),
                    item.getArticle().getExistencia(),
                    item.getArticle().getIsActive()
            );
        }

        UnitDto unitDto = null;
        if (item.getUnit() != null) {
            unitDto = new UnitDto(item.getUnit().getId(), item.getUnit().getDescripcion(), item.getUnit().getIsActive());
        }

        return new RequestArticleItemResponseDto(
                item.getId(),
                articleDto,
                item.getCantidad(),
                unitDto
        );
    }

    public List<RequestArticleItemResponseDto> getAllRequestArticleItems() {
        return requestArticleItemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<RequestArticleItemResponseDto> getRequestArticleItemById(Integer id) {
        return requestArticleItemRepository.findById(id)
                .map(this::convertToDto);
    }

    public RequestArticleItemResponseDto createRequestArticleItem(RequestArticleItemCreateDto itemDto) {
        RequestArticleItem item = new RequestArticleItem();
        item.setCantidad(itemDto.getCantidad());

        // Buscar solicitud
        RequestArticles requestArticles = requestArticlesRepository.findById(itemDto.getSolicitudId())
                .orElseThrow(() -> new EntityNotFoundException("Solicitud con ID " + itemDto.getSolicitudId() + " no encontrada"));
        item.setRequestArticle(requestArticles);

        // Buscar artículo
        Article article = articleRepository.findById(itemDto.getArticuloId())
                .orElseThrow(() -> new EntityNotFoundException("Artículo con ID " + itemDto.getArticuloId() + " no encontrado"));
        item.setArticle(article);

        // Buscar unidad de medida
        Unit unit = unitRepository.findById(itemDto.getUnidadMedidaId())
                .orElseThrow(() -> new EntityNotFoundException("Unidad de Medida con ID " + itemDto.getUnidadMedidaId() + " no encontrada"));
        item.setUnit(unit);

        RequestArticleItem createdItem = requestArticleItemRepository.save(item);
        return convertToDto(createdItem);
    }

    public RequestArticleItemResponseDto updateRequestArticleItem(Integer id, RequestArticleItemCreateDto itemDto) {
        Optional<RequestArticleItem> optionalItem = requestArticleItemRepository.findById(id);
        if (optionalItem.isPresent()) {
            RequestArticleItem existingItem = optionalItem.get();
            existingItem.setCantidad(itemDto.getCantidad());

            // Actualizar solicitud (opcional, si se permite reasignar ítems)
            if (itemDto.getSolicitudId() != null && !itemDto.getSolicitudId().equals(existingItem.getRequestArticle().getId())) {
                RequestArticles requestArticles = requestArticlesRepository.findById(itemDto.getSolicitudId())
                        .orElseThrow(() -> new EntityNotFoundException("Solicitud con ID " + itemDto.getSolicitudId() + " no encontrada"));
                existingItem.setRequestArticle(requestArticles);
            }

            // Actualizar artículo
            if (itemDto.getArticuloId() != null && !itemDto.getArticuloId().equals(existingItem.getArticle().getId())) {
                Article article = articleRepository.findById(itemDto.getArticuloId())
                        .orElseThrow(() -> new EntityNotFoundException("Artículo con ID " + itemDto.getArticuloId() + " no encontrado"));
                existingItem.setArticle(article);
            }

            // Actualizar unidad de medida
            if (itemDto.getUnidadMedidaId() != null && !itemDto.getUnidadMedidaId().equals(existingItem.getUnit().getId())) {
                Unit unit = unitRepository.findById(itemDto.getUnidadMedidaId())
                        .orElseThrow(() -> new EntityNotFoundException("Unidad de Medida con ID " + itemDto.getUnidadMedidaId() + " no encontrada"));
                existingItem.setUnit(unit);
            }

            RequestArticleItem updatedItem = requestArticleItemRepository.save(existingItem);
            return convertToDto(updatedItem);
        }
        return null;
    }

    public boolean deleteRequestArticleItem(Integer id) {
        if (requestArticleItemRepository.existsById(id)) {
            requestArticleItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
