package com.api.sicom.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "RequestArticles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestArticles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fechaSolicitud", nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Enumerated(EnumType.STRING) // Mapea el Enum de Java al String en la DB
    @Column(name = "estado")
    private RequestStatus estado = RequestStatus.pendiente;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY) // Una solicitud es hecha por un empleado
    @JoinColumn(name = "empleadoId", referencedColumnName = "id")
    private Employee employee;

    // Relación inversa con RequestArticleItem
    @OneToMany(mappedBy = "requestArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestArticleItem> requestArticleItems;

    // Relación inversa con PurchaseOrder
    @OneToMany(mappedBy = "requestArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrder> purchaseOrders;

    public enum RequestStatus {
        pendiente,
        aprobada,
        rechazada,
        anulada
    }
}

