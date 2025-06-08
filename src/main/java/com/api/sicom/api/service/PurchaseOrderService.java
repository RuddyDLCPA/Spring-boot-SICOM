package com.api.sicom.api.service;

import com.api.sicom.api.model.PurchaseOrder;
import com.api.sicom.api.model.RequestArticles; // Necesario para buscar solicitud
import com.api.sicom.api.repository.PurchaseOrderRepository;
import com.api.sicom.api.repository.RequestArticlesRepository; // Necesario
import com.api.sicom.api.dto.PurchaseOrderCreateDto;
import com.api.sicom.api.dto.PurchaseOrderResponseDto;
import com.api.sicom.api.dto.PurchaseOrderItemResponseDto; // Para la lista de ítems
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private RequestArticlesRepository requestArticlesRepository;
    @Autowired
    private PurchaseOrderItemService purchaseOrderItemService; // Para convertir los ítems

    // Convertir entidad a DTO de respuesta
    private PurchaseOrderResponseDto convertToDto(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) return null;

        List<PurchaseOrderItemResponseDto> itemDtos = null;
        if (purchaseOrder.getPurchaseOrderItems() != null) {
            itemDtos = purchaseOrder.getPurchaseOrderItems().stream()
                    .map(purchaseOrderItemService::convertToDto) // Usar el método de conversión del servicio de ítems
                    .collect(Collectors.toList());
        }

        // Asumiendo que `idSolicitud` en el DTO es solo el ID, no el objeto completo de RequestArticles
        return new PurchaseOrderResponseDto(
                purchaseOrder.getNumeroOrden(),
                purchaseOrder.getRequestArticle() != null ? purchaseOrder.getRequestArticle().getId() : null,
                purchaseOrder.getFechaOrden(),
                purchaseOrder.getEstado(),
                itemDtos,
                purchaseOrder.getIsActive()
        );
    }

    public List<PurchaseOrderResponseDto> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PurchaseOrderResponseDto> getPurchaseOrderById(Integer id) {
        return purchaseOrderRepository.findById(id)
                .map(this::convertToDto);
    }

    public PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderCreateDto purchaseOrderDto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setFechaOrden(purchaseOrderDto.getFechaOrden() != null ? purchaseOrderDto.getFechaOrden() : LocalDateTime.now());
        purchaseOrder.setEstado(purchaseOrderDto.getEstado() != null ? purchaseOrderDto.getEstado() : PurchaseOrder.PurchaseStatus.generada);
        purchaseOrder.setIsActive(purchaseOrderDto.getIsActive() != null ? purchaseOrderDto.getIsActive() : true);

        // Buscar la solicitud asociada
        if (purchaseOrderDto.getIdSolicitud() != null) {
            RequestArticles requestArticles = requestArticlesRepository.findById(purchaseOrderDto.getIdSolicitud())
                    .orElseThrow(() -> new EntityNotFoundException("Solicitud con ID " + purchaseOrderDto.getIdSolicitud() + " no encontrada"));
            purchaseOrder.setRequestArticle(requestArticles);
        } else {
            throw new IllegalArgumentException("El ID de la solicitud no puede ser nulo.");
        }


        PurchaseOrder createdOrder = purchaseOrderRepository.save(purchaseOrder);
        return convertToDto(createdOrder);
    }

    public PurchaseOrderResponseDto updatePurchaseOrder(Integer id, PurchaseOrderCreateDto purchaseOrderDto) {
        Optional<PurchaseOrder> optionalOrder = purchaseOrderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            PurchaseOrder existingOrder = optionalOrder.get();

            // Actualizar solicitud si se proporciona un nuevo ID
            if (purchaseOrderDto.getIdSolicitud() != null && !purchaseOrderDto.getIdSolicitud().equals(existingOrder.getRequestArticle().getId())) {
                RequestArticles requestArticles = requestArticlesRepository.findById(purchaseOrderDto.getIdSolicitud())
                        .orElseThrow(() -> new EntityNotFoundException("Solicitud con ID " + purchaseOrderDto.getIdSolicitud() + " no encontrada"));
                existingOrder.setRequestArticle(requestArticles);
            } else if (purchaseOrderDto.getIdSolicitud() == null) {
                throw new IllegalArgumentException("El ID de la solicitud no puede ser nulo en una actualización.");
            }

            if (purchaseOrderDto.getEstado() != null) {
                existingOrder.setEstado(purchaseOrderDto.getEstado());
            }
            if (purchaseOrderDto.getIsActive() != null) {
                existingOrder.setIsActive(purchaseOrderDto.getIsActive());
            }

            PurchaseOrder updatedOrder = purchaseOrderRepository.save(existingOrder);
            return convertToDto(updatedOrder);
        }
        return null;
    }

    public boolean deletePurchaseOrder(Integer id) {
        if (purchaseOrderRepository.existsById(id)) {
            purchaseOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
