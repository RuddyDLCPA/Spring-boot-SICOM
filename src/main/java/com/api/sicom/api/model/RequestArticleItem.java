package com.api.sicom.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "RequestArticleItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestArticleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY) // Un ítem de solicitud pertenece a una solicitud
    @JoinColumn(name = "solicitudId", referencedColumnName = "id")
    private RequestArticles requestArticle;

    @ManyToOne(fetch = FetchType.LAZY) // Un ítem de solicitud se refiere a un artículo
    @JoinColumn(name = "articuloId", referencedColumnName = "id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY) // Un ítem de solicitud tiene una unidad de medida
    @JoinColumn(name = "unidadMedidaId", referencedColumnName = "id")
    private Unit unit;
}
