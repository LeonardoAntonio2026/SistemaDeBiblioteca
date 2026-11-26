package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.tipolector.EditarTipoLectorDto;
import com.example.SistemaDeBiblioteca.dto.tipolector.TipoLectorDto;
import com.example.SistemaDeBiblioteca.service.TipoLectorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipo-lector")
@CrossOrigin("*")
public class TipoLectorController {
    private final TipoLectorService tipoLectorService;

    public TipoLectorController(TipoLectorService tipoLectorService) {
        this.tipoLectorService = tipoLectorService;
    }

    @PutMapping("/editar")
    public TipoLectorDto editarTipoLector(@Valid @RequestBody EditarTipoLectorDto dto) {
        return tipoLectorService.editarTipoLector(dto);
    }

    @GetMapping
    public List<TipoLectorDto> listarTiposLector() {
        return tipoLectorService.listarTiposLector();
    }

    @GetMapping("/{id}")
    public TipoLectorDto obtenerTipoLector(@PathVariable Integer id) {
        return tipoLectorService.obtenerTipoLector(id);
    }
}
