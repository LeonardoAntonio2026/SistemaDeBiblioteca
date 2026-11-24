package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Lector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectorRepository extends JpaRepository<Lector, Long> {

    boolean existsByFolio(String folio);
}
