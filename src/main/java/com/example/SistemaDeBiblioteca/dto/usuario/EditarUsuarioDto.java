package com.example.SistemaDeBiblioteca.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditarUsuarioDto {
    @NotNull
    private Long usuarioId;
    @NotBlank
    private String nombre;
    @NotBlank
    private String estado;
    @NotBlank
    private String rol;
}
