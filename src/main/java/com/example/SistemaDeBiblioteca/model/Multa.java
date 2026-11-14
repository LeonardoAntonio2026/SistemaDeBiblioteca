package com.example.SistemaDeBiblioteca.model;

import com.example.SistemaDeBiblioteca.model.enums.EstadoMulta;
import com.example.SistemaDeBiblioteca.model.enums.MotivoMulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "multa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_multa")
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MotivoMulta motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoMulta estado;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDate fechaGeneracion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_prestamo", nullable = false)
    private Prestamo prestamo;
}
