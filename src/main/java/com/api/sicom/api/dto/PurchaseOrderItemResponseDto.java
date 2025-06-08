package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemResponseDto {
    private Integer id;
    private String articulo; // Descripción del artículo (según tu JSON de ejemplo)
    private Integer cantidad;
    private String unidadMedida; // Descripción de la unidad de medida
    private String marca; // Descripción de la marca
    private BigDecimal costoUnitario;
}