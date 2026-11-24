package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.prestamo.CrearPrestamoDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.DevolucionDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.DevolverPrestamoDto;
import com.example.SistemaDeBiblioteca.dto.prestamo.PrestamoDto;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Ejemplar;
import com.example.SistemaDeBiblioteca.model.Lector;
import com.example.SistemaDeBiblioteca.model.Multa;
import com.example.SistemaDeBiblioteca.model.Prestamo;
import com.example.SistemaDeBiblioteca.model.Reserva;
import com.example.SistemaDeBiblioteca.model.TipoLector;
import com.example.SistemaDeBiblioteca.model.Titulo;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PrestamoServiceTest {

    private PrestamoRepository repository;
    private MultaRepository multaRepository;
    private ReservaRepository reservaRepository;
    private PrestamoService service;
    private Lector ana;
    private Ejemplar ejemplar;
    private Titulo calculo;

    @BeforeEach
    void preparar() {
        repository = mock(PrestamoRepository.class);
        LectorRepository lectorRepository = mock(LectorRepository.class);
        EjemplarRepository ejemplarRepository = mock(EjemplarRepository.class);
        multaRepository = mock(MultaRepository.class);
        reservaRepository = mock(ReservaRepository.class);
        service = new PrestamoService(repository, lectorRepository, ejemplarRepository, multaRepository,
                reservaRepository, 3);

        TipoLector alumno = new TipoLector(1, "alumno", 7, 3, 1, new BigDecimal("5.00"));
        calculo = new Titulo(1L, "Cálculo I", "Stewart", null, null, 2018, null, null);
        ana = new Lector(1L, "L-001", "Ana", null, EstadoLector.ACTIVO, alumno);
        ejemplar = new Ejemplar(1L, "EJ-001", "Anaquel A", "buena", EstadoEjemplar.DISPONIBLE, calculo);

        when(lectorRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplar));
        when(repository.save(any(Prestamo.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crearPrestamoUsaLosDiasDelTipoYMarcaElEjemplarPrestado() {
        PrestamoDto respuesta = service.crearPrestamo(pedido());

        assertThat(respuesta.fechaSalida()).isEqualTo(LocalDate.now());
        assertThat(respuesta.fechaLimite()).isEqualTo(LocalDate.now().plusDays(7));
        assertThat(ejemplar.getEstadoOperativo()).isEqualTo(EstadoEjemplar.PRESTADO);
    }

    @Test
    void crearPrestamoRechazaLectorSuspendido() {
        ana.setEstado(EstadoLector.SUSPENDIDO);

        assertThatThrownBy(() -> service.crearPrestamo(pedido())).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearPrestamoRechazaLectorConMultaPendiente() {
        when(multaRepository.existsByPrestamoLectorIdAndEstado(1L, EstadoMulta.PENDIENTE)).thenReturn(true);

        assertThatThrownBy(() -> service.crearPrestamo(pedido())).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearPrestamoRechazaEjemplarNoDisponible() {
        ejemplar.setEstadoOperativo(EstadoEjemplar.EN_REPARACION);

        assertThatThrownBy(() -> service.crearPrestamo(pedido())).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearPrestamoRechazaSiElLectorLlegoAlMaximoDeSuTipo() {
        when(repository.countByLectorIdAndFechaDevolucionIsNull(1L)).thenReturn(3L);

        assertThatThrownBy(() -> service.crearPrestamo(pedido())).isInstanceOf(ReglaNegocioException.class);
        assertThat(ejemplar.getEstadoOperativo()).isEqualTo(EstadoEjemplar.DISPONIBLE);
    }

    @Test
    void devolverATiempoDejaElEjemplarDisponibleSinMulta() {
        prestamoActivo(LocalDate.now().plusDays(2));

        DevolucionDto respuesta = service.devolverPrestamo(devolucion(EstadoEjemplar.DISPONIBLE, null));

        assertThat(respuesta.prestamo().fechaDevolucion()).isEqualTo(LocalDate.now());
        assertThat(respuesta.estadoEjemplar()).isEqualTo(EstadoEjemplar.DISPONIBLE);
        assertThat(respuesta.multaGenerada()).isEqualByComparingTo("0");
        verify(multaRepository, never()).save(any());
    }

    @Test
    void devolverConAtrasoGeneraMultaPorCadaDia() {
        prestamoActivo(LocalDate.now().minusDays(3));

        DevolucionDto respuesta = service.devolverPrestamo(devolucion(EstadoEjemplar.DISPONIBLE, null));

        ArgumentCaptor<Multa> multa = ArgumentCaptor.forClass(Multa.class);
        verify(multaRepository).save(multa.capture());
        assertThat(multa.getValue().getMotivo()).isEqualTo(MotivoMulta.ATRASO);
        assertThat(multa.getValue().getMonto()).isEqualByComparingTo("15.00");
        assertThat(multa.getValue().getEstado()).isEqualTo(EstadoMulta.PENDIENTE);
        assertThat(respuesta.multaGenerada()).isEqualByComparingTo("15.00");
    }

    @Test
    void devolverExtraviadoGeneraMultaPorExtravio() {
        prestamoActivo(LocalDate.now().plusDays(2));

        DevolucionDto respuesta = service.devolverPrestamo(
                devolucion(EstadoEjemplar.EXTRAVIADO, new BigDecimal("350.00")));

        ArgumentCaptor<Multa> multa = ArgumentCaptor.forClass(Multa.class);
        verify(multaRepository).save(multa.capture());
        assertThat(multa.getValue().getMotivo()).isEqualTo(MotivoMulta.EXTRAVIO);
        assertThat(respuesta.estadoEjemplar()).isEqualTo(EstadoEjemplar.EXTRAVIADO);
    }

    @Test
    void devolverConColaRetieneElEjemplarParaElPrimero() {
        prestamoActivo(LocalDate.now().plusDays(2));
        Lector luis = new Lector(2L, "L-002", "Luis", null, EstadoLector.ACTIVO, ana.getTipoLector());
        Reserva reserva = new Reserva(7L, LocalDateTime.now().minusDays(1), EstadoReserva.EN_COLA, null,
                luis, calculo, null, null);
        when(reservaRepository.findFirstByTituloIdAndEstadoOrderByFechaHoraSolicitudAsc(1L, EstadoReserva.EN_COLA))
                .thenReturn(Optional.of(reserva));

        DevolucionDto respuesta = service.devolverPrestamo(devolucion(EstadoEjemplar.DISPONIBLE, null));

        assertThat(respuesta.estadoEjemplar()).isEqualTo(EstadoEjemplar.RETENIDO);
        assertThat(reserva.getEstado()).isEqualTo(EstadoReserva.LISTA_PARA_RECOGER);
        assertThat(reserva.getEjemplar()).isSameAs(ejemplar);
        assertThat(reserva.getFechaLimiteRecoleccion()).isEqualTo(LocalDate.now().plusDays(3));
    }

    @Test
    void devolverUnPrestamoYaDevueltoLanzaError() {
        Prestamo prestamo = prestamoActivo(LocalDate.now().plusDays(2));
        prestamo.setFechaDevolucion(LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> service.devolverPrestamo(devolucion(EstadoEjemplar.DISPONIBLE, null)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    private Prestamo prestamoActivo(LocalDate fechaLimite) {
        ejemplar.setEstadoOperativo(EstadoEjemplar.PRESTADO);
        Prestamo prestamo = new Prestamo(10L, fechaLimite.minusDays(7), fechaLimite, null, 0, null, ana, ejemplar);
        when(repository.findById(10L)).thenReturn(Optional.of(prestamo));
        return prestamo;
    }

    private static CrearPrestamoDto pedido() {
        CrearPrestamoDto payload = new CrearPrestamoDto();
        payload.setLectorId(1L);
        payload.setEjemplarId(1L);
        return payload;
    }

    private static DevolverPrestamoDto devolucion(EstadoEjemplar estado, BigDecimal montoMulta) {
        DevolverPrestamoDto payload = new DevolverPrestamoDto();
        payload.setPrestamoId(10L);
        payload.setEstadoEjemplar(estado);
        payload.setMontoMulta(montoMulta);
        return payload;
    }
}
