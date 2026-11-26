package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.configuracion.ConfiguracionDto;
import com.example.SistemaDeBiblioteca.dto.configuracion.EditarConfiguracionDto;
import com.example.SistemaDeBiblioteca.service.ConfiguracionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuracion")
@CrossOrigin("*")
public class ConfiguracionController {
    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @PutMapping("/editar")
    public ConfiguracionDto editarConfiguracion(@Valid @RequestBody EditarConfiguracionDto dto) {
        return configuracionService.editarConfiguracion(dto);
    }

    @GetMapping
    public ConfiguracionDto obtenerConfiguracion() {
        return configuracionService.obtenerConfiguracion();
    }
}
