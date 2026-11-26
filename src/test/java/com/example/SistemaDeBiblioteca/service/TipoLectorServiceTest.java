package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.tipolector.EditarTipoLectorDto;
import com.example.SistemaDeBiblioteca.dto.tipolector.TipoLectorDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.model.TipoLector;
import com.example.SistemaDeBiblioteca.repository.TipoLectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TipoLectorServiceTest {

    private TipoLectorService service;
    private TipoLector alumno;

    @BeforeEach
    void preparar() {
        TipoLectorRepository repository = mock(TipoLectorRepository.class);
        service = new TipoLectorService(repository);

        alumno = new TipoLector(1, "alumno", 7, 3, 1, new BigDecimal("5.00"));
        when(repository.findById(1)).thenReturn(Optional.of(alumno));
        when(repository.save(any(TipoLector.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void editarTipoLectorCambiaSuPolitica() {
        EditarTipoLectorDto payload = new EditarTipoLectorDto(1, 10, 4, 2, new BigDecimal("8.00"));

        TipoLectorDto respuesta = service.editarTipoLector(payload);

        assertThat(respuesta.diasPrestamo()).isEqualTo(10);
        assertThat(respuesta.maxLibros()).isEqualTo(4);
        assertThat(respuesta.maxRenovaciones()).isEqualTo(2);
        assertThat(respuesta.multaPorDia()).isEqualByComparingTo("8.00");
        assertThat(respuesta.tipo()).isEqualTo("alumno");
    }

    @Test
    void editarTipoLectorQueNoExisteLanzaError() {
        EditarTipoLectorDto payload = new EditarTipoLectorDto(99, 10, 4, 2, new BigDecimal("8.00"));

        assertThatThrownBy(() -> service.editarTipoLector(payload)).isInstanceOf(RecursoNoEncontradoException.class);
    }
}
