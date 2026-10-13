package com.example.SistemaDeBiblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="tipo_lector")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoLector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_lector")
    private int id;


    @Column(length = 50)
    private String tipo;

    @Column()
    private int dias_prestamo;

    @Column()
    private int maxLibros;

    @Column()
    private int maxRenovaciones;

    @Column(precision = 10, scale = 2)
    private BigDecimal multa_por_dia;
}
