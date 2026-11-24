package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    boolean existsByFolio(String folio);

    List<Ejemplar> findByTituloId(Long tituloId);
}
