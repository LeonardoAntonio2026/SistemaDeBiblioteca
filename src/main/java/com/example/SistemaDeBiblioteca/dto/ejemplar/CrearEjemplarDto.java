package com.example.SistemaDeBiblioteca.dto.ejemplar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearEjemplarDto {
    @NotNull(message = "El id del título es obligatorio")
    private Long tituloId;
    @NotBlank(message = "El folio es obligatorio")
    @Size(max = 50, message = "El folio no puede pasar de 50 caracteres")
    private String folio;
    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 255, message = "La ubicación no puede pasar de 255 caracteres")
    private String ubicacion;
    @NotBlank(message = "La condición es obligatoria")
    @Size(max = 255, message = "La condición no puede pasar de 255 caracteres")
    private String condicion;
}
