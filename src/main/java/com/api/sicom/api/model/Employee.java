package com.api.sicom.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cedula", unique = true, nullable = false)
    private String cedula;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY) // Un empleado pertenece a un departamento
    @JoinColumn(name = "departamentoId", referencedColumnName = "id") // Columna de la clave foránea
    private Department department;

    // Relación inversa con RequestArticles (un empleado puede realizar muchas solicitudes)
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestArticles> requestArticles;
}
