package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.usuario.EditarUsuarioDto;
import com.example.SistemaDeBiblioteca.dto.usuario.NuevoUsuario;
import com.example.SistemaDeBiblioteca.dto.usuario.UsuarioDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Usuario;
import com.example.SistemaDeBiblioteca.model.enums.EstadoUsuario;
import com.example.SistemaDeBiblioteca.model.enums.RolUsuario;
import com.example.SistemaDeBiblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    private UsuarioRepository repository;
    private PasswordEncoder encoder;
    private UsuarioService service;

    @BeforeEach
    void preparar() {
        repository = mock(UsuarioRepository.class);
        encoder = new BCryptPasswordEncoder();
        service = new UsuarioService(repository, encoder);
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crearUsuarioGuardaHashActivoYConElRolPedido() {
        NuevoUsuario payload = new NuevoUsuario();
        payload.setNombre("ana");
        payload.setPassword("secreto123");
        payload.setRol(RolUsuario.BIBLIOTECARIO);

        UsuarioDto respuesta = service.crearUsuario(payload);

        ArgumentCaptor<Usuario> guardado = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(guardado.capture());
        assertThat(guardado.getValue().getPasswordHash()).isNotEqualTo("secreto123");
        assertThat(encoder.matches("secreto123", guardado.getValue().getPasswordHash())).isTrue();
        assertThat(respuesta.estado()).isEqualTo(EstadoUsuario.ACTIVO);
        assertThat(respuesta.rol()).isEqualTo(RolUsuario.BIBLIOTECARIO);
    }

    @Test
    void crearUsuarioRechazaNombreDuplicado() {
        when(repository.existsByNombre("ana")).thenReturn(true);
        NuevoUsuario payload = new NuevoUsuario();
        payload.setNombre("ana");
        payload.setPassword("secreto123");
        payload.setRol(RolUsuario.ADMINISTRADOR);

        assertThatThrownBy(() -> service.crearUsuario(payload)).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void editarUsuarioInexistenteLanzaNoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        EditarUsuarioDto payload = new EditarUsuarioDto();
        payload.setUsuarioId(99L);
        payload.setNombre("ana");
        payload.setEstado(EstadoUsuario.ACTIVO);
        payload.setRol(RolUsuario.ADMINISTRADOR);

        assertThatThrownBy(() -> service.editarUsuario(payload)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void editarUsuarioPermiteDesactivar() {
        Usuario existente = new Usuario(1L, "ana", RolUsuario.BIBLIOTECARIO, "hash", EstadoUsuario.ACTIVO);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        EditarUsuarioDto payload = new EditarUsuarioDto();
        payload.setUsuarioId(1L);
        payload.setNombre("ana");
        payload.setEstado(EstadoUsuario.DESACTIVADO);
        payload.setRol(RolUsuario.BIBLIOTECARIO);

        UsuarioDto respuesta = service.editarUsuario(payload);

        assertThat(respuesta.estado()).isEqualTo(EstadoUsuario.DESACTIVADO);
    }
}
