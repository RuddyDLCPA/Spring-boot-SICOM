package com.api.sicom.api.service;

import com.api.sicom.api.dto.*;
import com.api.sicom.api.model.RequestArticles;
import com.api.sicom.api.model.Employee; // Necesario para buscar empleado
import com.api.sicom.api.repository.RequestArticlesRepository;
import com.api.sicom.api.repository.EmployeeRepository; // Necesario
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestArticlesService {

    @Autowired
    private RequestArticlesRepository requestArticlesRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RequestArticleItemService requestArticleItemService; // Para convertir los ítems

    // Convertir entidad a DTO de respuesta
    private RequestArticlesResponseDto convertToDto(RequestArticles requestArticles) {
        if (requestArticles == null) return null;

        EmployeeResponseDto employeeDto = null;
        if (requestArticles.getEmployee() != null) {
            employeeDto = new EmployeeResponseDto(
                    requestArticles.getEmployee().getId(),
                    requestArticles.getEmployee().getCedula(),
                    requestArticles.getEmployee().getNombre(),
                    // Se asume que el departamento se carga aquí para el empleado
                    new DepartmentDto(requestArticles.getEmployee().getDepartment().getId(),
                            requestArticles.getEmployee().getDepartment().getNombre(),
                            requestArticles.getEmployee().getDepartment().getIsActive()),
                    requestArticles.getEmployee().getIsActive()
            );
        }

        List<RequestArticleItemResponseDto> itemDtos = null;
        if (requestArticles.getRequestArticleItems() != null) {
            itemDtos = requestArticles.getRequestArticleItems().stream()
                    .map(requestArticleItemService::convertToDto) // Usar el método de conversión del servicio de ítems
                    .collect(Collectors.toList());
        }

        return new RequestArticlesResponseDto(
                requestArticles.getId(),
                employeeDto,
                requestArticles.getFechaSolicitud(),
                requestArticles.getEstado(),
                itemDtos,
                requestArticles.getIsActive()
        );
    }

    public List<RequestArticlesResponseDto> getAllRequestArticles() {
        return requestArticlesRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<RequestArticlesResponseDto> getRequestArticlesById(Integer id) {
        return requestArticlesRepository.findById(id)
                .map(this::convertToDto);
    }

    public RequestArticlesResponseDto createRequestArticles(RequestArticlesCreateDto requestArticlesDto) {
        RequestArticles requestArticles = new RequestArticles();
        requestArticles.setFechaSolicitud(requestArticlesDto.getFechaSolicitud() != null ? requestArticlesDto.getFechaSolicitud() : LocalDateTime.now());
        requestArticles.setEstado(requestArticlesDto.getEstado() != null ? requestArticlesDto.getEstado() : RequestArticles.RequestStatus.pendiente);
        requestArticles.setIsActive(requestArticlesDto.getIsActive() != null ? requestArticlesDto.getIsActive() : true);

        // Buscar el empleado
        Employee employee = employeeRepository.findById(requestArticlesDto.getEmpleadoId())
                .orElseThrow(() -> new EntityNotFoundException("Empleado con ID " + requestArticlesDto.getEmpleadoId() + " no encontrado"));
        requestArticles.setEmployee(employee);

        RequestArticles createdRequest = requestArticlesRepository.save(requestArticles);
        return convertToDto(createdRequest);
    }

    public RequestArticlesResponseDto updateRequestArticles(Integer id, RequestArticlesCreateDto requestArticlesDto) {
        Optional<RequestArticles> optionalRequest = requestArticlesRepository.findById(id);
        if (optionalRequest.isPresent()) {
            RequestArticles existingRequest = optionalRequest.get();

            // Actualizar empleado si se proporciona un nuevo ID
            if (requestArticlesDto.getEmpleadoId() != null && !requestArticlesDto.getEmpleadoId().equals(existingRequest.getEmployee().getId())) {
                Employee employee = employeeRepository.findById(requestArticlesDto.getEmpleadoId())
                        .orElseThrow(() -> new EntityNotFoundException("Empleado con ID " + requestArticlesDto.getEmpleadoId() + " no encontrado"));
                existingRequest.setEmployee(employee);
            }

            // Solo actualizar el estado si se proporciona
            if (requestArticlesDto.getEstado() != null) {
                existingRequest.setEstado(requestArticlesDto.getEstado());
            }
            if (requestArticlesDto.getIsActive() != null) {
                existingRequest.setIsActive(requestArticlesDto.getIsActive());
            }
            // La fecha de solicitud no se actualiza por defecto en una actualización,
            // pero se podría añadir lógica si es necesario

            RequestArticles updatedRequest = requestArticlesRepository.save(existingRequest);
            return convertToDto(updatedRequest);
        }
        return null;
    }

    public boolean deleteRequestArticles(Integer id) {
        if (requestArticlesRepository.existsById(id)) {
            requestArticlesRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
