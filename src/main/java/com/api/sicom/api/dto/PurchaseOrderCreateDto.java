package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import com.api.sicom.api.model.PurchaseOrder.PurchaseStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderCreateDto {
    private Integer idSolicitud; // Solo el ID de la solicitud para crear/actualizar
    private LocalDateTime fechaOrden;
    private PurchaseStatus estado;
    private Boolean isActive;
}