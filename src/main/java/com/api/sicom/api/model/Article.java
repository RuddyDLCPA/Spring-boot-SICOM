package com.api.sicom.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "existencia")
    private Integer existencia = 0;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY) // Un artículo tiene una marca
    @JoinColumn(name = "marcaId", referencedColumnName = "id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY) // Un artículo tiene una unidad de medida
    @JoinColumn(name = "unidadMedidaId", referencedColumnName = "id")
    private Unit unit;

    // Relación inversa con RequestArticleItem
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestArticleItem> requestArticleItems;

    // Relación inversa con PurchaseOrderItem
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> purchaseOrderItems;
}
