package com.example.SistemaDeBiblioteca.dto.tipolector;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditarTipoLectorDto {
    @NotNull(message = "El id del tipo de lector es obligatorio")
    private Integer tipoLectorId;
    @Min(value = 1, message = "Los días de préstamo deben ser al menos 1")
    private int diasPrestamo;
    @Min(value = 1, message = "El máximo de libros debe ser al menos 1")
    private int maxLibros;
    @Min(value = 0, message = "El máximo de renovaciones no puede ser negativo")
    private int maxRenovaciones;
    @NotNull(message = "La multa por día es obligatoria")
    @Min(value = 0, message = "La multa por día no puede ser negativa")
    private BigDecimal multaPorDia;
}
