package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Contrato;
import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.repository.ContratoRepository;
import com.empleos.web_empleos_backend.repository.OfertaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContratoServiceTest {
    @Mock
    private ContratoRepository contratoRepository;
    @Mock
    private OfertaRepository ofertaRepository;
    @InjectMocks
    private ContratoService contratoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_delegaEnRepositorio() {
        List<Contrato> contratos = List.of(new Contrato());
        when(contratoRepository.findAll()).thenReturn(contratos);
        assertEquals(contratos, contratoService.findAll());
    }

    @Test
    void findById_delegaEnRepositorio() {
        Contrato contrato = new Contrato();
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        assertTrue(contratoService.findById(1L).isPresent());
    }

    @Test
    void findByUsuario_delegaEnRepositorio() {
        List<Contrato> contratos = List.of(new Contrato());
        when(contratoRepository.findByUsuario("user1")).thenReturn(contratos);
        assertEquals(contratos, contratoService.findByUsuario("user1"));
    }

    @Test
    void save_delegaEnRepositorio() {
        Contrato contrato = new Contrato();
        when(contratoRepository.save(contrato)).thenReturn(contrato);
        assertEquals(contrato, contratoService.save(contrato));
    }

    @Test
    void deleteById_delegaEnRepositorio() {
        doNothing().when(contratoRepository).deleteById(1L);
        contratoService.deleteById(1L);
        verify(contratoRepository).deleteById(1L);
    }

    @Test
    void findContratosPorTrabajadorOEmpleadorOEmail_delegaEnRepositorio() {
        List<Contrato> contratos = List.of(new Contrato());
        when(contratoRepository.findContratosPorTrabajadorOEmpleadorOEmail("u1", "e1")).thenReturn(contratos);
        assertEquals(contratos, contratoService.findContratosPorTrabajadorOEmpleadorOEmail("u1", "e1"));
    }

    @Test
    void generarContratoInicial_exito() {
        Postulacion postulacion = new Postulacion();
        when(contratoRepository.findByPostulacion(postulacion)).thenReturn(Optional.empty());
        Contrato contrato = new Contrato(postulacion);
        when(contratoRepository.save(any(Contrato.class))).thenReturn(contrato);
        Contrato result = contratoService.generarContratoInicial(postulacion);
        assertEquals(contrato, result);
    }

    @Test
    void generarContratoInicial_yaExisteContrato_lanzaExcepcion() {
        Postulacion postulacion = new Postulacion();
        when(contratoRepository.findByPostulacion(postulacion)).thenReturn(Optional.of(new Contrato()));
        assertThrows(RuntimeException.class, () -> contratoService.generarContratoInicial(postulacion));
    }

    @Test
    void firmarContrato_trabajadorFirmaPrimeraVez_exito() {
        Postulacion postulacion = new Postulacion();
        postulacion.setTrabajadorId("trab1");
        postulacion.setEmail("trab@correo.com");
        Contrato contrato = new Contrato(postulacion);
        contrato.setPostulacion(postulacion);
        contrato.setFirmaTrabajador(null);
        contrato.setFirmaEmpleador(null);
        contrato.setEstado("PENDIENTE_FIRMAS");
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        var oferta = mock(com.empleos.web_empleos_backend.model.Oferta.class);
        when(oferta.getEmpleadorId()).thenReturn("emp1");
        when(ofertaRepository.findById(any())).thenReturn(Optional.of(oferta));
        when(contratoRepository.save(any(Contrato.class))).thenReturn(contrato);
        Contrato result = contratoService.firmarContrato(1L, "trab1", "trab@correo.com");
        assertNotNull(result.getFirmaTrabajador());
    }

    @Test
    void firmarContrato_empleadorFirmaPrimeraVez_exito() {
        Postulacion postulacion = new Postulacion();
        postulacion.setTrabajadorId("trab1");
        postulacion.setEmail("trab@correo.com");
        postulacion.setOfertaId(2L);
        Contrato contrato = new Contrato(postulacion);
        contrato.setPostulacion(postulacion);
        contrato.setFirmaTrabajador(LocalDateTime.now());
        contrato.setFirmaEmpleador(null);
        contrato.setEstado("PENDIENTE_FIRMAS");
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        var oferta = mock(com.empleos.web_empleos_backend.model.Oferta.class);
        when(oferta.getEmpleadorId()).thenReturn("emp1");
        when(ofertaRepository.findById(2L)).thenReturn(Optional.of(oferta));
        when(contratoRepository.save(any(Contrato.class))).thenReturn(contrato);
        Contrato result = contratoService.firmarContrato(1L, "emp1", "otro@correo.com");
        assertNotNull(result.getFirmaEmpleador());
    }

    @Test
    void firmarContrato_yaFirmadoPorTrabajador_lanzaExcepcion() {
        Postulacion postulacion = new Postulacion();
        postulacion.setTrabajadorId("trab1");
        postulacion.setEmail("trab@correo.com");
        Contrato contrato = new Contrato(postulacion);
        contrato.setPostulacion(postulacion);
        contrato.setFirmaTrabajador(LocalDateTime.now());
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        var oferta = mock(com.empleos.web_empleos_backend.model.Oferta.class);
        when(oferta.getEmpleadorId()).thenReturn("emp1");
        when(ofertaRepository.findById(any())).thenReturn(Optional.of(oferta));
        assertThrows(RuntimeException.class, () -> contratoService.firmarContrato(1L, "trab1", "trab@correo.com"));
    }

    @Test
    void firmarContrato_noExisteContrato_lanzaExcepcion() {
        when(contratoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> contratoService.firmarContrato(1L, "trab1", "correo@correo.com"));
    }

    @Test
    void firmarContrato_noExisteOferta_lanzaExcepcion() {
        Postulacion postulacion = new Postulacion();
        postulacion.setTrabajadorId("trab1");
        postulacion.setEmail("trab@correo.com");
        postulacion.setOfertaId(2L);
        Contrato contrato = new Contrato(postulacion);
        contrato.setPostulacion(postulacion);
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        when(ofertaRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> contratoService.firmarContrato(1L, "emp1", "otro@correo.com"));
    }

    @Test
    void rechazarContrato_exito() {
        Postulacion postulacion = new Postulacion();
        postulacion.setTrabajadorId("trab1");
        postulacion.setEmail("trab@correo.com");
        postulacion.setOfertaId(2L);
        Contrato contrato = new Contrato(postulacion);
        contrato.setPostulacion(postulacion);
        contrato.setEstado("PENDIENTE_FIRMAS");
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        var oferta = mock(com.empleos.web_empleos_backend.model.Oferta.class);
        when(oferta.getEmpleadorId()).thenReturn("emp1");
        when(ofertaRepository.findById(2L)).thenReturn(Optional.of(oferta));
        when(contratoRepository.save(any(Contrato.class))).thenReturn(contrato);
        contratoService.rechazarContrato(1L, "trab1", "trab@correo.com");
        assertEquals("RECHAZADA", contrato.getEstado());
    }

    @Test
    void rechazarContrato_noPermiso_lanzaExcepcion() {
        Postulacion postulacion = new Postulacion();
        postulacion.setTrabajadorId("trab1");
        postulacion.setEmail("trab@correo.com");
        postulacion.setOfertaId(2L);
        Contrato contrato = new Contrato(postulacion);
        contrato.setPostulacion(postulacion);
        contrato.setEstado("PENDIENTE_FIRMAS");
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        var oferta = mock(com.empleos.web_empleos_backend.model.Oferta.class);
        when(oferta.getEmpleadorId()).thenReturn("emp1");
        when(ofertaRepository.findById(2L)).thenReturn(Optional.of(oferta));
        assertThrows(RuntimeException.class, () -> contratoService.rechazarContrato(1L, "otro", "otro@correo.com"));
    }

    @Test
    void rechazarContrato_estadoNoPendiente_lanzaExcepcion() {
        Postulacion postulacion = new Postulacion();
        postulacion.setTrabajadorId("trab1");
        postulacion.setEmail("trab@correo.com");
        postulacion.setOfertaId(2L);
        Contrato contrato = new Contrato(postulacion);
        contrato.setPostulacion(postulacion);
        contrato.setEstado("ACTIVO");
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        var oferta = mock(com.empleos.web_empleos_backend.model.Oferta.class);
        when(oferta.getEmpleadorId()).thenReturn("emp1");
        when(ofertaRepository.findById(2L)).thenReturn(Optional.of(oferta));
        assertThrows(RuntimeException.class, () -> contratoService.rechazarContrato(1L, "trab1", "trab@correo.com"));
    }

    @Test
    void rechazarContrato_noExisteContrato_lanzaExcepcion() {
        when(contratoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> contratoService.rechazarContrato(1L, "trab1", "correo@correo.com"));
    }

}
