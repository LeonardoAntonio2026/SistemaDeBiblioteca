package com.example.SistemaDeBiblioteca.exception;

import com.example.SistemaDeBiblioteca.controller.UsuarioController;
import com.example.SistemaDeBiblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ManejadorErroresTest {

    private UsuarioService servicio;
    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        servicio = mock(UsuarioService.class);
        mvc = MockMvcBuilders.standaloneSetup(new UsuarioController(servicio))
                .setControllerAdvice(new ManejadorErrores())
                .build();
    }

    @Test
    void recursoInexistenteDevuelve404() throws Exception {
        when(servicio.obtenerUsuario(5L)).thenThrow(new RecursoNoEncontradoException("Usuario", 5L));

        mvc.perform(get("/usuario/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Usuario no encontrado: 5"));
    }

    @Test
    void reglaDeNegocioDevuelve422() throws Exception {
        when(servicio.crearUsuario(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new ReglaNegocioException("duplicado"));

        mvc.perform(post("/usuario/crear").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"ana\",\"password\":\"secreto123\",\"rol\":\"ADMINISTRADOR\"}"))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("duplicado"));
    }

    @Test
    void datosInvalidosDevuelven400ConDetalle() throws Exception {
        mvc.perform(post("/usuario/crear").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"password\":\"corta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles").isNotEmpty());
    }

    @Test
    void rolInexistenteDevuelve400() throws Exception {
        mvc.perform(post("/usuario/crear").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"ana\",\"password\":\"secreto123\",\"rol\":\"SUPERMAN\"}"))
                .andExpect(status().isBadRequest());
    }
}
