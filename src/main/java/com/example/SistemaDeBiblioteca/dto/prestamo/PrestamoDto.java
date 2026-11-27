package com.example.SistemaDeBiblioteca.dto.prestamo;

import com.example.SistemaDeBiblioteca.model.Prestamo;

import java.time.LocalDate;

public record PrestamoDto(Long id, Long lectorId, String lector, String tipoLector, Long ejemplarId,
                          String folioEjemplar, String titulo, LocalDate fechaSalida, LocalDate fechaLimite,
                          LocalDate fechaDevolucion, int renovacionesRealizadas, String observaciones) {

    public static PrestamoDto de(Prestamo prestamo) {
        return new PrestamoDto(prestamo.getId(), prestamo.getLector().getId(), prestamo.getLector().getNombre(),
                prestamo.getLector().getTipoLector().getTipo(), prestamo.getEjemplar().getId(),
                prestamo.getEjemplar().getFolio(), prestamo.getEjemplar().getTitulo().getTitulo(),
                prestamo.getFechaSalida(), prestamo.getFechaLimite(), prestamo.getFechaDevolucion(),
                prestamo.getRenovacionesRealizadas(), prestamo.getObservaciones());
    }
}
