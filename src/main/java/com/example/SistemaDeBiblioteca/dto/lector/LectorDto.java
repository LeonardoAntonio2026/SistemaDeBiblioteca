package com.example.SistemaDeBiblioteca.dto.lector;

import com.example.SistemaDeBiblioteca.model.Lector;
import com.example.SistemaDeBiblioteca.model.enums.EstadoLector;

public record LectorDto(Long id, String folio, String nombre, String contacto, EstadoLector estado,
                        Integer tipoLectorId, String tipoLector) {

    public static LectorDto de(Lector lector) {
        return new LectorDto(lector.getId(), lector.getFolio(), lector.getNombre(), lector.getContacto(),
                lector.getEstado(), lector.getTipoLector().getId(), lector.getTipoLector().getTipo());
    }
}
