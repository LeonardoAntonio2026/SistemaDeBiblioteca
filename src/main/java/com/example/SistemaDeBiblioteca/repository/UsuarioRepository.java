package com.example.SistemaDeBiblioteca.repository;

import com.example.SistemaDeBiblioteca.model.Usuario;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { }
