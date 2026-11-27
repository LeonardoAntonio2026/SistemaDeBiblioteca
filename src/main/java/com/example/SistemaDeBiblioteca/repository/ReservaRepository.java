package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Reserva;
import com.example.SistemaDeBiblioteca.model.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findFirstByTituloIdAndEstadoOrderByFechaHoraSolicitudAsc(Long tituloId, EstadoReserva estado);

    List<Reserva> findByTituloIdAndEstadoOrderByFechaHoraSolicitudAsc(Long tituloId, EstadoReserva estado);

    Optional<Reserva> findFirstByEjemplarIdAndEstado(Long ejemplarId, EstadoReserva estado);

    List<Reserva> findByEstadoAndFechaLimiteRecoleccionBefore(EstadoReserva estado, LocalDate fecha);

    boolean existsByLectorIdAndTituloIdAndEstado(Long lectorId, Long tituloId, EstadoReserva estado);

    boolean existsByTituloIdAndEstado(Long tituloID, EstadoReserva estado);

}
