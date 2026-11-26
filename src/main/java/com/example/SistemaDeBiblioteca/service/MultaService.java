package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.multa.MultaDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Multa;
import com.example.SistemaDeBiblioteca.model.enums.EstadoMulta;
import com.example.SistemaDeBiblioteca.repository.LectorRepository;
import com.example.SistemaDeBiblioteca.repository.MultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MultaService {

    private final MultaRepository multaRepository;
    private final LectorRepository lectorRepository;

    public MultaService(MultaRepository multaRepository, LectorRepository lectorRepository) {
        this.multaRepository = multaRepository;
        this.lectorRepository = lectorRepository;
    }

    // HU-BIB-08: el lector pagó la multa
    @Transactional
    public MultaDto pagarMulta(Long id) {
        return cambiarEstado(id, EstadoMulta.PAGADA);
    }

    // HU-BIB-08: la biblioteca le perdona la multa
    @Transactional
    public MultaDto condonarMulta(Long id) {
        return cambiarEstado(id, EstadoMulta.CONDONADA);
    }

    @Transactional(readOnly = true)
    public List<MultaDto> listarMultas() {
        return multaRepository.findAll().stream().map(MultaDto::de).toList();
    }

    // HU-ADM-04: multas que faltan por pagar
    @Transactional(readOnly = true)
    public List<MultaDto> listarPendientes() {
        return multaRepository.findByEstado(EstadoMulta.PENDIENTE).stream().map(MultaDto::de).toList();
    }

    @Transactional(readOnly = true)
    public List<MultaDto> multasDelLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new RecursoNoEncontradoException("Lector", lectorId);
        }
        return multaRepository.findByPrestamoLectorId(lectorId).stream().map(MultaDto::de).toList();
    }

    @Transactional(readOnly = true)
    public MultaDto obtenerMulta(Long id) {
        return MultaDto.de(buscar(id));
    }

    // Solo una multa pendiente se puede pagar o condonar
    private MultaDto cambiarEstado(Long id, EstadoMulta nuevoEstado) {
        Multa multa = buscar(id);

        if (multa.getEstado() != EstadoMulta.PENDIENTE) {
            throw new ReglaNegocioException("La multa ya no está pendiente");
        }

        multa.setEstado(nuevoEstado);
        return MultaDto.de(multaRepository.save(multa));
    }

    private Multa buscar(Long id) {
        return multaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Multa", id));
    }
}
