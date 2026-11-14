package com.example.SistemaDeBiblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "titulo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_titulo")
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 100)
    private String autor;

    @Column(unique = true, length = 20)
    private String isbn;

    @Column(length = 100)
    private String editorial;

    @Column(name = "anio")
    private int anio;

    @Column(length = 50)
    private String categoria;

    @Column(name = "estado_catalogo", length = 50)
    private String estadoCatalogo;
}
