package com.example.SistemaDeBiblioteca.model;

import com.example.SistemaDeBiblioteca.model.enums.EstadoLector;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lector")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lector")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String folio;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 100)
    private String contacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoLector estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_lector", nullable = false)
    private TipoLector tipoLector;
}
