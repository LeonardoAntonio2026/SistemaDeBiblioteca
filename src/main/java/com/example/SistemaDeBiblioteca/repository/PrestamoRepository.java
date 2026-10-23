package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
