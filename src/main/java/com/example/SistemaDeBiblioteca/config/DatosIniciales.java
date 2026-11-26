package com.example.SistemaDeBiblioteca.config;

import com.example.SistemaDeBiblioteca.model.Configuracion;
import com.example.SistemaDeBiblioteca.model.TipoLector;
import com.example.SistemaDeBiblioteca.model.Usuario;
import com.example.SistemaDeBiblioteca.model.enums.EstadoUsuario;
import com.example.SistemaDeBiblioteca.model.enums.RolUsuario;
import com.example.SistemaDeBiblioteca.repository.ConfiguracionRepository;
import com.example.SistemaDeBiblioteca.repository.TipoLectorRepository;
import com.example.SistemaDeBiblioteca.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class DatosIniciales implements ApplicationRunner {

    private final TipoLectorRepository tipoLectorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConfiguracionRepository configuracionRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminNombre;
    private final String adminPassword;
    private final int diasRetencion;

    public DatosIniciales(TipoLectorRepository tipoLectorRepository,
                          UsuarioRepository usuarioRepository,
                          ConfiguracionRepository configuracionRepository,
                          PasswordEncoder passwordEncoder,
                          @Value("${biblioteca.admin.nombre:admin}") String adminNombre,
                          @Value("${biblioteca.admin.password:}") String adminPassword,
                          @Value("${biblioteca.reserva.dias-retencion:3}") int diasRetencion) {
        this.tipoLectorRepository = tipoLectorRepository;
        this.usuarioRepository = usuarioRepository;
        this.configuracionRepository = configuracionRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminNombre = adminNombre;
        this.adminPassword = adminPassword;
        this.diasRetencion = diasRetencion;
    }

    @Override
    public void run(ApplicationArguments args) {
        cargarTiposDeLector();
        crearConfiguracion();
        crearPrimerAdministrador();
    }

    /** Plazo de retención de partida; el administrador lo cambia después (HU-ADM-03). */
    private void crearConfiguracion() {
        if (configuracionRepository.count() > 0) {
            return;
        }
        configuracionRepository.save(new Configuracion(null, diasRetencion));
        log.info("Configuración inicial creada ({} días de retención)", diasRetencion);
    }

    /** Valores de partida; el administrador los ajusta después (HU-ADM-02). */
    private void cargarTiposDeLector() {
        if (tipoLectorRepository.count() > 0) {
            return;
        }
        tipoLectorRepository.save(tipo("alumno", 7, 3, 1, "5.00"));
        tipoLectorRepository.save(tipo("docente", 15, 5, 2, "5.00"));
        tipoLectorRepository.save(tipo("externo", 5, 2, 0, "10.00"));
        log.info("Tipos de lector iniciales creados (alumno, docente, externo)");
    }

    private void crearPrimerAdministrador() {
        if (usuarioRepository.count() > 0) {
            return;
        }
        if (adminPassword.isBlank()) {
            log.warn("No hay usuarios y no se definió biblioteca.admin.password (variable ADMIN_PASSWORD): "
                    + "no se creó el administrador inicial");
            return;
        }
        Usuario admin = new Usuario();
        admin.setNombre(adminNombre);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRol(RolUsuario.ADMINISTRADOR);
        admin.setEstado(EstadoUsuario.ACTIVO);
        usuarioRepository.save(admin);
        log.info("Administrador inicial '{}' creado", adminNombre);
    }

    private static TipoLector tipo(String nombre, int dias, int maxLibros, int maxRenovaciones, String multaPorDia) {
        return new TipoLector(null, nombre, dias, maxLibros, maxRenovaciones, new BigDecimal(multaPorDia));
    }
}
