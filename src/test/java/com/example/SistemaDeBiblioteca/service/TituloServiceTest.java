package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.titulo.CrearTituloDto;
import com.example.SistemaDeBiblioteca.dto.titulo.EditarTituloDto;
import com.example.SistemaDeBiblioteca.dto.titulo.TituloDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Titulo;
import com.example.SistemaDeBiblioteca.repository.TituloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TituloServiceTest {

    private TituloRepository repository;
    private TituloService service;

    @BeforeEach
    void preparar() {
        repository = mock(TituloRepository.class);
        service = new TituloService(repository);
        when(repository.save(any(Titulo.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crearTituloGuardaLosDatosDelLibro() {
        CrearTituloDto payload = new CrearTituloDto();
        payload.setTitulo("Cálculo I");
        payload.setAutor("James Stewart");
        payload.setIsbn("9786075228624");
        payload.setAnio(2018);

        TituloDto respuesta = service.crearTitulo(payload);

        assertThat(respuesta.titulo()).isEqualTo("Cálculo I");
        assertThat(respuesta.autor()).isEqualTo("James Stewart");
        assertThat(respuesta.isbn()).isEqualTo("9786075228624");
        assertThat(respuesta.anio()).isEqualTo(2018);
    }

    @Test
    void crearTituloRechazaIsbnDuplicado() {
        when(repository.existsByIsbn("9786075228624")).thenReturn(true);
        CrearTituloDto payload = new CrearTituloDto();
        payload.setTitulo("Cálculo I");
        payload.setIsbn("9786075228624");

        assertThatThrownBy(() -> service.crearTitulo(payload)).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearTituloConIsbnVacioLoGuardaSinIsbn() {
        CrearTituloDto payload = new CrearTituloDto();
        payload.setTitulo("Apuntes de física");
        payload.setIsbn("  ");

        TituloDto respuesta = service.crearTitulo(payload);

        assertThat(respuesta.isbn()).isNull();
        verify(repository, never()).existsByIsbn(anyString());
    }

    @Test
    void editarTituloInexistenteLanzaNoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        EditarTituloDto payload = new EditarTituloDto();
        payload.setTituloId(99L);
        payload.setTitulo("Cálculo I");

        assertThatThrownBy(() -> service.editarTitulo(payload)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void editarTituloConservandoSuIsbnNoLoMarcaDuplicado() {
        Titulo existente = new Titulo(1L, "Calculo", "Stewart", "9786075228624", null, 2018, null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        EditarTituloDto payload = new EditarTituloDto();
        payload.setTituloId(1L);
        payload.setTitulo("Cálculo I");
        payload.setIsbn("9786075228624");

        TituloDto respuesta = service.editarTitulo(payload);

        assertThat(respuesta.titulo()).isEqualTo("Cálculo I");
        verify(repository, never()).existsByIsbn(anyString());
    }
}
