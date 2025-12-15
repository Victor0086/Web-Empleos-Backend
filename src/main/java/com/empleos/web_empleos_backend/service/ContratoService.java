package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Contrato;
import com.empleos.web_empleos_backend.model.Postulacion;
import com.empleos.web_empleos_backend.repository.ContratoRepository;
import com.empleos.web_empleos_backend.repository.OfertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private com.empleos.web_empleos_backend.repository.UsuarioRepository usuarioRepository;

    // Método auxiliar para obtener el empleadorId de una oferta
    public String getEmpleadorIdByOfertaId(Long ofertaId) {
        return ofertaRepository.findById(ofertaId)
                .map(oferta -> oferta.getEmpleadorId())
                .orElse("NO_ENCONTRADO");
    }

    public List<Contrato> findAll() {
        return contratoRepository.findAll();
    }

    public Optional<Contrato> findById(Long id) {
        return contratoRepository.findById(id);
    }

    public List<Contrato> findByUsuario(String userId) {
        return contratoRepository.findByUsuario(userId);
    }

    public Contrato save(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    public void deleteById(Long id) {
        contratoRepository.deleteById(id);
    }
    
    public List<Contrato> findContratosPorTrabajadorOEmpleadorOEmail(String userId, String email) {
        // Buscar el user_id correcto por email en la tabla Usuarios
        String userIdFromDB = userId;
        if (email != null) {
            Optional<com.empleos.web_empleos_backend.model.Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isPresent()) {
                userIdFromDB = usuarioOpt.get().getUserId();
                System.out.println("[DEBUG] user_id encontrado en BD por email: " + userIdFromDB);
            }
        }
        
        System.out.println("[DEBUG] Buscando contratos con user_id: " + userIdFromDB + ", email: " + email);
        List<Contrato> contratos = contratoRepository.findContratosPorTrabajadorOEmpleadorOEmail(userIdFromDB, email);
        
        // Limpiar firmas de notario de contratos existentes
        for (Contrato contrato : contratos) {
            if (contrato.getFirmaNotario() != null || contrato.getIdNotario() != null) {
                System.out.println("[DEBUG] Limpiando firma de notario del contrato ID: " + contrato.getId());
                contrato.setFirmaNotario(null);
                contrato.setIdNotario(null);
                contratoRepository.save(contrato);
            }
        }
        
        return contratos;
    }


    public Contrato generarContratoInicial(Postulacion postulacion) {
        if (contratoRepository.findByPostulacion(postulacion).isPresent()) {
            throw new RuntimeException("Ya existe un contrato generado para esta postulación.");
        }
        Contrato nuevoContrato = new Contrato(postulacion);
        return contratoRepository.save(nuevoContrato);
    }

    @Transactional
    public Contrato firmarContrato(Long idContrato, String idUsuario, String emailUsuario) {
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado."));

        System.out.println("[DEBUG FIRMA] Estado inicial del contrato: " + contrato.getEstado());
        System.out.println("[DEBUG FIRMA] Firma trabajador actual: " + contrato.getFirmaTrabajador());
        System.out.println("[DEBUG FIRMA] Firma empleador actual: " + contrato.getFirmaEmpleador());

        String idTrabajador = contrato.getPostulacion().getTrabajadorId();
        String emailTrabajador = contrato.getPostulacion().getEmail();
        
        String idEmpleador = ofertaRepository.findById(contrato.getPostulacion().getOfertaId())
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada")).getEmpleadorId();

        // Buscar el empleador también por email como fallback
        String userIdFromEmail = null;
        if (emailUsuario != null) {
            Optional<com.empleos.web_empleos_backend.model.Usuario> usuarioOpt = usuarioRepository.findByEmail(emailUsuario);
            if (usuarioOpt.isPresent()) {
                userIdFromEmail = usuarioOpt.get().getUserId();
            }
        }

        boolean esTrabajador = idUsuario.equals(idTrabajador) || emailUsuario.equals(emailTrabajador);
        boolean esEmpleador = idUsuario.equals(idEmpleador) || 
                              (userIdFromEmail != null && userIdFromEmail.equals(idEmpleador));

        System.out.println("[DEBUG FIRMA] Usuario que firma - ID: " + idUsuario + ", Email: " + emailUsuario);
        System.out.println("[DEBUG FIRMA] UserID desde email: " + userIdFromEmail);
        System.out.println("[DEBUG FIRMA] Es trabajador: " + esTrabajador + " (ID trabajador: " + idTrabajador + ", Email: " + emailTrabajador + ")");
        System.out.println("[DEBUG FIRMA] Es empleador: " + esEmpleador + " (ID empleador: " + idEmpleador + ")");

        // Limpiar firma de notario si existe (ya que se ha decidido eliminar el notario del proceso)
        if (contrato.getIdNotario() != null || contrato.getFirmaNotario() != null) {
            contrato.setIdNotario(null);
            contrato.setFirmaNotario(null);
            System.out.println("[DEBUG FIRMA] Limpiando firma de notario del contrato");
        }

        if (esTrabajador) {
            if (contrato.getFirmaTrabajador() != null) {
                 throw new RuntimeException("Error: Ya has firmado este contrato anteriormente.");
            }
            LocalDateTime fechaFirma = LocalDateTime.now();
            contrato.setFirmaTrabajador(fechaFirma);
            System.out.println("[DEBUG FIRMA] Trabajador firma el contrato. Fecha: " + fechaFirma);
            
        } else if (esEmpleador) {
            if (contrato.getFirmaEmpleador() != null) {
                 throw new RuntimeException("Error: Ya has firmado este contrato anteriormente.");
            }
            LocalDateTime fechaFirma = LocalDateTime.now();
            contrato.setFirmaEmpleador(fechaFirma);
            System.out.println("[DEBUG FIRMA] Empleador firma el contrato. Fecha: " + fechaFirma);
            
        } else {
            throw new RuntimeException("No tienes permisos para firmar este contrato. Solo el trabajador y el empleador pueden firmar.");
        }

        // Verificar si ambas partes han firmado
        boolean trabajadorFirmado = contrato.getFirmaTrabajador() != null;
        boolean empleadorFirmado = contrato.getFirmaEmpleador() != null;
        
        System.out.println("[DEBUG FIRMA] Después de firma - Trabajador firmado: " + trabajadorFirmado + ", Empleador firmado: " + empleadorFirmado);

        if (trabajadorFirmado && empleadorFirmado) {
            contrato.setEstado("ACTIVO");
            contrato.getPostulacion().setEstado("aceptada");
            System.out.println("[DEBUG FIRMA] Ambas partes firmaron - Estado cambiado a ACTIVO y postulación aceptada");
        } else {
            System.out.println("[DEBUG FIRMA] Aún falta una firma - Estado sigue siendo: " + contrato.getEstado());
        }

        Contrato contratoGuardado = contratoRepository.save(contrato);
        System.out.println("[DEBUG FIRMA] Contrato guardado - Estado final: " + contratoGuardado.getEstado());
        System.out.println("[DEBUG FIRMA] Fecha firma trabajador final: " + contratoGuardado.getFirmaTrabajador());
        System.out.println("[DEBUG FIRMA] Fecha firma empleador final: " + contratoGuardado.getFirmaEmpleador());

        return contratoGuardado;
    }

    @Transactional
    public void rechazarContrato(Long idContrato, String idUsuario, String emailUsuario) {
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado."));
        
        String idTrabajador = contrato.getPostulacion().getTrabajadorId();
        String emailTrabajador = contrato.getPostulacion().getEmail();
        
        String idEmpleador = ofertaRepository.findById(contrato.getPostulacion().getOfertaId())
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada")).getEmpleadorId();

        boolean esTrabajador = idUsuario.equals(idTrabajador) || emailUsuario.equals(emailTrabajador);
        boolean esEmpleador = idUsuario.equals(idEmpleador);

        if (!esTrabajador && !esEmpleador) {
            System.out.println("[ContratoService] Usuario sin permisos intenta rechazar contrato: idUsuario=" + idUsuario + ", emailUsuario=" + emailUsuario);
            throw new RuntimeException("No tienes permisos para rechazar este contrato.");
        }
        
        if (!"PENDIENTE_FIRMAS".equals(contrato.getEstado())) {
            throw new RuntimeException("Solo se pueden rechazar contratos pendientes de firma.");
        }
        
        contrato.setEstado("RECHAZADA");
        contrato.getPostulacion().setEstado("RECHAZADA");
        contratoRepository.save(contrato);
    }
}