package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.usuario.EditarUsuarioDto;
import com.example.SistemaDeBiblioteca.dto.usuario.NuevoUsuario;
import com.example.SistemaDeBiblioteca.model.Usuario;
import com.example.SistemaDeBiblioteca.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;


    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear")
    public Usuario CrearUsuario(@Valid @RequestBody NuevoUsuario dto){
        return usuarioService.CrearUsuario(dto);
    }

    @PutMapping("/editar")
    public Usuario EditarUsuario(@Valid @RequestBody EditarUsuarioDto dto){
        return usuarioService.EditarUsuario(dto);
    }
}
