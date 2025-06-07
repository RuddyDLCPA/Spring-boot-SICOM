package com.api.sicom.api.service;

import com.api.sicom.api.model.Employee;
import com.api.sicom.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Integer id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Integer id, Employee employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setCedula(employeeDetails.getCedula());
            existingEmployee.setNombre(employeeDetails.getNombre());
            existingEmployee.setDepartment(employeeDetails.getDepartment()); // Asume que el departamento ya existe y es válido
            existingEmployee.setIsActive(employeeDetails.getIsActive());
            return employeeRepository.save(existingEmployee);
        }
        return null; // O lanzar una excepción
    }

    public boolean deleteEmployee(Integer id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}