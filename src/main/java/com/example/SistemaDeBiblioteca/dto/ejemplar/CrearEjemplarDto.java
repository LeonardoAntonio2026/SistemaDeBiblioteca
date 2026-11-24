package com.example.SistemaDeBiblioteca.dto.ejemplar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearEjemplarDto {
    @NotNull
    private Long tituloId;
    @NotBlank
    @Size(max = 50)
    private String folio;
    @NotBlank
    @Size(max = 255)
    private String ubicacion;
    @NotBlank
    @Size(max = 255)
    private String condicion;
}
