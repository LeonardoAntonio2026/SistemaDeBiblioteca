package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.usuario.EditarUsuarioDto;
import com.example.SistemaDeBiblioteca.dto.usuario.NuevoUsuario;
import com.example.SistemaDeBiblioteca.model.Usuario;
import com.example.SistemaDeBiblioteca.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario CrearUsuario(NuevoUsuario payload){
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(payload.getNombre());
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(payload.getPassword()));
        nuevoUsuario.setEstado("activo");
        nuevoUsuario.setRol("Usuario");

        return usuarioRepository.save(nuevoUsuario);
    }

    public Usuario EditarUsuario(EditarUsuarioDto payload){
        Usuario usuario = usuarioRepository.findById(payload.getUsuarioId()).orElseThrow();

        usuario.setEstado(payload.getEstado());
        usuario.setNombre(payload.getNombre());
        usuario.setRol(payload.getRol());

        return usuarioRepository.save(usuario);
    }
}
