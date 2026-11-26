package com.example.SistemaDeBiblioteca.dto.configuracion;

import com.example.SistemaDeBiblioteca.model.Configuracion;

public record ConfiguracionDto(int diasRetencion) {

    public static ConfiguracionDto de(Configuracion configuracion) {
        return new ConfiguracionDto(configuracion.getDiasRetencion());
    }
}
