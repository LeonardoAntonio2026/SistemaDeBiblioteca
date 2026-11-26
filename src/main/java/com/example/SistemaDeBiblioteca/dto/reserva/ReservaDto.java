package com.example.SistemaDeBiblioteca.dto.reserva;

import com.example.SistemaDeBiblioteca.model.Reserva;
import com.example.SistemaDeBiblioteca.model.enums.EstadoReserva;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservaDto(Long id, Long lectorId, String lector, Long tituloId, String titulo,
                         Long ejemplarId, String folioEjemplar, EstadoReserva estado,
                         LocalDateTime fechaHoraSolicitud, LocalDate fechaLimiteRecoleccion) {

    public static ReservaDto de(Reserva reserva) {
        // El ejemplar solo existe cuando la reserva ya está lista para recoger
        Long ejemplarId = null;
        String folioEjemplar = null;
        if (reserva.getEjemplar() != null) {
            ejemplarId = reserva.getEjemplar().getId();
            folioEjemplar = reserva.getEjemplar().getFolio();
        }

        return new ReservaDto(reserva.getId(), reserva.getLector().getId(), reserva.getLector().getNombre(),
                reserva.getTitulo().getId(), reserva.getTitulo().getTitulo(), ejemplarId, folioEjemplar,
                reserva.getEstado(), reserva.getFechaHoraSolicitud(), reserva.getFechaLimiteRecoleccion());
    }
}
