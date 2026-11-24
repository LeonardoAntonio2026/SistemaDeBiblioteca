package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.prestamo.CrearPrestamoDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.DevolucionDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.DevolverPrestamoDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.PrestamoDto;
import com.example.SistemaDeBiblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamo")
@CrossOrigin("*")
public class PrestamoController {
    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoDto crearPrestamo(@Valid @RequestBody CrearPrestamoDto dto) {
        return prestamoService.crearPrestamo(dto);
    }

    @PutMapping("/devolver")
    public DevolucionDto devolverPrestamo(@Valid @RequestBody DevolverPrestamoDto dto) {
        return prestamoService.devolverPrestamo(dto);
    }

    @GetMapping
    public List<PrestamoDto> listarPrestamos() {
        return prestamoService.listarPrestamos();
    }

    @GetMapping("/{id}")
    public PrestamoDto obtenerPrestamo(@PathVariable Long id) {
        return prestamoService.obtenerPrestamo(id);
    }
}
