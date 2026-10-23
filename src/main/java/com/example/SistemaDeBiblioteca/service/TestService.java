package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public Usuario generarUsuario(String nombre){
        return new Usuario(3L,nombre, "admin", "irj", "activo");
    }
}
