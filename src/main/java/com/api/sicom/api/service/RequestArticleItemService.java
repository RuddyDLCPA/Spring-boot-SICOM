package com.api.sicom.api.service;

import com.api.sicom.api.model.RequestArticleItem;
import com.api.sicom.api.repository.RequestArticleItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestArticleItemService {

    @Autowired
    private RequestArticleItemRepository requestArticleItemRepository;

    public List<RequestArticleItem> getAllRequestArticleItems() {
        return requestArticleItemRepository.findAll();
    }

    public Optional<RequestArticleItem> getRequestArticleItemById(Integer id) {
        return requestArticleItemRepository.findById(id);
    }

    public RequestArticleItem createRequestArticleItem(RequestArticleItem requestArticleItem) {
        return requestArticleItemRepository.save(requestArticleItem);
    }

    public RequestArticleItem updateRequestArticleItem(Integer id, RequestArticleItem itemDetails) {
        Optional<RequestArticleItem> optionalItem = requestArticleItemRepository.findById(id);
        if (optionalItem.isPresent()) {
            RequestArticleItem existingItem = optionalItem.get();
            existingItem.setCantidad(itemDetails.getCantidad());
            existingItem.setArticle(itemDetails.getArticle()); // Asume que Article ya existe
            existingItem.setUnit(itemDetails.getUnit());       // Asume que Unit ya existe
            existingItem.setRequestArticle(itemDetails.getRequestArticle()); // Asume que RequestArticles ya existe
            return requestArticleItemRepository.save(existingItem);
        }
        return null; // O lanzar una excepción
    }

    public boolean deleteRequestArticleItem(Integer id) {
        if (requestArticleItemRepository.existsById(id)) {
            requestArticleItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}