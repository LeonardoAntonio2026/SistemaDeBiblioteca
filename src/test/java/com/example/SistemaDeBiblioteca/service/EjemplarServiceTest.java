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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EjemplarServiceTest {

    private EjemplarRepository repository;
    private TituloRepository tituloRepository;
    private EjemplarService service;
    private Titulo calculo;

    @BeforeEach
    void preparar() {
        repository = mock(EjemplarRepository.class);
        tituloRepository = mock(TituloRepository.class);
        service = new EjemplarService(repository, tituloRepository);
        calculo = new Titulo(1L, "Cálculo I", "Stewart", null, null, 2018, null, null);
        when(repository.save(any(Ejemplar.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crearEjemplarQuedaDisponibleYLigadoASuTitulo() {
        when(tituloRepository.findById(1L)).thenReturn(Optional.of(calculo));

        EjemplarDto respuesta = service.crearEjemplar(nuevo(1L, "EJ-001"));

        assertThat(respuesta.estadoOperativo()).isEqualTo(EstadoEjemplar.DISPONIBLE);
        assertThat(respuesta.tituloId()).isEqualTo(1L);
        assertThat(respuesta.titulo()).isEqualTo("Cálculo I");
    }

    @Test
    void crearEjemplarRechazaFolioDuplicado() {
        when(tituloRepository.findById(1L)).thenReturn(Optional.of(calculo));
        when(repository.existsByFolio("EJ-001")).thenReturn(true);

        assertThatThrownBy(() -> service.crearEjemplar(nuevo(1L, "EJ-001")))
                .isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearEjemplarDeTituloInexistenteLanzaNoEncontrado() {
        when(tituloRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crearEjemplar(nuevo(99L, "EJ-001")))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void editarEjemplarPermiteMandarloAReparacion() {
        Ejemplar existente = new Ejemplar(5L, "EJ-001", "Anaquel A", "buena", EstadoEjemplar.DISPONIBLE, calculo);
        when(repository.findById(5L)).thenReturn(Optional.of(existente));

        EjemplarDto respuesta = service.editarEjemplar(edicion(5L, EstadoEjemplar.EN_REPARACION));

        assertThat(respuesta.estadoOperativo()).isEqualTo(EstadoEjemplar.EN_REPARACION);
    }

    @Test
    void editarEjemplarNoPermiteMarcarloPrestadoAMano() {
        Ejemplar existente = new Ejemplar(5L, "EJ-001", "Anaquel A", "buena", EstadoEjemplar.DISPONIBLE, calculo);
        when(repository.findById(5L)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.editarEjemplar(edicion(5L, EstadoEjemplar.PRESTADO)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void editarEjemplarPrestadoNoPermiteCambiarSuEstado() {
        Ejemplar existente = new Ejemplar(5L, "EJ-001", "Anaquel A", "buena", EstadoEjemplar.PRESTADO, calculo);
        when(repository.findById(5L)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.editarEjemplar(edicion(5L, EstadoEjemplar.DISPONIBLE)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    private static CrearEjemplarDto nuevo(Long tituloId, String folio) {
        CrearEjemplarDto payload = new CrearEjemplarDto();
        payload.setTituloId(tituloId);
        payload.setFolio(folio);
        payload.setUbicacion("Anaquel A");
        payload.setCondicion("buena");
        return payload;
    }

    private static EditarEjemplarDto edicion(Long ejemplarId, EstadoEjemplar estado) {
        EditarEjemplarDto payload = new EditarEjemplarDto();
        payload.setEjemplarId(ejemplarId);
        payload.setFolio("EJ-001");
        payload.setUbicacion("Anaquel A");
        payload.setCondicion("buena");
        payload.setEstadoOperativo(estado);
        return payload;
    }
}
