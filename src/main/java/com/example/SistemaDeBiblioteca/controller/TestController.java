package com.example.SistemaDeBiblioteca.controller;

import com.example.SistemaDeBiblioteca.model.Usuario;
import com.example.SistemaDeBiblioteca.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final TestService testService;

    public TestController(TestService testService){
        this.testService = testService;
    }

    @GetMapping("/test")
    public Usuario test(@RequestParam(value = "nombre", defaultValue = "leo") String nombre){
        return testService.generarUsuario(nombre);
    }
}
