package com.example.SistemaDeBiblioteca.dto.configuracion;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditarConfiguracionDto {
    @Min(value = 1, message = "Los días de retención deben ser al menos 1")
    private int diasRetencion;
}
