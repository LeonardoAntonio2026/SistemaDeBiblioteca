package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TituloRepository extends JpaRepository<Titulo, Long> {
}
