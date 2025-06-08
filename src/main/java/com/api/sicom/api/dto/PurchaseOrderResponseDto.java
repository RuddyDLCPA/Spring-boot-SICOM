package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import com.api.sicom.api.model.PurchaseOrder.PurchaseStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponseDto {
    private Integer numeroOrden;
    private Integer idSolicitud; // Mantener el ID de la solicitud
    // private RequestArticlesResponseDto solicitud; // Si quisieras el objeto solicitud completo
    private LocalDateTime fechaOrden;
    private PurchaseStatus estado;
    private List<PurchaseOrderItemResponseDto> items; // Lista de ítems anidados
    private Boolean isActive;
}