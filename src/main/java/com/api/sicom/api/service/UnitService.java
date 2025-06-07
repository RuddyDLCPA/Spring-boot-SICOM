package com.api.sicom.api.service;

import com.api.sicom.api.model.Unit;
import com.api.sicom.api.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Optional<Unit> getUnitById(Integer id) {
        return unitRepository.findById(id);
    }

    public Unit createUnit(Unit unit) {
        return unitRepository.save(unit);
    }

    public Unit updateUnit(Integer id, Unit unitDetails) {
        Optional<Unit> optionalUnit = unitRepository.findById(id);
        if (optionalUnit.isPresent()) {
            Unit existingUnit = optionalUnit.get();
            existingUnit.setDescripcion(unitDetails.getDescripcion());
            existingUnit.setIsActive(unitDetails.getIsActive());
            return unitRepository.save(existingUnit);
        }
        return null; // O lanzar una excepción
    }

    public boolean deleteUnit(Integer id) {
        if (unitRepository.existsById(id)) {
            unitRepository.deleteById(id);
            return true;
        }
        return false;
    }
}