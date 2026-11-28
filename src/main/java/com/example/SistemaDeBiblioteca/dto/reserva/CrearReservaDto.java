package com.example.SistemaDeBiblioteca.dto.reserva;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearReservaDto {
    @NotNull(message = "El ID del lector es obligatorio")
    private Long lectorId;
    @NotNull(message = "El ID del título es obligatorio")
    private Long tituloId;
}
