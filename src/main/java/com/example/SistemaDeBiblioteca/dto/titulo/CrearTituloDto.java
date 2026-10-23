package com.example.SistemaDeBiblioteca.dto.titulo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearTituloDto {

    @NotBlank
    private String titulo;

    private String autor;

    private String editorial;

    private int anio;

    private String categoria;

    private String estadoCatalogo;
}
