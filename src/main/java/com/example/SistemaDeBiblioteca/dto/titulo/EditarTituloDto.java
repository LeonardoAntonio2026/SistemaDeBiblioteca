package com.example.SistemaDeBiblioteca.dto.titulo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarTituloDto {
    @NotNull
    private Long tituloId;
    @NotBlank
    @Size(max = 200)
    private String titulo;
    @Size(max = 100)
    private String autor;
    @Size(max = 20)
    private String isbn;
    @Size(max = 100)
    private String editorial;
    private int anio;
    @Size(max = 50)
    private String categoria;
    @Size(max = 50)
    private String estadoCatalogo;
}
