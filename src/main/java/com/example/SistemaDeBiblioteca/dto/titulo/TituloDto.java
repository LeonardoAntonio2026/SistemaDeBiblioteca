package com.example.SistemaDeBiblioteca.dto.titulo;

import com.example.SistemaDeBiblioteca.model.Titulo;

public record TituloDto(Long id, String titulo, String autor, String isbn, String editorial,
                        int anio, String categoria, String estadoCatalogo) {

    public static TituloDto de(Titulo titulo) {
        return new TituloDto(titulo.getId(), titulo.getTitulo(), titulo.getAutor(), titulo.getIsbn(),
                titulo.getEditorial(), titulo.getAnio(), titulo.getCategoria(), titulo.getEstadoCatalogo());
    }
}
