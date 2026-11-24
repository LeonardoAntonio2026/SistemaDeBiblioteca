package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Reserva;
import com.example.SistemaDeBiblioteca.model.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findFirstByTituloIdAndEstadoOrderByFechaHoraSolicitudAsc(Long tituloId, EstadoReserva estado);
}
