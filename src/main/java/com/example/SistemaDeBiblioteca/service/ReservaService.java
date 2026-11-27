package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.prestamo.CrearPrestamoDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.PrestamoDto;
import com.example.SistemaDeBiblioteca.dto.reserva.CrearReservaDto;
import com.example.SistemaDeBiblioteca.dto.reserva.ReservaDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Ejemplar;
import com.example.SistemaDeBiblioteca.model.Lector;
import com.example.SistemaDeBiblioteca.model.Reserva;
import com.example.SistemaDeBiblioteca.model.Titulo;
import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;
import com.example.SistemaDeBiblioteca.model.enums.EstadoLector;
import com.example.SistemaDeBiblioteca.model.enums.EstadoMulta;
import com.example.SistemaDeBiblioteca.model.enums.EstadoReserva;
import com.example.SistemaDeBiblioteca.repository.EjemplarRepository;
import com.example.SistemaDeBiblioteca.repository.LectorRepository;
import com.example.SistemaDeBiblioteca.repository.MultaRepository;
import com.example.SistemaDeBiblioteca.repository.ReservaRepository;
import com.example.SistemaDeBiblioteca.repository.TituloRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LectorRepository lectorRepository;
    private final TituloRepository tituloRepository;
    private final EjemplarRepository ejemplarRepository;
    private final MultaRepository multaRepository;
    private final PrestamoService prestamoService;
    private final ConfiguracionService configuracionService;

    public ReservaService(ReservaRepository reservaRepository,
                          LectorRepository lectorRepository,
                          TituloRepository tituloRepository,
                          EjemplarRepository ejemplarRepository,
                          MultaRepository multaRepository,
                          PrestamoService prestamoService,
                          ConfiguracionService configuracionService) {
        this.reservaRepository = reservaRepository;
        this.lectorRepository = lectorRepository;
        this.tituloRepository = tituloRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.multaRepository = multaRepository;
        this.prestamoService = prestamoService;
        this.configuracionService = configuracionService;
    }

    @Transactional
    public ReservaDto crearReserva(CrearReservaDto payload) {
        Lector lector = lectorRepository.findById(payload.getLectorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Lector", payload.getLectorId()));
        Titulo titulo = tituloRepository.findById(payload.getTituloId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Título", payload.getTituloId()));

        if (lector.getEstado() == EstadoLector.SUSPENDIDO) {
            throw new ReglaNegocioException("El lector está suspendido");
        }
        if (multaRepository.existsByPrestamoLectorIdAndEstado(lector.getId(), EstadoMulta.PENDIENTE)) {
            throw new ReglaNegocioException("El lector tiene multas pendientes");
        }
        if (ejemplarRepository.existsByTituloIdAndEstadoOperativo(titulo.getId(), EstadoEjemplar.DISPONIBLE)) {
            throw new ReglaNegocioException("El título tiene ejemplares disponibles, no hace falta reservar");
        }
        if (reservaRepository.existsByLectorIdAndTituloIdAndEstado(lector.getId(), titulo.getId(), EstadoReserva.EN_COLA)
                || reservaRepository.existsByLectorIdAndTituloIdAndEstado(lector.getId(), titulo.getId(),
                EstadoReserva.LISTA_PARA_RECOGER)) {
            throw new ReglaNegocioException("El lector ya tiene una reserva activa de este título");
        }

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setLector(lector);
        nuevaReserva.setTitulo(titulo);
        nuevaReserva.setEstado(EstadoReserva.EN_COLA);
        nuevaReserva.setFechaHoraSolicitud(LocalDateTime.now());

        return ReservaDto.de(reservaRepository.save(nuevaReserva));
    }

    @Transactional
    public ReservaDto cancelarReserva(Long id) {
        Reserva reserva = buscar(id);

        if (reserva.getEstado() != EstadoReserva.EN_COLA && reserva.getEstado() != EstadoReserva.LISTA_PARA_RECOGER) {
            throw new ReglaNegocioException("Solo se puede cancelar una reserva en cola o lista para recoger");
        }

        boolean teniaEjemplar = reserva.getEstado() == EstadoReserva.LISTA_PARA_RECOGER;
        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);

        if (teniaEjemplar) {
            pasarAlSiguiente(reserva.getEjemplar());
        }

        return ReservaDto.de(reserva);
    }

    @Transactional
    public PrestamoDto entregarReserva(Long id) {
        Reserva reserva = buscar(id);

        if (reserva.getEstado() != EstadoReserva.LISTA_PARA_RECOGER) {
            throw new ReglaNegocioException("La reserva no está lista para recoger");
        }

        CrearPrestamoDto prestamo = new CrearPrestamoDto(reserva.getLector().getId(), reserva.getEjemplar().getId());
        return prestamoService.crearPrestamo(prestamo);
    }

    @Transactional
    public List<ReservaDto> caducarReservas() {
        List<Reserva> vencidas = reservaRepository.findByEstadoAndFechaLimiteRecoleccionBefore(
                EstadoReserva.LISTA_PARA_RECOGER, LocalDate.now());

        List<ReservaDto> canceladas = new ArrayList<>();
        for (Reserva reserva : vencidas) {
            reserva.setEstado(EstadoReserva.CANCELADA);
            reservaRepository.save(reserva);
            pasarAlSiguiente(reserva.getEjemplar());
            canceladas.add(ReservaDto.de(reserva));
        }
        return canceladas;
    }

    @Scheduled(cron = "0 5 0 * * *")
    @Transactional
    public void caducarCadaDia() {
        caducarReservas();
    }

    @Transactional(readOnly = true)
    public List<ReservaDto> listarReservas() {
        return reservaRepository.findAll().stream().map(ReservaDto::de).toList();
    }

    @Transactional(readOnly = true)
    public List<ReservaDto> colaDelTitulo(Long tituloId) {
        if (!tituloRepository.existsById(tituloId)) {
            throw new RecursoNoEncontradoException("Título", tituloId);
        }
        return reservaRepository.findByTituloIdAndEstadoOrderByFechaHoraSolicitudAsc(tituloId, EstadoReserva.EN_COLA)
                .stream().map(ReservaDto::de).toList();
    }

    @Transactional(readOnly = true)
    public ReservaDto obtenerReserva(Long id) {
        return ReservaDto.de(buscar(id));
    }

    private Reserva buscar(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", id));
    }

    private void pasarAlSiguiente(Ejemplar ejemplar) {
        Optional<Reserva> siguiente = reservaRepository.findFirstByTituloIdAndEstadoOrderByFechaHoraSolicitudAsc(
                ejemplar.getTitulo().getId(), EstadoReserva.EN_COLA);

        if (siguiente.isPresent()) {
            Reserva reserva = siguiente.get();
            reserva.setEstado(EstadoReserva.LISTA_PARA_RECOGER);
            reserva.setEjemplar(ejemplar);
            reserva.setFechaLimiteRecoleccion(LocalDate.now().plusDays(configuracionService.diasRetencion()));
            reservaRepository.save(reserva);
            ejemplar.setEstadoOperativo(EstadoEjemplar.RETENIDO);
        } else {
            ejemplar.setEstadoOperativo(EstadoEjemplar.DISPONIBLE);
        }
        ejemplarRepository.save(ejemplar);
    }
}
