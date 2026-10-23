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
    private Long id;
    private LocalDate fechaSalida;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private int renovacionesRealizadas;
    private String observaciones;
    @ManyToOne
    @JoinColumn(name = "lector_id_lector")
    private Lector lector;
    @ManyToOne
    @JoinColumn(name = "ejemplar_id_ejemplar")
    private Ejemplar ejemplar;

}
