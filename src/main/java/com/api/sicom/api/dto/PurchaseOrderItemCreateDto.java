package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemCreateDto {
    private Integer ordenCompraId; // El ID de la orden de compra a la que pertenece
    private Integer articuloId; // ID del artículo
    private Integer cantidad;
    private Integer unidadMedidaId; // ID de la unidad de medida
    private Integer marcaId; // ID de la marca
    private BigDecimal costoUnitario;
}
