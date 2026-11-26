package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.multa.MultaDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Ejemplar;
import com.example.SistemaDeBiblioteca.model.Lector;
import com.example.SistemaDeBiblioteca.model.Multa;
import com.example.SistemaDeBiblioteca.model.Prestamo;
import com.example.SistemaDeBiblioteca.model.TipoLector;
import com.example.SistemaDeBiblioteca.model.Titulo;
import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;
import com.example.SistemaDeBiblioteca.model.enums.EstadoLector;
import com.example.SistemaDeBiblioteca.model.enums.EstadoMulta;
import com.example.SistemaDeBiblioteca.model.enums.MotivoMulta;
import com.example.SistemaDeBiblioteca.repository.LectorRepository;
import com.example.SistemaDeBiblioteca.repository.MultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MultaServiceTest {

    private MultaRepository repository;
    private LectorRepository lectorRepository;
    private MultaService service;
    private Multa multa;

    @BeforeEach
    void preparar() {
        repository = mock(MultaRepository.class);
        lectorRepository = mock(LectorRepository.class);
        service = new MultaService(repository, lectorRepository);

        TipoLector alumno = new TipoLector(1, "alumno", 7, 3, 1, new BigDecimal("5.00"));
        Titulo calculo = new Titulo(1L, "Cálculo I", "Stewart", null, null, 2018, null, null);
        Lector ana = new Lector(1L, "L-001", "Ana", null, EstadoLector.ACTIVO, alumno);
        Ejemplar ejemplar = new Ejemplar(1L, "EJ-001", "Anaquel A", "buena", EstadoEjemplar.DISPONIBLE, calculo);
        Prestamo prestamo = new Prestamo(10L, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3),
                LocalDate.now(), 0, null, ana, ejemplar);
        multa = new Multa(5L, new BigDecimal("15.00"), MotivoMulta.ATRASO, EstadoMulta.PENDIENTE, LocalDate.now(),
                prestamo);

        when(repository.findById(5L)).thenReturn(Optional.of(multa));
        when(repository.save(any(Multa.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void pagarMultaPendienteLaDejaPagada() {
        MultaDto respuesta = service.pagarMulta(5L);

        assertThat(respuesta.estado()).isEqualTo(EstadoMulta.PAGADA);
        assertThat(respuesta.lector()).isEqualTo("Ana");
    }

    @Test
    void condonarMultaPendienteLaDejaCondonada() {
        MultaDto respuesta = service.condonarMulta(5L);

        assertThat(respuesta.estado()).isEqualTo(EstadoMulta.CONDONADA);
    }

    @Test
    void pagarMultaYaPagadaLanzaError() {
        multa.setEstado(EstadoMulta.PAGADA);

        assertThatThrownBy(() -> service.pagarMulta(5L)).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void pagarMultaQueNoExisteLanzaError() {
        assertThatThrownBy(() -> service.pagarMulta(99L)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void listarPendientesSoloTraeLasPendientes() {
        when(repository.findByEstado(EstadoMulta.PENDIENTE)).thenReturn(List.of(multa));

        List<MultaDto> respuesta = service.listarPendientes();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).tipoLector()).isEqualTo("alumno");
    }

    @Test
    void multasDeUnLectorQueNoExisteLanzaError() {
        when(lectorRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.multasDelLector(99L)).isInstanceOf(RecursoNoEncontradoException.class);
    }
}
