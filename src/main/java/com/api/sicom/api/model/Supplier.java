package com.api.sicom.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Supplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cedulaRnc")
    private String cedulaRnc;

    @Column(name = "nombreComercial")
    private String nombreComercial;

    @Column(name = "isActive")
    private Boolean isActive = true;
}
