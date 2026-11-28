package com.example.SistemaDeBiblioteca.dto.titulo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditarTituloDto {
    @NotNull(message = "El ID del título es obligatorio")
    private Long tituloId;
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede pasar de 200 caracteres")
    private String titulo;
    @Size(max = 100, message = "El autor no puede pasar de 100 caracteres")
    private String autor;
    @Size(max = 20, message = "El ISBN no puede pasar de 20 caracteres")
    private String isbn;
    @Size(max = 100, message = "La editorial no puede pasar de 100 caracteres")
    private String editorial;
    private int anio;
    @Size(max = 50, message = "La categoría no puede pasar de 50 caracteres")
    private String categoria;
    @Size(max = 50, message = "El estado en catálogo no puede pasar de 50 caracteres")
    private String estadoCatalogo;
}
