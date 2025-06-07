package com.api.sicom.api.service;

import com.api.sicom.api.model.PurchaseOrder;
import com.api.sicom.api.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public Optional<PurchaseOrder> getPurchaseOrderById(Integer id) {
        return purchaseOrderRepository.findById(id);
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        // La fecha de orden se establece automáticamente en la entidad
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder updatePurchaseOrder(Integer id, PurchaseOrder orderDetails) {
        Optional<PurchaseOrder> optionalOrder = purchaseOrderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            PurchaseOrder existingOrder = optionalOrder.get();
            existingOrder.setEstado(orderDetails.getEstado());
            existingOrder.setIsActive(orderDetails.getIsActive());
            existingOrder.setRequestArticle(orderDetails.getRequestArticle()); // Asume que RequestArticles ya existe
            // No actualizar fechaOrden a menos que sea explícitamente necesario
            return purchaseOrderRepository.save(existingOrder);
        }
        return null; // O lanzar una excepción
    }

    public boolean deletePurchaseOrder(Integer id) {
        if (purchaseOrderRepository.existsById(id)) {
            purchaseOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}