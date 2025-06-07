package com.api.sicom.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.math.BigDecimal;

@Entity
@Table(name = "PurchaseOrderItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "costoUnitario", precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    @ManyToOne(fetch = FetchType.LAZY) // Un ítem de orden de compra pertenece a una orden de compra
    @JoinColumn(name = "ordenCompraId", referencedColumnName = "numeroOrden")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY) // Un ítem de orden de compra se refiere a un artículo
    @JoinColumn(name = "articuloId", referencedColumnName = "id")
    private Article article;

    @ManyToOne(fetch = Fetch = FetchType.LAZY) // Un ítem de orden de compra tiene una unidad de medida
    @JoinColumn(name = "unidadMedidaId", referencedColumnName = "id")
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY) // Un ítem de orden de compra tiene una marca
    @JoinColumn(name = "marcaId", referencedColumnName = "id")
    private Brand brand;
}
