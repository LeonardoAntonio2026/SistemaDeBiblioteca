package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.ejemplar.CrearEjemplarDto;
import com.example.SistemaDeBiblioteca.dto.ejemplar.EditarEjemplarDto;
import com.example.SistemaDeBiblioteca.dto.ejemplar.EjemplarDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Ejemplar;
import com.example.SistemaDeBiblioteca.model.Titulo;
import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;
import com.example.SistemaDeBiblioteca.repository.EjemplarRepository;
import com.example.SistemaDeBiblioteca.repository.TituloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EjemplarService {

    private final EjemplarRepository ejemplarRepository;
    private final TituloRepository tituloRepository;

    public EjemplarService(EjemplarRepository ejemplarRepository, TituloRepository tituloRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.tituloRepository = tituloRepository;
    }

    @Transactional
    public EjemplarDto crearEjemplar(CrearEjemplarDto payload) {
        Titulo titulo = tituloRepository.findById(payload.getTituloId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Título", payload.getTituloId()));

        if (ejemplarRepository.existsByFolio(payload.getFolio())) {
            throw new ReglaNegocioException("Ya existe un ejemplar con el folio '" + payload.getFolio() + "'");
        }

        Ejemplar nuevoEjemplar = new Ejemplar();
        nuevoEjemplar.setFolio(payload.getFolio());
        nuevoEjemplar.setUbicacion(payload.getUbicacion());
        nuevoEjemplar.setCondicion(payload.getCondicion());
        nuevoEjemplar.setEstadoOperativo(EstadoEjemplar.DISPONIBLE);
        nuevoEjemplar.setTitulo(titulo);

        return EjemplarDto.de(ejemplarRepository.save(nuevoEjemplar));
    }

    @Transactional
    public EjemplarDto editarEjemplar(EditarEjemplarDto payload) {
        Ejemplar ejemplar = buscar(payload.getEjemplarId());

        if (!ejemplar.getFolio().equals(payload.getFolio())
                && ejemplarRepository.existsByFolio(payload.getFolio())) {
            throw new ReglaNegocioException("Ya existe un ejemplar con el folio '" + payload.getFolio() + "'");
        }

        if (ejemplar.getEstadoOperativo() != payload.getEstadoOperativo()
                && (loControlaPrestamoOReserva(ejemplar.getEstadoOperativo())
                || loControlaPrestamoOReserva(payload.getEstadoOperativo()))) {
            throw new ReglaNegocioException("Los estados prestado y retenido solo cambian con préstamos y reservas");
        }

        ejemplar.setFolio(payload.getFolio());
        ejemplar.setUbicacion(payload.getUbicacion());
        ejemplar.setCondicion(payload.getCondicion());
        ejemplar.setEstadoOperativo(payload.getEstadoOperativo());

        return EjemplarDto.de(ejemplarRepository.save(ejemplar));
    }

    @Transactional(readOnly = true)
    public List<EjemplarDto> listarEjemplares() {
        return ejemplarRepository.findAll().stream().map(EjemplarDto::de).toList();
    }

    @Transactional(readOnly = true)
    public List<EjemplarDto> listarPorTitulo(Long tituloId) {
        if (!tituloRepository.existsById(tituloId)) {
            throw new RecursoNoEncontradoException("Título", tituloId);
        }
        return ejemplarRepository.findByTituloId(tituloId).stream().map(EjemplarDto::de).toList();
    }

    @Transactional(readOnly = true)
    public EjemplarDto obtenerEjemplar(Long id) {
        return EjemplarDto.de(buscar(id));
    }

    private Ejemplar buscar(Long id) {
        return ejemplarRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ejemplar", id));
    }

    private static boolean loControlaPrestamoOReserva(EstadoEjemplar estado) {
        return estado == EstadoEjemplar.PRESTADO || estado == EstadoEjemplar.RETENIDO;
    }
}
