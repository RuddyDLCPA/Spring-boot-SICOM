package com.api.sicom.api.service;

import com.api.sicom.api.dto.DepartmentDto;
import com.api.sicom.api.model.Department;
import com.api.sicom.api.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Convertir entidad a DTO
    private DepartmentDto convertToDto(Department department) {
        if (department == null) return null;
        return new DepartmentDto(department.getId(), department.getNombre(), department.getIsActive());
    }

    // Convertir DTO a entidad (para creación/actualización)
    private Department convertToEntity(DepartmentDto departmentDto) {
        if (departmentDto == null) return null;
        return new Department(departmentDto.getId(), departmentDto.getNombre(), departmentDto.getIsActive(), null); // List 'employees' is managed elsewhere
    }

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<DepartmentDto> getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .map(this::convertToDto);
    }

    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = convertToEntity(departmentDto);
        department.setIsActive(true);
        Department createdDepartment = departmentRepository.save(department);
        return convertToDto(createdDepartment);
    }

    public DepartmentDto updateDepartment(Integer id, DepartmentDto departmentDto) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            Department existingDepartment = optionalDepartment.get();
            existingDepartment.setNombre(departmentDto.getNombre());
            existingDepartment.setIsActive(departmentDto.getIsActive());
            Department updatedDepartment = departmentRepository.save(existingDepartment);
            return convertToDto(updatedDepartment);
        }
        return null;
    }

    public boolean deleteDepartment(Integer id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
