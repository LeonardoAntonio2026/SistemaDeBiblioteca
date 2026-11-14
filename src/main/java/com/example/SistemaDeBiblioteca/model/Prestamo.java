package com.example.SistemaDeBiblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "prestamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Long id;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDate fechaSalida;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Column(name = "renovaciones_realizadas", nullable = false)
    private int renovacionesRealizadas;

    private String observaciones;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_lector", nullable = false)
    private Lector lector;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_ejemplar", nullable = false)
    private Ejemplar ejemplar;

}
