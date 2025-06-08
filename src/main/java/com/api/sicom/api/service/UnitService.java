package com.api.sicom.api.service;

import com.api.sicom.api.model.Unit;
import com.api.sicom.api.repository.UnitRepository;
import com.api.sicom.api.dto.UnitDto; // Importar el DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    // Convertir entidad a DTO
    private UnitDto convertToDto(Unit unit) {
        if (unit == null) return null;
        return new UnitDto(unit.getId(), unit.getDescripcion(), unit.getIsActive());
    }

    // Convertir DTO a entidad (para creación/actualización)
    private Unit convertToEntity(UnitDto unitDto) {
        if (unitDto == null) return null;
        return new Unit(unitDto.getId(), unitDto.getDescripcion(), unitDto.getIsActive(), null, null, null); // Lists are managed elsewhere
    }

    public List<UnitDto> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<UnitDto> getUnitById(Integer id) {
        return unitRepository.findById(id)
                .map(this::convertToDto);
    }

    public UnitDto createUnit(UnitDto unitDto) {
        Unit unit = convertToEntity(unitDto);
        unit.setIsActive(true);
        Unit createdUnit = unitRepository.save(unit);
        return convertToDto(createdUnit);
    }

    public UnitDto updateUnit(Integer id, UnitDto unitDto) {
        Optional<Unit> optionalUnit = unitRepository.findById(id);
        if (optionalUnit.isPresent()) {
            Unit existingUnit = optionalUnit.get();
            existingUnit.setDescripcion(unitDto.getDescripcion());
            existingUnit.setIsActive(unitDto.getIsActive());
            Unit updatedUnit = unitRepository.save(existingUnit);
            return convertToDto(updatedUnit);
        }
        return null;
    }

    public boolean deleteUnit(Integer id) {
        if (unitRepository.existsById(id)) {
            unitRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
