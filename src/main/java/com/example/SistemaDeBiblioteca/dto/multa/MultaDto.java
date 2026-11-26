package com.example.SistemaDeBiblioteca.dto.multa;

import com.example.SistemaDeBiblioteca.model.Multa;
import com.example.SistemaDeBiblioteca.model.enums.EstadoMulta;
import com.example.SistemaDeBiblioteca.model.enums.MotivoMulta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MultaDto(Long id, Long prestamoId, Long lectorId, String lector, String tipoLector,
                       String folioEjemplar, MotivoMulta motivo, BigDecimal monto, EstadoMulta estado,
                       LocalDate fechaGeneracion) {

    public static MultaDto de(Multa multa) {
        return new MultaDto(multa.getId(), multa.getPrestamo().getId(), multa.getPrestamo().getLector().getId(),
                multa.getPrestamo().getLector().getNombre(),
                multa.getPrestamo().getLector().getTipoLector().getTipo(),
                multa.getPrestamo().getEjemplar().getFolio(), multa.getMotivo(), multa.getMonto(),
                multa.getEstado(), multa.getFechaGeneracion());
    }
}
