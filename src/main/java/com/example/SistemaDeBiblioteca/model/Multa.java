package com.example.SistemaDeBiblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "multa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;
    private String motivo;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "prestamo_id")
    private Prestamo prestamo;
}
