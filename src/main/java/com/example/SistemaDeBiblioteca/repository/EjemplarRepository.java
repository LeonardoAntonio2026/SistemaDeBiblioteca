package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    boolean existsByFolio(String folio);

    List<Ejemplar> findByTituloId(Long tituloId);
}
