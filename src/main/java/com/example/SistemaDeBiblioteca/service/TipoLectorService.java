package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.tipolector.EditarTipoLectorDto;
import com.example.SistemaDeBiblioteca.dto.tipolector.TipoLectorDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.model.TipoLector;
import com.example.SistemaDeBiblioteca.repository.TipoLectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoLectorService {

    private final TipoLectorRepository tipoLectorRepository;

    public TipoLectorService(TipoLectorRepository tipoLectorRepository) {
        this.tipoLectorRepository = tipoLectorRepository;
    }

    // HU-ADM-02: cambiar la política de un tipo de lector (alumno, docente o externo)
    @Transactional
    public TipoLectorDto editarTipoLector(EditarTipoLectorDto payload) {
        TipoLector tipo = buscar(payload.getTipoLectorId());

        tipo.setDiasPrestamo(payload.getDiasPrestamo());
        tipo.setMaxLibros(payload.getMaxLibros());
        tipo.setMaxRenovaciones(payload.getMaxRenovaciones());
        tipo.setMultaPorDia(payload.getMultaPorDia());

        return TipoLectorDto.de(tipoLectorRepository.save(tipo));
    }

    @Transactional(readOnly = true)
    public List<TipoLectorDto> listarTiposLector() {
        return tipoLectorRepository.findAll().stream().map(TipoLectorDto::de).toList();
    }

    @Transactional(readOnly = true)
    public TipoLectorDto obtenerTipoLector(Integer id) {
        return TipoLectorDto.de(buscar(id));
    }

    private TipoLector buscar(Integer id) {
        return tipoLectorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de lector", id));
    }
}
