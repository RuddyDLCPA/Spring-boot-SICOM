package com.api.sicom.api.service;

import com.api.sicom.api.model.PurchaseOrderItem;
import com.api.sicom.api.model.PurchaseOrder;
import com.api.sicom.api.model.Article;
import com.api.sicom.api.model.Unit;
import com.api.sicom.api.model.Brand;
import com.api.sicom.api.repository.PurchaseOrderItemRepository;
import com.api.sicom.api.repository.PurchaseOrderRepository;
import com.api.sicom.api.repository.ArticleRepository;
import com.api.sicom.api.repository.UnitRepository;
import com.api.sicom.api.repository.BrandRepository;
import com.api.sicom.api.dto.PurchaseOrderItemCreateDto;
import com.api.sicom.api.dto.PurchaseOrderItemResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderItemService {

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private BrandRepository brandRepository;

    // Convertir entidad a DTO de respuesta (con descripciones en lugar de IDs)
    public PurchaseOrderItemResponseDto convertToDto(PurchaseOrderItem item) {
        if (item == null) return null;

        String articleDescription = (item.getArticle() != null) ? item.getArticle().getDescripcion() : null;
        String unitDescription = (item.getUnit() != null) ? item.getUnit().getDescripcion() : null;
        String brandDescription = (item.getBrand() != null) ? item.getBrand().getDescripcion() : null;

        return new PurchaseOrderItemResponseDto(
                item.getId(),
                articleDescription,
                item.getCantidad(),
                unitDescription,
                brandDescription,
                item.getCostoUnitario()
        );
    }

    public List<PurchaseOrderItemResponseDto> getAllPurchaseOrderItems() {
        return purchaseOrderItemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PurchaseOrderItemResponseDto> getPurchaseOrderItemById(Integer id) {
        return purchaseOrderItemRepository.findById(id)
                .map(this::convertToDto);
    }

    public PurchaseOrderItemResponseDto createPurchaseOrderItem(PurchaseOrderItemCreateDto itemDto) {
        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setCantidad(itemDto.getCantidad());
        item.setCostoUnitario(itemDto.getCostoUnitario());

        // Buscar orden de compra
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(itemDto.getOrdenCompraId())
                .orElseThrow(() -> new EntityNotFoundException("Orden de Compra con ID " + itemDto.getOrdenCompraId() + " no encontrada"));
        item.setPurchaseOrder(purchaseOrder);

        // Buscar artículo
        Article article = articleRepository.findById(itemDto.getArticuloId())
                .orElseThrow(() -> new EntityNotFoundException("Artículo con ID " + itemDto.getArticuloId() + " no encontrado"));
        item.setArticle(article);

        // Buscar unidad de medida
        Unit unit = unitRepository.findById(itemDto.getUnidadMedidaId())
                .orElseThrow(() -> new EntityNotFoundException("Unidad de Medida con ID " + itemDto.getUnidadMedidaId() + " no encontrada"));
        item.setUnit(unit);

        // Buscar marca
        Brand brand = brandRepository.findById(itemDto.getMarcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + itemDto.getMarcaId() + " no encontrada"));
        item.setBrand(brand);

        PurchaseOrderItem createdItem = purchaseOrderItemRepository.save(item);
        return convertToDto(createdItem);
    }

    public PurchaseOrderItemResponseDto updatePurchaseOrderItem(Integer id, PurchaseOrderItemCreateDto itemDto) {
        Optional<PurchaseOrderItem> optionalItem = purchaseOrderItemRepository.findById(id);
        if (optionalItem.isPresent()) {
            PurchaseOrderItem existingItem = optionalItem.get();
            existingItem.setCantidad(itemDto.getCantidad());
            existingItem.setCostoUnitario(itemDto.getCostoUnitario());

            // Actualizar orden de compra
            if (itemDto.getOrdenCompraId() != null && !itemDto.getOrdenCompraId().equals(existingItem.getPurchaseOrder().getNumeroOrden())) {
                PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(itemDto.getOrdenCompraId())
                        .orElseThrow(() -> new EntityNotFoundException("Orden de Compra con ID " + itemDto.getOrdenCompraId() + " no encontrada"));
                existingItem.setPurchaseOrder(purchaseOrder);
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

            // Actualizar marca
            if (itemDto.getMarcaId() != null && !itemDto.getMarcaId().equals(existingItem.getBrand().getId())) {
                Brand brand = brandRepository.findById(itemDto.getMarcaId())
                        .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + itemDto.getMarcaId() + " no encontrada"));
                existingItem.setBrand(brand);
            }

            PurchaseOrderItem updatedItem = purchaseOrderItemRepository.save(existingItem);
            return convertToDto(updatedItem);
        }
        return null;
    }

    public boolean deletePurchaseOrderItem(Integer id) {
        if (purchaseOrderItemRepository.existsById(id)) {
            purchaseOrderItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}