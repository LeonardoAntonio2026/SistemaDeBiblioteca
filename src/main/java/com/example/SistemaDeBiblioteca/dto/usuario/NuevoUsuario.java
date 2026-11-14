package com.example.SistemaDeBiblioteca.dto.usuario;

import com.example.SistemaDeBiblioteca.model.enums.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NuevoUsuario {
    @NotBlank
    @Size(max = 100)
    private String nombre;
    @NotBlank
    @Size(min = 8, max = 72)
    private String password;
    @NotNull
    private RolUsuario rol;
}
