package com.example.SistemaDeBiblioteca.dto.lector;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearLectorDto {
    @NotBlank
    @Size(max = 50)
    private String folio;
    @NotBlank
    @Size(max = 150)
    private String nombre;
    @Size(max = 100)
    private String contacto;
    @NotNull
    private Integer tipoLectorId;
}
