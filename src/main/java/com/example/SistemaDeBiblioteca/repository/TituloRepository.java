package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TituloRepository extends JpaRepository<Titulo, Long> {

    boolean existsByIsbn(String isbn);
}
