package com.api.sicom.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PurchaseOrder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numeroOrden; // Corresponde a 'numeroOrden' en la base de datos

    @Column(name = "fechaOrden", nullable = false)
    private LocalDateTime fechaOrden = LocalDateTime.now();

    @Enumerated(EnumType.STRING) // Mapea el Enum de Java al String en la DB
    @Column(name = "estado")
    private PurchaseStatus estado = PurchaseStatus.generada;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY) // Una orden de compra se refiere a una solicitud
    @JoinColumn(name = "idSolicitud", referencedColumnName = "id")
    private RequestArticles requestArticle;

    // Relación inversa con PurchaseOrderItem
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> purchaseOrderItems;

    public enum PurchaseStatus {
        generada,
        procesada,
        completada,
        cancelada
    }
}