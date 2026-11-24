package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.lector.CrearLectorDto;
import com.example.SistemaDeBiblioteca.dto.lector.EditarLectorDto;
import com.example.SistemaDeBiblioteca.dto.lector.LectorDto;
import com.example.SistemaDeBiblioteca.service.LectorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lector")
public class LectorController {
    private final LectorService lectorService;

    public LectorController(LectorService lectorService) {
        this.lectorService = lectorService;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public LectorDto crearLector(@Valid @RequestBody CrearLectorDto dto) {
        return lectorService.crearLector(dto);
    }

    @PutMapping("/editar")
    public LectorDto editarLector(@Valid @RequestBody EditarLectorDto dto) {
        return lectorService.editarLector(dto);
    }

    @GetMapping
    public List<LectorDto> listarLectores() {
        return lectorService.listarLectores();
    }

    @GetMapping("/{id}")
    public LectorDto obtenerLector(@PathVariable Long id) {
        return lectorService.obtenerLector(id);
    }
}
