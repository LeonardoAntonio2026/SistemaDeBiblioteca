package com.example.SistemaDeBiblioteca.dto.tipolector;

import com.example.SistemaDeBiblioteca.model.TipoLector;

import java.math.BigDecimal;

public record TipoLectorDto(Integer id, String tipo, int diasPrestamo, int maxLibros, int maxRenovaciones,
                            BigDecimal multaPorDia) {

    public static TipoLectorDto de(TipoLector tipo) {
        return new TipoLectorDto(tipo.getId(), tipo.getTipo(), tipo.getDiasPrestamo(), tipo.getMaxLibros(),
                tipo.getMaxRenovaciones(), tipo.getMultaPorDia());
    }
}
