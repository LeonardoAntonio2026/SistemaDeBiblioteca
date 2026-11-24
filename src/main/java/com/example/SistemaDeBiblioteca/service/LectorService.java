package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.lector.CrearLectorDto;
import com.example.SistemaDeBiblioteca.dto.lector.EditarLectorDto;
import com.example.SistemaDeBiblioteca.dto.lector.LectorDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Lector;
import com.example.SistemaDeBiblioteca.model.TipoLector;
import com.example.SistemaDeBiblioteca.model.enums.EstadoLector;
import com.example.SistemaDeBiblioteca.repository.LectorRepository;
import com.example.SistemaDeBiblioteca.repository.TipoLectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LectorService {

    private final LectorRepository lectorRepository;
    private final TipoLectorRepository tipoLectorRepository;

    public LectorService(LectorRepository lectorRepository, TipoLectorRepository tipoLectorRepository) {
        this.lectorRepository = lectorRepository;
        this.tipoLectorRepository = tipoLectorRepository;
    }

    @Transactional
    public LectorDto crearLector(CrearLectorDto payload) {
        TipoLector tipo = buscarTipo(payload.getTipoLectorId());

        if (lectorRepository.existsByFolio(payload.getFolio())) {
            throw new ReglaNegocioException("Ya existe un lector con el folio '" + payload.getFolio() + "'");
        }

        Lector nuevoLector = new Lector();
        nuevoLector.setFolio(payload.getFolio());
        nuevoLector.setNombre(payload.getNombre());
        nuevoLector.setContacto(payload.getContacto());
        nuevoLector.setEstado(EstadoLector.ACTIVO);
        nuevoLector.setTipoLector(tipo);

        return LectorDto.de(lectorRepository.save(nuevoLector));
    }

    @Transactional
    public LectorDto editarLector(EditarLectorDto payload) {
        Lector lector = buscar(payload.getLectorId());
        TipoLector tipo = buscarTipo(payload.getTipoLectorId());

        if (!lector.getFolio().equals(payload.getFolio())
                && lectorRepository.existsByFolio(payload.getFolio())) {
            throw new ReglaNegocioException("Ya existe un lector con el folio '" + payload.getFolio() + "'");
        }

        lector.setFolio(payload.getFolio());
        lector.setNombre(payload.getNombre());
        lector.setContacto(payload.getContacto());
        lector.setEstado(payload.getEstado());
        lector.setTipoLector(tipo);

        return LectorDto.de(lectorRepository.save(lector));
    }

    @Transactional(readOnly = true)
    public List<LectorDto> listarLectores() {
        return lectorRepository.findAll().stream().map(LectorDto::de).toList();
    }

    @Transactional(readOnly = true)
    public LectorDto obtenerLector(Long id) {
        return LectorDto.de(buscar(id));
    }

    private Lector buscar(Long id) {
        return lectorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Lector", id));
    }

    private TipoLector buscarTipo(Integer id) {
        return tipoLectorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de lector", id));
    }
}
