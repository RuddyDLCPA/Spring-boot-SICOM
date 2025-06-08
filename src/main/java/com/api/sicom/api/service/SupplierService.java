package com.api.sicom.api.service;

import com.api.sicom.api.dto.SupplierDto;
import com.api.sicom.api.model.Supplier;
import com.api.sicom.api.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // Convertir entidad a DTO
    private SupplierDto convertToDto(Supplier supplier) {
        if (supplier == null) return null;
        return new SupplierDto(supplier.getId(), supplier.getCedulaRnc(), supplier.getNombreComercial(), supplier.getIsActive());
    }

    // Convertir DTO a entidad (para creación/actualización)
    private Supplier convertToEntity(SupplierDto supplierDto) {
        if (supplierDto == null) return null;
        return new Supplier(supplierDto.getId(), supplierDto.getCedulaRnc(), supplierDto.getNombreComercial(), supplierDto.getIsActive());
    }

    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<SupplierDto> getSupplierById(Integer id) {
        return supplierRepository.findById(id)
                .map(this::convertToDto);
    }

    public SupplierDto createSupplier(SupplierDto supplierDto) {
        Supplier supplier = convertToEntity(supplierDto);
        supplier.setIsActive(true);
        Supplier createdSupplier = supplierRepository.save(supplier);
        return convertToDto(createdSupplier);
    }

    public SupplierDto updateSupplier(Integer id, SupplierDto supplierDto) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        if (optionalSupplier.isPresent()) {
            Supplier existingSupplier = optionalSupplier.get();
            existingSupplier.setCedulaRnc(supplierDto.getCedulaRnc());
            existingSupplier.setNombreComercial(supplierDto.getNombreComercial());
            existingSupplier.setIsActive(supplierDto.getIsActive());
            Supplier updatedSupplier = supplierRepository.save(existingSupplier);
            return convertToDto(updatedSupplier); // Corregido: Usar 'updatedSupplier'
        }
        return null;
    }

    public boolean deleteSupplier(Integer id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
