package com.example.SistemaDeBiblioteca.dto.usuario;

import com.example.SistemaDeBiblioteca.model.enums.EstadoUsuario;
import com.example.SistemaDeBiblioteca.model.enums.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarUsuarioDto {
    @NotNull
    private Long usuarioId;
    @NotBlank
    @Size(max = 100)
    private String nombre;
    @NotNull
    private EstadoUsuario estado;
    @NotNull
    private RolUsuario rol;
}
