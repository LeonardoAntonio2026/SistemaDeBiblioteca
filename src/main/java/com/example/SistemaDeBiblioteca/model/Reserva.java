package com.example.SistemaDeBiblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHoraSubida;

    private String estado;

    private LocalDate fechaLimiteRecoleccion;

    @ManyToOne
    @JoinColumn(name = "lector_id_lector")
    private Lector lector;

    @ManyToOne
    @JoinColumn(name = "titulo_id_titulo")
    private Titulo titulo;

    @ManyToOne
    @JoinColumn(name = "ejemplar_id_ejemplar")
    private Ejemplar ejemplar;
}
