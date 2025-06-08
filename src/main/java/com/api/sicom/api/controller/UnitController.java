package com.api.sicom.api.controller;

import com.api.sicom.api.dto.UnitDto; // Usar el DTO
import com.api.sicom.api.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades") // Endpoint cambiado a /api/unidades
public class UnitController {

    @Autowired
    private UnitService unitService;

    @GetMapping
    public List<UnitDto> getAllUnits() {
        return unitService.getAllUnits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitDto> getUnitById(@PathVariable Integer id) {
        return unitService.getUnitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UnitDto> createUnit(@RequestBody UnitDto unitDto) {
        UnitDto createdUnit = unitService.createUnit(unitDto);
        return new ResponseEntity<>(createdUnit, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitDto> updateUnit(@PathVariable Integer id, @RequestBody UnitDto unitDto) {
        UnitDto updatedUnit = unitService.updateUnit(id, unitDto);
        if (updatedUnit != null) {
            return ResponseEntity.ok(updatedUnit);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Integer id) {
        if (unitService.deleteUnit(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
