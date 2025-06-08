package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDto {
    private Integer id;
    private String descripcion;
    private Boolean isActive;
}