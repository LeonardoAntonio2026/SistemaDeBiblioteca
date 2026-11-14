package com.example.SistemaDeBiblioteca.model;

import com.example.SistemaDeBiblioteca.model.enums.EstadoReserva;
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
    @Column(name = "id_reserva")
    private Long id;

    @Column(name = "fecha_hora_solicitud", nullable = false)
    private LocalDateTime fechaHoraSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado;

    @Column(name = "fecha_limite_recoleccion")
    private LocalDate fechaLimiteRecoleccion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_lector", nullable = false)
    private Lector lector;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_titulo", nullable = false)
    private Titulo titulo;

    @ManyToOne
    @JoinColumn(name = "id_ejemplar")
    private Ejemplar ejemplar;

    @OneToOne
    @JoinColumn(name = "id_prestamo")
    private Prestamo prestamo;
}
