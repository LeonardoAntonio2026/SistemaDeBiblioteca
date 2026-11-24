package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.prestamo.CrearPrestamoDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.DevolucionDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.DevolverPrestamoDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.PrestamoDto;
import com.example.SistemaDeBiblioteca.exception.RecursoNoEncontradoException;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Ejemplar;
import com.example.SistemaDeBiblioteca.model.Lector;
import com.example.SistemaDeBiblioteca.model.Multa;
import com.example.SistemaDeBiblioteca.model.Prestamo;
import com.example.SistemaDeBiblioteca.model.Reserva;
import com.example.SistemaDeBiblioteca.model.TipoLector;
import com.example.SistemaDeBiblioteca.model.enums.EstadoEjemplar;
import com.example.SistemaDeBiblioteca.model.enums.EstadoLector;
import com.example.SistemaDeBiblioteca.model.enums.EstadoMulta;
import com.example.SistemaDeBiblioteca.model.enums.EstadoReserva;
import com.example.SistemaDeBiblioteca.model.enums.MotivoMulta;
import com.example.SistemaDeBiblioteca.repository.EjemplarRepository;
import com.example.SistemaDeBiblioteca.repository.LectorRepository;
import com.example.SistemaDeBiblioteca.repository.MultaRepository;
import com.example.SistemaDeBiblioteca.repository.PrestamoRepository;
import com.example.SistemaDeBiblioteca.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LectorRepository lectorRepository;
    private final EjemplarRepository ejemplarRepository;
    private final MultaRepository multaRepository;
    private final ReservaRepository reservaRepository;
    private final int diasRetencion;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LectorRepository lectorRepository,
                           EjemplarRepository ejemplarRepository,
                           MultaRepository multaRepository,
                           ReservaRepository reservaRepository,
                           @Value("${biblioteca.reserva.dias-retencion:3}") int diasRetencion) {
        this.prestamoRepository = prestamoRepository;
        this.lectorRepository = lectorRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.multaRepository = multaRepository;
        this.reservaRepository = reservaRepository;
        this.diasRetencion = diasRetencion;
    }

    @Transactional
    public PrestamoDto crearPrestamo(CrearPrestamoDto payload) {
        Lector lector = lectorRepository.findById(payload.getLectorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Lector", payload.getLectorId()));
        Ejemplar ejemplar = ejemplarRepository.findById(payload.getEjemplarId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Ejemplar", payload.getEjemplarId()));
        TipoLector tipo = lector.getTipoLector();

        if (lector.getEstado() == EstadoLector.SUSPENDIDO) {
            throw new ReglaNegocioException("El lector está suspendido");
        }
        if (multaRepository.existsByPrestamoLectorIdAndEstado(lector.getId(), EstadoMulta.PENDIENTE)) {
            throw new ReglaNegocioException("El lector tiene multas pendientes");
        }
        if (ejemplar.getEstadoOperativo() != EstadoEjemplar.DISPONIBLE) {
            throw new ReglaNegocioException("El ejemplar no está disponible");
        }
        if (prestamoRepository.countByLectorIdAndFechaDevolucionIsNull(lector.getId()) >= tipo.getMaxLibros()) {
            throw new ReglaNegocioException("El lector ya tiene el máximo de " + tipo.getMaxLibros() + " préstamos");
        }

        LocalDate hoy = LocalDate.now();
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setLector(lector);
        nuevoPrestamo.setEjemplar(ejemplar);
        nuevoPrestamo.setFechaSalida(hoy);
        nuevoPrestamo.setFechaLimite(hoy.plusDays(tipo.getDiasPrestamo()));
        nuevoPrestamo.setRenovacionesRealizadas(0);

        ejemplar.setEstadoOperativo(EstadoEjemplar.PRESTADO);
        ejemplarRepository.save(ejemplar);

        return PrestamoDto.de(prestamoRepository.save(nuevoPrestamo));
    }

    @Transactional
    public DevolucionDto devolverPrestamo(DevolverPrestamoDto payload) {
        Prestamo prestamo = buscar(payload.getPrestamoId());
        EstadoEjemplar estadoPedido = payload.getEstadoEjemplar();

        if (prestamo.getFechaDevolucion() != null) {
            throw new ReglaNegocioException("El préstamo ya fue devuelto");
        }
        if (estadoPedido == EstadoEjemplar.PRESTADO || estadoPedido == EstadoEjemplar.RETENIDO) {
            throw new ReglaNegocioException("El ejemplar solo puede regresar como disponible, en reparación o extraviado");
        }

        LocalDate hoy = LocalDate.now();
        BigDecimal multaGenerada = BigDecimal.ZERO;

        long diasAtraso = ChronoUnit.DAYS.between(prestamo.getFechaLimite(), hoy);
        if (diasAtraso > 0) {
            BigDecimal monto = prestamo.getLector().getTipoLector().getMultaPorDia()
                    .multiply(BigDecimal.valueOf(diasAtraso));
            generarMulta(prestamo, MotivoMulta.ATRASO, monto, hoy);
            multaGenerada = multaGenerada.add(monto);
        }
        if (payload.getMontoMulta() != null && payload.getMontoMulta().signum() > 0) {
            MotivoMulta motivo = estadoPedido == EstadoEjemplar.EXTRAVIADO ? MotivoMulta.EXTRAVIO : MotivoMulta.DANIO;
            generarMulta(prestamo, motivo, payload.getMontoMulta(), hoy);
            multaGenerada = multaGenerada.add(payload.getMontoMulta());
        }

        prestamo.setFechaDevolucion(hoy);
        prestamo.setObservaciones(payload.getObservaciones());

        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstadoOperativo(destinoDelEjemplar(ejemplar, estadoPedido, hoy));
        ejemplarRepository.save(ejemplar);

        return new DevolucionDto(PrestamoDto.de(prestamoRepository.save(prestamo)),
                ejemplar.getEstadoOperativo(), multaGenerada);
    }

    @Transactional(readOnly = true)
    public List<PrestamoDto> listarPrestamos() {
        return prestamoRepository.findAll().stream().map(PrestamoDto::de).toList();
    }

    @Transactional(readOnly = true)
    public PrestamoDto obtenerPrestamo(Long id) {
        return PrestamoDto.de(buscar(id));
    }

    private Prestamo buscar(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Préstamo", id));
    }

    private void generarMulta(Prestamo prestamo, MotivoMulta motivo, BigDecimal monto, LocalDate hoy) {
        Multa multa = new Multa();
        multa.setPrestamo(prestamo);
        multa.setMotivo(motivo);
        multa.setMonto(monto);
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setFechaGeneracion(hoy);
        multaRepository.save(multa);
    }

    private EstadoEjemplar destinoDelEjemplar(Ejemplar ejemplar, EstadoEjemplar estadoPedido, LocalDate hoy) {
        if (estadoPedido != EstadoEjemplar.DISPONIBLE) {
            return estadoPedido;
        }

        Optional<Reserva> enTurno = reservaRepository
                .findFirstByTituloIdAndEstadoOrderByFechaHoraSolicitudAsc(ejemplar.getTitulo().getId(), EstadoReserva.EN_COLA);
        if (enTurno.isEmpty()) {
            return EstadoEjemplar.DISPONIBLE;
        }

        Reserva reserva = enTurno.get();
        reserva.setEstado(EstadoReserva.LISTA_PARA_RECOGER);
        reserva.setEjemplar(ejemplar);
        reserva.setFechaLimiteRecoleccion(hoy.plusDays(diasRetencion));
        reservaRepository.save(reserva);
        return EstadoEjemplar.RETENIDO;
    }
}
