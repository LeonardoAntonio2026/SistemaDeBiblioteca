package com.example.SistemaDeBiblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "tipo_lector")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoLector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_lector")
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String tipo;

    @Column(name = "dias_prestamo", nullable = false)
    private int diasPrestamo;

    @Column(name = "max_libros", nullable = false)
    private int maxLibros;

    @Column(name = "max_renovaciones", nullable = false)
    private int maxRenovaciones;

    @Column(name = "multa_por_dia", precision = 10, scale = 2, nullable = false)
    private BigDecimal multaPorDia;

}
