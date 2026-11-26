package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Multa;
import com.example.SistemaDeBiblioteca.model.enums.EstadoMulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

    boolean existsByPrestamoLectorIdAndEstado(Long lectorId, EstadoMulta estado);

    List<Multa> findByEstado(EstadoMulta estado);

    List<Multa> findByPrestamoLectorId(Long lectorId);
}
