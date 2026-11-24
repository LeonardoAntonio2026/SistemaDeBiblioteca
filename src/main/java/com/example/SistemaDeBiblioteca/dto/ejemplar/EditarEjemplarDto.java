package com.example.SistemaDeBiblioteca.dto.ejemplar;

import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarEjemplarDto {
    @NotNull
    private Long ejemplarId;
    @NotBlank
    @Size(max = 50)
    private String folio;
    @NotBlank
    @Size(max = 255)
    private String ubicacion;
    @NotBlank
    @Size(max = 255)
    private String condicion;
    @NotNull
    private EstadoEjemplar estadoOperativo;
}
