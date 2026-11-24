package com.example.SistemaDeBiblioteca.dto.prestamo;

import com.example.SistemaDeBiblioteca.model.Prestamo;

import java.time.LocalDate;

public record PrestamoDto(Long id, Long lectorId, String lector, Long ejemplarId, String folioEjemplar,
                          String titulo, LocalDate fechaSalida, LocalDate fechaLimite, LocalDate fechaDevolucion,
                          String observaciones) {

    public static PrestamoDto de(Prestamo prestamo) {
        return new PrestamoDto(prestamo.getId(), prestamo.getLector().getId(), prestamo.getLector().getNombre(),
                prestamo.getEjemplar().getId(), prestamo.getEjemplar().getFolio(),
                prestamo.getEjemplar().getTitulo().getTitulo(), prestamo.getFechaSalida(),
                prestamo.getFechaLimite(), prestamo.getFechaDevolucion(), prestamo.getObservaciones());
    }
}
