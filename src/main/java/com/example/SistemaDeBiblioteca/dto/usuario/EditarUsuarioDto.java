package com.example.SistemaDeBiblioteca.dto.usuario;

import com.example.SistemaDeBiblioteca.model.enums.EstadoUsuario;
import com.example.SistemaDeBiblioteca.model.enums.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditarUsuarioDto {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede pasar de 100 caracteres")
    private String nombre;
    @NotNull(message = "El estado es obligatorio")
    private EstadoUsuario estado;
    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;
}
