package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.usuario.EditarUsuarioDto;
import com.example.SistemaDeBiblioteca.dto.usuario.NuevoUsuario;
import com.example.SistemaDeBiblioteca.dto.usuario.UsuarioDto;
import com.example.SistemaDeBiblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin("*")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto crearUsuario(@Valid @RequestBody NuevoUsuario dto) {
        return usuarioService.crearUsuario(dto);
    }

    @PutMapping("/editar")
    public UsuarioDto editarUsuario(@Valid @RequestBody EditarUsuarioDto dto) {
        return usuarioService.editarUsuario(dto);
    }

    @GetMapping
    public List<UsuarioDto> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioDto obtenerUsuario(@PathVariable Long id) {
        return usuarioService.obtenerUsuario(id);
    }
}
