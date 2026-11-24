package com.example.SistemaDeBiblioteca.dto.prestamo;

import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DevolverPrestamoDto {
    @NotNull(message = "El id del préstamo es obligatorio")
    private Long prestamoId;
    @NotNull(message = "El estado del ejemplar es obligatorio")
    private EstadoEjemplar estadoEjemplar;
    @Size(max = 255, message = "Las observaciones no pueden pasar de 255 caracteres")
    private String observaciones;
    @Min(value = 0, message = "El monto de la multa no puede ser negativo")
    private BigDecimal montoMulta;
}
