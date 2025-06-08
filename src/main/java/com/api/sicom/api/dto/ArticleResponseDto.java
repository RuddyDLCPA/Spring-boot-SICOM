package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {
    private Integer id;
    private String descripcion;
    private BrandDto marca; // Objeto completo para la respuesta
    private UnitDto unidadMedida; // Objeto completo para la respuesta
    private Integer existencia;
    private Boolean isActive;
}