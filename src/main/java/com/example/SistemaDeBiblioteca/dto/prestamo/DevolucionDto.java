package com.example.SistemaDeBiblioteca.dto.prestamo;

import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;

import java.math.BigDecimal;

public record DevolucionDto(PrestamoDto prestamo, EstadoEjemplar estadoEjemplar, BigDecimal multaGenerada) {
}
