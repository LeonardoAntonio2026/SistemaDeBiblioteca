package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {
}
