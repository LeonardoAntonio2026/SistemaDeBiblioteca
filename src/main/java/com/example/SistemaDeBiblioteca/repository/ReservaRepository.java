package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
