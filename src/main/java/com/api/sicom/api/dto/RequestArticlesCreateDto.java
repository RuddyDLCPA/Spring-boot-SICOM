package com.api.sicom.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import com.api.sicom.api.model.RequestArticles.RequestStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestArticlesCreateDto {
    private Integer empleadoId;
    private LocalDateTime fechaSolicitud;
    private RequestStatus estado;
    private Boolean isActive;
    // Para la creación, los ítems de solicitud se pueden manejar por separado o en un DTO más complejo
    // Para simplificar aquí, asumimos que los ítems se añadirán después de crear la solicitud principal
}
