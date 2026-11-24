package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.TipoLector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLectorRepository extends JpaRepository<TipoLector, Integer> {
}
