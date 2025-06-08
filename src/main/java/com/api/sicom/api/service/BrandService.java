package com.api.sicom.api.service;

import com.api.sicom.api.model.Brand;
import com.api.sicom.api.repository.BrandRepository;
import com.api.sicom.api.dto.BrandDto; // Importar el DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Para mapear listas

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    // Convertir entidad a DTO
    private BrandDto convertToDto(Brand brand) {
        if (brand == null) return null;
        return new BrandDto(brand.getId(), brand.getDescripcion(), brand.getIsActive());
    }

    // Convertir DTO a entidad (para creación/actualización)
    private Brand convertToEntity(BrandDto brandDto) {
        if (brandDto == null) return null;
        // Si el DTO tiene un ID, se considerará una actualización, de lo contrario una creación
        return new Brand(brandDto.getId(), brandDto.getDescripcion(), brandDto.getIsActive(), null, null); // Los lists 'articles' y 'purchaseOrderItems' se manejan en sus propias entidades
    }

    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<BrandDto> getBrandById(Integer id) {
        return brandRepository.findById(id)
                .map(this::convertToDto);
    }

    public BrandDto createBrand(BrandDto brandDto) {
        Brand brand = convertToEntity(brandDto);
        brand.setIsActive(true); // Asegurar que isActive es true por defecto si no viene en el DTO
        Brand createdBrand = brandRepository.save(brand);
        return convertToDto(createdBrand);
    }

    public BrandDto updateBrand(Integer id, BrandDto brandDto) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isPresent()) {
            Brand existingBrand = optionalBrand.get();
            existingBrand.setDescripcion(brandDto.getDescripcion());
            existingBrand.setIsActive(brandDto.getIsActive());
            Brand updatedBrand = brandRepository.save(existingBrand);
            return convertToDto(updatedBrand);
        }
        return null; // Manejar como excepción en el controlador
    }

    public boolean deleteBrand(Integer id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
            return true;
        }
        return false;
    }
}