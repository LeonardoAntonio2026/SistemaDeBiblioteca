package com.example.SistemaDeBiblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ejemplar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ejemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejemplar")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String folio;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private String condicion;

    @Column(name = "estado_operativo", nullable = false)
    private String estadoOperativo;

    @ManyToOne
    @JoinColumn(name = "id_titulo")
    private Titulo titulo;

}
