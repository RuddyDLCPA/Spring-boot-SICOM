package com.api.sicom.api.service;

import com.api.sicom.api.dto.DepartmentDto;
import com.api.sicom.api.dto.EmployeeCreateDto;
import com.api.sicom.api.dto.EmployeeResponseDto;
import com.api.sicom.api.model.Department;
import com.api.sicom.api.model.Employee;
import com.api.sicom.api.repository.DepartmentRepository;
import com.api.sicom.api.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    // Convertir entidad a DTO de respuesta
    private EmployeeResponseDto convertToDto(Employee employee) {
        if (employee == null) return null;
        DepartmentDto departmentDto = null;
        if (employee.getDepartment() != null) {
            departmentDto = new DepartmentDto(
                    employee.getDepartment().getId(),
                    employee.getDepartment().getNombre(),
                    employee.getDepartment().getIsActive()
            );
        }
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getCedula(),
                employee.getNombre(),
                departmentDto,
                employee.getIsActive()
        );
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeResponseDto> getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .map(this::convertToDto);
    }

    public EmployeeResponseDto createEmployee(EmployeeCreateDto employeeDto) {
        Employee employee = new Employee();
        employee.setCedula(employeeDto.getCedula());
        employee.setNombre(employeeDto.getNombre());
        employee.setIsActive(employeeDto.getIsActive() != null ? employeeDto.getIsActive() : true);

        // Buscar el departamento por ID
        if (employeeDto.getDepartamentoId() != null) {
            Department department = departmentRepository.findById(employeeDto.getDepartamentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Departamento con ID " + employeeDto.getDepartamentoId() + " no encontrado"));
            employee.setDepartment(department);
        } else {
            throw new IllegalArgumentException("El ID del departamento no puede ser nulo.");
        }

        Employee createdEmployee = employeeRepository.save(employee);
        return convertToDto(createdEmployee);
    }

    public EmployeeResponseDto updateEmployee(Integer id, EmployeeCreateDto employeeDto) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setCedula(employeeDto.getCedula());
            existingEmployee.setNombre(employeeDto.getNombre());
            existingEmployee.setIsActive(employeeDto.getIsActive());

            // Actualizar el departamento si se proporciona un nuevo ID
            if (employeeDto.getDepartamentoId() != null && !employeeDto.getDepartamentoId().equals(existingEmployee.getDepartment().getId())) {
                Department department = departmentRepository.findById(employeeDto.getDepartamentoId())
                        .orElseThrow(() -> new EntityNotFoundException("Departamento con ID " + employeeDto.getDepartamentoId() + " no encontrado"));
                existingEmployee.setDepartment(department);
            } else if (employeeDto.getDepartamentoId() == null) {
                // Si el DTO envía departamentoId nulo, puedes decidir qué hacer:
                // 1. Mantener el departamento actual: No hacer nada.
                // 2. Establecer el departamento a nulo: existingEmployee.setDepartment(null);
                // 3. Lanzar una excepción si el departamento es obligatorio: throw new IllegalArgumentException("Departamento ID no puede ser nulo.");
                // Por ahora, lanzaremos una excepción si se intenta anular un departamento existente que es obligatorio
                throw new IllegalArgumentException("El ID del departamento no puede ser nulo en una actualización.");
            }

            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return convertToDto(updatedEmployee);
        }
        return null;
    }

    public boolean deleteEmployee(Integer id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
