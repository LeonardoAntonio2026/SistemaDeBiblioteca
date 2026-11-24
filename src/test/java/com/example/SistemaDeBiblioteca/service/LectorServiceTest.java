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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LectorServiceTest {

    private LectorRepository repository;
    private TipoLectorRepository tipoLectorRepository;
    private LectorService service;
    private TipoLector alumno;
    private TipoLector docente;

    @BeforeEach
    void preparar() {
        repository = mock(LectorRepository.class);
        tipoLectorRepository = mock(TipoLectorRepository.class);
        service = new LectorService(repository, tipoLectorRepository);
        alumno = new TipoLector(1, "alumno", 7, 3, 1, new BigDecimal("5.00"));
        docente = new TipoLector(2, "docente", 15, 5, 2, new BigDecimal("5.00"));
        when(tipoLectorRepository.findById(1)).thenReturn(Optional.of(alumno));
        when(tipoLectorRepository.findById(2)).thenReturn(Optional.of(docente));
        when(repository.save(any(Lector.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crearLectorQuedaActivoConSuTipo() {
        LectorDto respuesta = service.crearLector(nuevo("L-001", 1));

        assertThat(respuesta.estado()).isEqualTo(EstadoLector.ACTIVO);
        assertThat(respuesta.tipoLector()).isEqualTo("alumno");
    }

    @Test
    void crearLectorRechazaFolioDuplicado() {
        when(repository.existsByFolio("L-001")).thenReturn(true);

        assertThatThrownBy(() -> service.crearLector(nuevo("L-001", 1)))
                .isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearLectorConTipoInexistenteLanzaNoEncontrado() {
        when(tipoLectorRepository.findById(9)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crearLector(nuevo("L-001", 9)))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void editarLectorPermiteSuspenderYCambiarDeTipo() {
        Lector existente = new Lector(3L, "L-001", "Ana", null, EstadoLector.ACTIVO, alumno);
        when(repository.findById(3L)).thenReturn(Optional.of(existente));
        EditarLectorDto payload = new EditarLectorDto();
        payload.setLectorId(3L);
        payload.setFolio("L-001");
        payload.setNombre("Ana");
        payload.setTipoLectorId(2);
        payload.setEstado(EstadoLector.SUSPENDIDO);

        LectorDto respuesta = service.editarLector(payload);

        assertThat(respuesta.estado()).isEqualTo(EstadoLector.SUSPENDIDO);
        assertThat(respuesta.tipoLector()).isEqualTo("docente");
    }

    private static CrearLectorDto nuevo(String folio, Integer tipoLectorId) {
        CrearLectorDto payload = new CrearLectorDto();
        payload.setFolio(folio);
        payload.setNombre("Ana");
        payload.setContacto("ana@correo.com");
        payload.setTipoLectorId(tipoLectorId);
        return payload;
    }
}
