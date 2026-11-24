package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.titulo.CrearTituloDto;
import com.example.SistemaDeBiblioteca.dto.titulo.EditarTituloDto;
import com.example.SistemaDeBiblioteca.dto.titulo.TituloDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Titulo;
import com.example.SistemaDeBiblioteca.repository.TituloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TituloService {

    private final TituloRepository tituloRepository;

    public TituloService(TituloRepository tituloRepository) {
        this.tituloRepository = tituloRepository;
    }

    @Transactional
    public TituloDto crearTitulo(CrearTituloDto payload) {
        String isbn = normalizarIsbn(payload.getIsbn());
        if (isbn != null && tituloRepository.existsByIsbn(isbn)) {
            throw new ReglaNegocioException("Ya existe un título con el ISBN '" + isbn + "'");
        }

        Titulo nuevoTitulo = new Titulo();
        nuevoTitulo.setTitulo(payload.getTitulo());
        nuevoTitulo.setAutor(payload.getAutor());
        nuevoTitulo.setIsbn(isbn);
        nuevoTitulo.setEditorial(payload.getEditorial());
        nuevoTitulo.setAnio(payload.getAnio());
        nuevoTitulo.setCategoria(payload.getCategoria());
        nuevoTitulo.setEstadoCatalogo(payload.getEstadoCatalogo());

        return TituloDto.de(tituloRepository.save(nuevoTitulo));
    }

    @Transactional
    public TituloDto editarTitulo(EditarTituloDto payload) {
        Titulo titulo = buscar(payload.getTituloId());
        String isbn = normalizarIsbn(payload.getIsbn());

        if (isbn != null && !isbn.equals(titulo.getIsbn()) && tituloRepository.existsByIsbn(isbn)) {
            throw new ReglaNegocioException("Ya existe un título con el ISBN '" + isbn + "'");
        }

        titulo.setTitulo(payload.getTitulo());
        titulo.setAutor(payload.getAutor());
        titulo.setIsbn(isbn);
        titulo.setEditorial(payload.getEditorial());
        titulo.setAnio(payload.getAnio());
        titulo.setCategoria(payload.getCategoria());
        titulo.setEstadoCatalogo(payload.getEstadoCatalogo());

        return TituloDto.de(tituloRepository.save(titulo));
    }

    @Transactional(readOnly = true)
    public List<TituloDto> listarTitulos() {
        return tituloRepository.findAll().stream().map(TituloDto::de).toList();
    }

    @Transactional(readOnly = true)
    public TituloDto obtenerTitulo(Long id) {
        return TituloDto.de(buscar(id));
    }

    private Titulo buscar(Long id) {
        return tituloRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Título", id));
    }

    private static String normalizarIsbn(String isbn) {
        return isbn == null || isbn.isBlank() ? null : isbn.trim();
    }
}
