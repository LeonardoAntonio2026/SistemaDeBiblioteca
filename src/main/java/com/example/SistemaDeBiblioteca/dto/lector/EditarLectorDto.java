package com.example.SistemaDeBiblioteca.dto.lector;

import com.example.SistemaDeBiblioteca.model.enums.EstadoLector;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarLectorDto {
    @NotNull
    private Long lectorId;
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
    @NotNull
    private EstadoLector estado;
}
