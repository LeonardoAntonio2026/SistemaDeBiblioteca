package com.example.SistemaDeBiblioteca.dto.lector;

import com.example.SistemaDeBiblioteca.model.enums.EstadoLector;
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
public class EditarLectorDto {
    @NotNull(message = "El ID del lector es obligatorio")
    private Long lectorId;
    @NotBlank(message = "El folio es obligatorio")
    @Size(max = 50, message = "El folio no puede pasar de 50 caracteres")
    private String folio;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede pasar de 150 caracteres")
    private String nombre;
    @Size(max = 100, message = "El contacto no puede exceder de 100 caracteres")
    private String contacto;
    @NotNull(message = "El tipo de lector es obligatorio")
    private Integer tipoLectorId;
    @NotNull(message = "El estado es obligatorio")
    private EstadoLector estado;
}
