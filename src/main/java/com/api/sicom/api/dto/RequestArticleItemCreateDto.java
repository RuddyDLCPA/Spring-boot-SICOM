package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestArticleItemCreateDto {
    private Integer solicitudId; // El ID de la solicitud a la que pertenece
    private Integer articuloId; // Solo el ID del artículo
    private Integer cantidad;
    private Integer unidadMedidaId; // Solo el ID de la unidad de medida
}