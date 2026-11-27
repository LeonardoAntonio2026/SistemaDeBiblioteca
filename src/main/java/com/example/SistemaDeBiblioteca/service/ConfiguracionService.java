package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.configuracion.ConfiguracionDto;
import com.example.SistemaDeBiblioteca.dto.configuracion.EditarConfiguracionDto;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Configuracion;
import com.example.SistemaDeBiblioteca.repository.ConfiguracionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionService(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    @Transactional
    public ConfiguracionDto editarConfiguracion(EditarConfiguracionDto payload) {
        Configuracion configuracion = buscar();
        configuracion.setDiasRetencion(payload.getDiasRetencion());
        return ConfiguracionDto.de(configuracionRepository.save(configuracion));
    }

    @Transactional(readOnly = true)
    public ConfiguracionDto obtenerConfiguracion() {
        return ConfiguracionDto.de(buscar());
    }

    @Transactional(readOnly = true)
    public int diasRetencion() {
        return buscar().getDiasRetencion();
    }

    private Configuracion buscar() {
        return configuracionRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ReglaNegocioException("La configuración de la biblioteca no existe"));
    }
}
