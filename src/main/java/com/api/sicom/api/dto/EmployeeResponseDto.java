package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    private Integer id;
    private String cedula;
    private String nombre;
    private DepartmentDto departamento; // Objeto completo para la respuesta
    private Boolean isActive;
}
