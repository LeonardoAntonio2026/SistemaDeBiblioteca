package com.example.SistemaDeBiblioteca.exception;

import java.util.List;

public record ErrorRespuesta(int estado, String mensaje, List<String> detalles) {

    public static ErrorRespuesta de(int estado, String mensaje) {
        return new ErrorRespuesta(estado, mensaje, List.of());
    }
}
