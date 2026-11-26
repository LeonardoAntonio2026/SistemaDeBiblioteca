package com.example.SistemaDeBiblioteca.service;

import com.example.SistemaDeBiblioteca.dto.prestamo.CrearPrestamoDto;
import com.example.SistemaDeBiblioteca.dto.reserva.CrearReservaDto;
import com.example.SistemaDeBiblioteca.dto.reserva.ReservaDto;
import com.example.SistemaDeBiblioteca.exception.ReglaNegocioException;
import com.example.SistemaDeBiblioteca.model.Ejemplar;
import com.example.SistemaDeBiblioteca.model.Lector;
import com.example.SistemaDeBiblioteca.model.Reserva;
import com.example.SistemaDeBiblioteca.model.TipoLector;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservaServiceTest {

    private ReservaRepository repository;
    private EjemplarRepository ejemplarRepository;
    private MultaRepository multaRepository;
    private PrestamoService prestamoService;
    private ReservaService service;
    private Lector ana;
    private Lector luis;
    private Titulo calculo;
    private Ejemplar ejemplar;

    @BeforeEach
    void preparar() {
        repository = mock(ReservaRepository.class);
        LectorRepository lectorRepository = mock(LectorRepository.class);
        TituloRepository tituloRepository = mock(TituloRepository.class);
        ejemplarRepository = mock(EjemplarRepository.class);
        multaRepository = mock(MultaRepository.class);
        prestamoService = mock(PrestamoService.class);
        service = new ReservaService(repository, lectorRepository, tituloRepository, ejemplarRepository,
                multaRepository, prestamoService, configuracion());

        TipoLector alumno = new TipoLector(1, "alumno", 7, 3, 1, new BigDecimal("5.00"));
        calculo = new Titulo(1L, "Cálculo I", "Stewart", null, null, 2018, null, null);
        ana = new Lector(1L, "L-001", "Ana", null, EstadoLector.ACTIVO, alumno);
        luis = new Lector(2L, "L-002", "Luis", null, EstadoLector.ACTIVO, alumno);
        ejemplar = new Ejemplar(1L, "EJ-001", "Anaquel A", "buena", EstadoEjemplar.RETENIDO, calculo);

        when(lectorRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(tituloRepository.findById(1L)).thenReturn(Optional.of(calculo));
        when(repository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crearReservaQuedaEnCola() {
        ReservaDto respuesta = service.crearReserva(pedido());

        assertThat(respuesta.estado()).isEqualTo(EstadoReserva.EN_COLA);
        assertThat(respuesta.fechaHoraSolicitud()).isNotNull();
        assertThat(respuesta.ejemplarId()).isNull();
    }

    @Test
    void crearReservaRechazaSiHayEjemplaresDisponibles() {
        when(ejemplarRepository.existsByTituloIdAndEstadoOperativo(1L, EstadoEjemplar.DISPONIBLE)).thenReturn(true);

        assertThatThrownBy(() -> service.crearReserva(pedido())).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearReservaRechazaLectorSuspendido() {
        ana.setEstado(EstadoLector.SUSPENDIDO);

        assertThatThrownBy(() -> service.crearReserva(pedido())).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearReservaRechazaLectorConMultaPendiente() {
        when(multaRepository.existsByPrestamoLectorIdAndEstado(1L, EstadoMulta.PENDIENTE)).thenReturn(true);

        assertThatThrownBy(() -> service.crearReserva(pedido())).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void crearReservaRechazaSiElLectorYaEstaEnLaCola() {
        when(repository.existsByLectorIdAndTituloIdAndEstado(1L, 1L, EstadoReserva.EN_COLA)).thenReturn(true);

        assertThatThrownBy(() -> service.crearReserva(pedido())).isInstanceOf(ReglaNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void cancelarReservaListaPasaElEjemplarAlSiguiente() {
        Reserva deAna = reservaLista(ana, LocalDate.now().plusDays(1));
        Reserva deLuis = new Reserva(8L, LocalDateTime.now().minusDays(1), EstadoReserva.EN_COLA, null,
                luis, calculo, null, null);
        when(repository.findFirstByTituloIdAndEstadoOrderByFechaHoraSolicitudAsc(1L, EstadoReserva.EN_COLA))
                .thenReturn(Optional.of(deLuis));

        service.cancelarReserva(7L);

        assertThat(deAna.getEstado()).isEqualTo(EstadoReserva.CANCELADA);
        assertThat(deLuis.getEstado()).isEqualTo(EstadoReserva.LISTA_PARA_RECOGER);
        assertThat(deLuis.getEjemplar()).isSameAs(ejemplar);
        assertThat(deLuis.getFechaLimiteRecoleccion()).isEqualTo(LocalDate.now().plusDays(3));
        assertThat(ejemplar.getEstadoOperativo()).isEqualTo(EstadoEjemplar.RETENIDO);
    }

    @Test
    void cancelarReservaYaCumplidaLanzaError() {
        Reserva reserva = reservaLista(ana, LocalDate.now().plusDays(1));
        reserva.setEstado(EstadoReserva.CUMPLIDA);

        assertThatThrownBy(() -> service.cancelarReserva(7L)).isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void entregarReservaCreaElPrestamoParaElLectorEnTurno() {
        reservaLista(ana, LocalDate.now().plusDays(1));

        service.entregarReserva(7L);

        ArgumentCaptor<CrearPrestamoDto> pedido = ArgumentCaptor.forClass(CrearPrestamoDto.class);
        verify(prestamoService).crearPrestamo(pedido.capture());
        assertThat(pedido.getValue().getLectorId()).isEqualTo(1L);
        assertThat(pedido.getValue().getEjemplarId()).isEqualTo(1L);
    }

    @Test
    void entregarReservaEnColaLanzaError() {
        Reserva reserva = reservaLista(ana, LocalDate.now().plusDays(1));
        reserva.setEstado(EstadoReserva.EN_COLA);

        assertThatThrownBy(() -> service.entregarReserva(7L)).isInstanceOf(ReglaNegocioException.class);
        verify(prestamoService, never()).crearPrestamo(any());
    }

    @Test
    void caducarReservasSinNadieEnColaDejaElEjemplarDisponible() {
        Reserva vencida = reservaLista(ana, LocalDate.now().minusDays(1));
        when(repository.findByEstadoAndFechaLimiteRecoleccionBefore(EstadoReserva.LISTA_PARA_RECOGER, LocalDate.now()))
                .thenReturn(List.of(vencida));

        List<ReservaDto> canceladas = service.caducarReservas();

        assertThat(canceladas).hasSize(1);
        assertThat(vencida.getEstado()).isEqualTo(EstadoReserva.CANCELADA);
        assertThat(ejemplar.getEstadoOperativo()).isEqualTo(EstadoEjemplar.DISPONIBLE);
    }

    private Reserva reservaLista(Lector lector, LocalDate fechaLimite) {
        Reserva reserva = new Reserva(7L, LocalDateTime.now().minusDays(3), EstadoReserva.LISTA_PARA_RECOGER,
                fechaLimite, lector, calculo, ejemplar, null);
        when(repository.findById(7L)).thenReturn(Optional.of(reserva));
        return reserva;
    }

    private static CrearReservaDto pedido() {
        return new CrearReservaDto(1L, 1L);
    }

    // Simula que el administrador configuró 3 días de retención
    private static ConfiguracionService configuracion() {
        ConfiguracionService configuracion = mock(ConfiguracionService.class);
        when(configuracion.diasRetencion()).thenReturn(3);
        return configuracion;
    }
}
