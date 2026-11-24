package com.example.SistemaDeBiblioteca.dto.ejemplar;

import com.example.SistemaDeBiblioteca.model.Ejemplar;
import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;

public record EjemplarDto(Long id, String folio, String ubicacion, String condicion,
                          EstadoEjemplar estadoOperativo, Long tituloId, String titulo) {

    public static EjemplarDto de(Ejemplar ejemplar) {
        return new EjemplarDto(ejemplar.getId(), ejemplar.getFolio(), ejemplar.getUbicacion(),
                ejemplar.getCondicion(), ejemplar.getEstadoOperativo(),
                ejemplar.getTitulo().getId(), ejemplar.getTitulo().getTitulo());
    }
}
