package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestArticleItemResponseDto {
    private Integer id;
    private ArticleResponseDto articulo; // Objeto completo del artículo para la respuesta
    private Integer cantidad;
    private UnitDto unidadMedida; // Objeto completo de la unidad de medida para la respuesta
}