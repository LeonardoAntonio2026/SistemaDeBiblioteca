package com.example.SistemaDeBiblioteca.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NuevoUsuario {
    @NotBlank
    private String nombre;
    @NotBlank
    private String password;
}
