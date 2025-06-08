package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateDto {
    private String descripcion;
    private Integer marcaId; // Solo el ID para crear/actualizar
    private Integer unidadMedidaId; // Solo el ID para crear/actualizar
    private Integer existencia;
    private Boolean isActive;
}