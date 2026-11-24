package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    long countByLectorIdAndFechaDevolucionIsNull(Long lectorId);
}
