package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.repository.TituloRepository;
import org.springframework.stereotype.Service;

@Service
public class TituloService {
    TituloRepository tituloRepository;

    public TituloService(TituloRepository tituloRepository) {
        this.tituloRepository = tituloRepository;
    }


}
