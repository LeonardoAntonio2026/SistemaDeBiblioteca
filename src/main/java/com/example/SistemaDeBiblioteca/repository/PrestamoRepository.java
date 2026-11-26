package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    long countByLectorIdAndFechaDevolucionIsNull(Long lectorId);

    List<Prestamo> findByFechaDevolucionIsNull();

    List<Prestamo> findByFechaDevolucionIsNullAndFechaLimiteBefore(LocalDate fecha);

    List<Prestamo> findByLectorIdOrderByFechaSalidaDesc(Long lectorId);

    Optional<Prestamo> findFirstByEjemplarIdAndFechaDevolucionIsNull(Long ejemplarId);
}
