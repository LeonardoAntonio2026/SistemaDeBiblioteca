package com.example.SistemaDeBiblioteca.dto.usuario;

import com.example.SistemaDeBiblioteca.model.Usuario;
import com.example.SistemaDeBiblioteca.model.enums.EstadoUsuario;
import com.example.SistemaDeBiblioteca.model.enums.RolUsuario;

public record UsuarioDto(Long id, String nombre, RolUsuario rol, EstadoUsuario estado) {

    public static UsuarioDto de(Usuario usuario) {
        return new UsuarioDto(usuario.getId(), usuario.getNombre(), usuario.getRol(), usuario.getEstado());
    }
}
