package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {
    private String cedula;
    private String nombre;
    private Integer departamentoId; // Solo el ID para crear/actualizar
    private Boolean isActive;
}