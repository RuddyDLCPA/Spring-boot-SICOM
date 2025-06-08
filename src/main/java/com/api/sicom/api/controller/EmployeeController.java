package com.api.sicom.api.controller;

import com.api.sicom.api.dto.EmployeeCreateDto; // Usar DTO de creación
import com.api.sicom.api.dto.EmployeeResponseDto; // Usar DTO de respuesta
import com.api.sicom.api.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException; // Para capturar la excepción

import java.util.List;

@RestController
@RequestMapping("/api/empleados") // Endpoint cambiado a /api/empleados
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody EmployeeCreateDto employeeDto) {
        try {
            EmployeeResponseDto createdEmployee = employeeService.createEmployee(employeeDto);
            return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el departamento no existe
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el ID del departamento es nulo
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeCreateDto employeeDto) {
        try {
            EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
            if (updatedEmployee != null) {
                return ResponseEntity.ok(updatedEmployee);
            }
            return ResponseEntity.notFound().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el departamento no existe
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el ID del departamento es nulo
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        if (employeeService.deleteEmployee(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}