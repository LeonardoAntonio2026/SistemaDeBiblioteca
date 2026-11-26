package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {

    Optional<Configuracion> findFirstByOrderByIdAsc();
}
