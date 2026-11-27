package com.example.SistemaDeBiblioteca.dto.ejemplar;

import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;

import java.time.LocalDate;

public record LocalizacionEjemplarDto(
        Long ejemplarId,
        String folio,
        String titulo,
        EstadoEjemplar estadoOperativo,
        String ubicacion,
        Long lectorId,
        String lectorNombre,
        LocalDate fechaLimite
){
}
