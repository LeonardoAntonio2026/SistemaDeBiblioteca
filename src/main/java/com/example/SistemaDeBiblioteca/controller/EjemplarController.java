package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.ejemplar.CrearEjemplarDto;
import com.example.SistemaDeBiblioteca.dto.ejemplar.EditarEjemplarDto;
import com.example.SistemaDeBiblioteca.dto.ejemplar.EjemplarDto;
import com.example.SistemaDeBiblioteca.service.EjemplarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ejemplar")
@CrossOrigin("*")
public class EjemplarController {
    private final EjemplarService ejemplarService;

    public EjemplarController(EjemplarService ejemplarService) {
        this.ejemplarService = ejemplarService;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public EjemplarDto crearEjemplar(@Valid @RequestBody CrearEjemplarDto dto) {
        return ejemplarService.crearEjemplar(dto);
    }

    @PutMapping("/editar")
    public EjemplarDto editarEjemplar(@Valid @RequestBody EditarEjemplarDto dto) {
        return ejemplarService.editarEjemplar(dto);
    }

    @GetMapping
    public List<EjemplarDto> listarEjemplares() {
        return ejemplarService.listarEjemplares();
    }

    @GetMapping("/titulo/{tituloId}")
    public List<EjemplarDto> listarPorTitulo(@PathVariable Long tituloId) {
        return ejemplarService.listarPorTitulo(tituloId);
    }

    @GetMapping("/{id}")
    public EjemplarDto obtenerEjemplar(@PathVariable Long id) {
        return ejemplarService.obtenerEjemplar(id);
    }
}
