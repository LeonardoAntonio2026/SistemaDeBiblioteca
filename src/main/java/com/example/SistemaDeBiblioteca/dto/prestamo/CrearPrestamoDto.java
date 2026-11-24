package com.example.SistemaDeBiblioteca.dto.prestamo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearPrestamoDto {
    @NotNull(message = "El id del lector es obligatorio")
    private Long lectorId;
    @NotNull(message = "El id del ejemplar es obligatorio")
    private Long ejemplarId;
}
