package com.api.sicom.api.service;

import com.api.sicom.api.model.PurchaseOrderItem;
import com.api.sicom.api.repository.PurchaseOrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderItemService {

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    public List<PurchaseOrderItem> getAllPurchaseOrderItems() {
        return purchaseOrderItemRepository.findAll();
    }

    public Optional<PurchaseOrderItem> getPurchaseOrderItemById(Integer id) {
        return purchaseOrderItemRepository.findById(id);
    }

    public PurchaseOrderItem createPurchaseOrderItem(PurchaseOrderItem item) {
        return purchaseOrderItemRepository.save(item);
    }

    public PurchaseOrderItem updatePurchaseOrderItem(Integer id, PurchaseOrderItem itemDetails) {
        Optional<PurchaseOrderItem> optionalItem = purchaseOrderItemRepository.findById(id);
        if (optionalItem.isPresent()) {
            PurchaseOrderItem existingItem = optionalItem.get();
            existingItem.setCantidad(itemDetails.getCantidad());
            existingItem.setCostoUnitario(itemDetails.getCostoUnitario());
            existingItem.setArticle(itemDetails.getArticle()); // Asume que Article ya existe
            existingItem.setUnit(itemDetails.getUnit());       // Asume que Unit ya existe
            existingItem.setBrand(itemDetails.getBrand());     // Asume que Brand ya existe
            existingItem.setPurchaseOrder(itemDetails.getPurchaseOrder()); // Asume que PurchaseOrder ya existe
            return purchaseOrderItemRepository.save(existingItem);
        }
        return null; // O lanzar una excepción
    }

    public boolean deletePurchaseOrderItem(Integer id) {
        if (purchaseOrderItemRepository.existsById(id)) {
            purchaseOrderItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}