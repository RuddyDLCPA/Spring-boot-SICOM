package com.api.sicom.api.service;

import com.api.sicom.api.model.RequestArticles;
import com.api.sicom.api.repository.RequestArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestArticlesService {

    @Autowired
    private RequestArticlesRepository requestArticlesRepository;

    public List<RequestArticles> getAllRequestArticles() {
        return requestArticlesRepository.findAll();
    }

    public Optional<RequestArticles> getRequestArticlesById(Integer id) {
        return requestArticlesRepository.findById(id);
    }

    public RequestArticles createRequestArticles(RequestArticles requestArticles) {
        // La fecha de solicitud se establece automáticamente en la entidad
        return requestArticlesRepository.save(requestArticles);
    }

    public RequestArticles updateRequestArticles(Integer id, RequestArticles requestArticlesDetails) {
        Optional<RequestArticles> optionalRequest = requestArticlesRepository.findById(id);
        if (optionalRequest.isPresent()) {
            RequestArticles existingRequest = optionalRequest.get();
            existingRequest.setEmployee(requestArticlesDetails.getEmployee()); // Asume que Employee ya existe
            existingRequest.setEstado(requestArticlesDetails.getEstado());
            existingRequest.setIsActive(requestArticlesDetails.getIsActive());
            // No actualizar fechaSolicitud a menos que sea explícitamente necesario y se tenga una lógica para ello
            return requestArticlesRepository.save(existingRequest);
        }
        return null; // O lanzar una excepción
    }

    public boolean deleteRequestArticles(Integer id) {
        if (requestArticlesRepository.existsById(id)) {
            requestArticlesRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
