package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.multa.MultaDto;
import com.example.SistemaDeBiblioteca.service.MultaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/multa")
@CrossOrigin("*")
public class MultaController {
    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    @PutMapping("/pagar/{id}")
    public MultaDto pagarMulta(@PathVariable Long id) {
        return multaService.pagarMulta(id);
    }

    @PutMapping("/condonar/{id}")
    public MultaDto condonarMulta(@PathVariable Long id) {
        return multaService.condonarMulta(id);
    }

    @GetMapping
    public List<MultaDto> listarMultas() {
        return multaService.listarMultas();
    }

    @GetMapping("/pendientes")
    public List<MultaDto> listarPendientes() {
        return multaService.listarPendientes();
    }

    @GetMapping("/lector/{lectorId}")
    public List<MultaDto> multasDelLector(@PathVariable Long lectorId) {
        return multaService.multasDelLector(lectorId);
    }

    @GetMapping("/{id}")
    public MultaDto obtenerMulta(@PathVariable Long id) {
        return multaService.obtenerMulta(id);
    }
}
