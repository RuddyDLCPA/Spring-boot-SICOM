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
public class RequestArticlesResponseDto {
    private Integer id;
    private EmployeeResponseDto empleadoSolicitante; // Objeto completo para la respuesta
    private LocalDateTime fechaSolicitud;
    private RequestStatus estado;
    private List<RequestArticleItemResponseDto> items; // Lista de ítems anidados
    private Boolean isActive;
}