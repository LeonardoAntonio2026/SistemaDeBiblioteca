package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.titulo.CrearTituloDto;
import com.example.SistemaDeBiblioteca.dto.titulo.EditarTituloDto;
import com.example.SistemaDeBiblioteca.dto.titulo.TituloDto;
import com.example.SistemaDeBiblioteca.service.TituloService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/titulo")
public class TituloController {
    private final TituloService tituloService;

    public TituloController(TituloService tituloService) {
        this.tituloService = tituloService;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public TituloDto crearTitulo(@Valid @RequestBody CrearTituloDto dto) {
        return tituloService.crearTitulo(dto);
    }

    @PutMapping("/editar")
    public TituloDto editarTitulo(@Valid @RequestBody EditarTituloDto dto) {
        return tituloService.editarTitulo(dto);
    }

    @GetMapping
    public List<TituloDto> listarTitulos() {
        return tituloService.listarTitulos();
    }

    @GetMapping("/{id}")
    public TituloDto obtenerTitulo(@PathVariable Long id) {
        return tituloService.obtenerTitulo(id);
    }
}
