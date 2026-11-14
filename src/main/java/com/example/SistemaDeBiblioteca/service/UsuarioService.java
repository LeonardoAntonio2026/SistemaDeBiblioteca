package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.usuario.EditarUsuarioDto;
import com.example.SistemaDeBiblioteca.dto.usuario.NuevoUsuario;
import com.example.SistemaDeBiblioteca.dto.usuario.UsuarioDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Usuario;
import com.example.SistemaDeBiblioteca.model.enums.EstadoUsuario;
import com.example.SistemaDeBiblioteca.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioDto crearUsuario(NuevoUsuario payload) {
        if (usuarioRepository.existsByNombre(payload.getNombre())) {
            throw new ReglaNegocioException("Ya existe un usuario con el nombre '" + payload.getNombre() + "'");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(payload.getNombre());
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(payload.getPassword()));
        nuevoUsuario.setEstado(EstadoUsuario.ACTIVO);
        nuevoUsuario.setRol(payload.getRol());

        return UsuarioDto.de(usuarioRepository.save(nuevoUsuario));
    }

    @Transactional
    public UsuarioDto editarUsuario(EditarUsuarioDto payload) {
        Usuario usuario = buscar(payload.getUsuarioId());

        if (!usuario.getNombre().equals(payload.getNombre())
                && usuarioRepository.existsByNombre(payload.getNombre())) {
            throw new ReglaNegocioException("Ya existe un usuario con el nombre '" + payload.getNombre() + "'");
        }

        usuario.setEstado(payload.getEstado());
        usuario.setNombre(payload.getNombre());
        usuario.setRol(payload.getRol());

        return UsuarioDto.de(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(UsuarioDto::de).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDto obtenerUsuario(Long id) {
        return UsuarioDto.de(buscar(id));
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    }
}
