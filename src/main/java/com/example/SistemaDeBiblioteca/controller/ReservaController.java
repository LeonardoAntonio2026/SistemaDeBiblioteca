package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.dto.prestamo.PrestamoDto;
import com.example.SistemaDeBiblioteca.dto.reserva.CrearReservaDto;
import com.example.SistemaDeBiblioteca.dto.reserva.ReservaDto;
import com.example.SistemaDeBiblioteca.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserva")
@CrossOrigin("*")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaDto crearReserva(@Valid @RequestBody CrearReservaDto dto) {
        return reservaService.crearReserva(dto);
    }

    @PutMapping("/cancelar/{id}")
    public ReservaDto cancelarReserva(@PathVariable Long id) {
        return reservaService.cancelarReserva(id);
    }

    @PostMapping("/entregar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoDto entregarReserva(@PathVariable Long id) {
        return reservaService.entregarReserva(id);
    }

    @PutMapping("/caducar")
    public List<ReservaDto> caducarReservas() {
        return reservaService.caducarReservas();
    }

    @GetMapping
    public List<ReservaDto> listarReservas() {
        return reservaService.listarReservas();
    }

    @GetMapping("/titulo/{tituloId}")
    public List<ReservaDto> colaDelTitulo(@PathVariable Long tituloId) {
        return reservaService.colaDelTitulo(tituloId);
    }

    @GetMapping("/{id}")
    public ReservaDto obtenerReserva(@PathVariable Long id) {
        return reservaService.obtenerReserva(id);
    }
}
