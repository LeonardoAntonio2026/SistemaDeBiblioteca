package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.configuracion.ConfiguracionDto;
import com.example.SistemaDeBiblioteca.dto.configuracion.EditarConfiguracionDto;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Configuracion;
import com.example.SistemaDeBiblioteca.repository.ConfiguracionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConfiguracionServiceTest {

    private ConfiguracionRepository repository;
    private ConfiguracionService service;

    @BeforeEach
    void preparar() {
        repository = mock(ConfiguracionRepository.class);
        service = new ConfiguracionService(repository);
        when(repository.save(any(Configuracion.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void editarConfiguracionCambiaLosDiasDeRetencion() {
        when(repository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(new Configuracion(1L, 3)));

        ConfiguracionDto respuesta = service.editarConfiguracion(new EditarConfiguracionDto(5));

        assertThat(respuesta.diasRetencion()).isEqualTo(5);
        assertThat(service.diasRetencion()).isEqualTo(5);
    }

    @Test
    void sinConfiguracionLanzaError() {
        when(repository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.diasRetencion()).isInstanceOf(ReglaNegocioException.class);
    }
}
