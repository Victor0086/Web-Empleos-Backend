package com.empleos.web_empleos_backend.service;

import com.empleos.web_empleos_backend.model.Usuario;
import com.empleos.web_empleos_backend.model.Contacto;
import com.empleos.web_empleos_backend.model.Estudio;
import com.empleos.web_empleos_backend.model.Experiencia;
import com.empleos.web_empleos_backend.dto.PerfilDTO;
import com.empleos.web_empleos_backend.dto.ContactoDTO;
import com.empleos.web_empleos_backend.dto.EstudioDTO;
import com.empleos.web_empleos_backend.dto.ExperienciaDTO;
import com.empleos.web_empleos_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(new java.util.Date());
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio y debe ser único.");
        }
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            Usuario user = existente.get();
            // Actualiza solo los campos básicos si vienen nuevos datos
            if (usuario.getNombreCompleto() != null) user.setNombreCompleto(usuario.getNombreCompleto());
            if (usuario.getRol() != null) user.setRol(usuario.getRol());
            if (usuario.getFotoUrl() != null) user.setFotoUrl(usuario.getFotoUrl());
            if (usuario.getNacionalidad() != null) user.setNacionalidad(usuario.getNacionalidad());
            if (usuario.getNacimiento() != null) user.setNacimiento(usuario.getNacimiento());
            if (usuario.getGenero() != null) user.setGenero(usuario.getGenero());
            if (usuario.getEstadoCivil() != null) user.setEstadoCivil(usuario.getEstadoCivil());
            if (usuario.getLicencia() != null) user.setLicencia(usuario.getLicencia());
            if (usuario.getContacto() != null) user.setContacto(usuario.getContacto());
            if (usuario.getDescripcion() != null) user.setDescripcion(usuario.getDescripcion());
            if (usuario.getHabilidades() != null) user.setHabilidades(usuario.getHabilidades());
            if (usuario.getCvAdjunto() != null) user.setCvAdjunto(usuario.getCvAdjunto());
            
            return usuarioRepository.save(user);
        } else {
            return usuarioRepository.save(usuario);
        }
    }

    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario buscarPorId(String userId) {
        return usuarioRepository.findById(userId).orElse(null);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar educación
    public Estudio agregarEstudio(String userId, EstudioDTO estudioDTO) {
        System.out.println("[agregarEstudio] userId recibido: " + userId);
        Usuario usuario = buscarPorId(userId);
        if (usuario == null) {
            System.out.println("[agregarEstudio] Usuario no encontrado para userId: " + userId);
            return null;
        }
        if (usuario.getEstudios() == null) {
            usuario.setEstudios(new java.util.ArrayList<>());
            System.out.println("[agregarEstudio] Inicializando lista de estudios para usuario: " + userId);
        }
        // ...existing code...
        Estudio estudio = new Estudio();
        estudio.setTitulo(estudioDTO.getTitulo());
        estudio.setInstitucion(estudioDTO.getInstitucion());
        estudio.setTipo(estudioDTO.getTipo());
        estudio.setEstado(estudioDTO.getEstado());
        estudio.setFechainicio(estudioDTO.getFechainicio());
        estudio.setFechaFin(estudioDTO.getFechaFin());
        estudio.setReferencia(estudioDTO.getReferencia());
        estudio.setCertificado(estudioDTO.getCertificado());
        estudio.setUsuario(usuario); // Asociar usuario al estudio
        usuario.getEstudios().add(estudio);
        guardar(usuario);
        return estudio;
    }

    // Actualizar experiencia
    public Experiencia agregarExperiencia(String userId, ExperienciaDTO experienciaDTO) {
        Usuario usuario = buscarPorId(userId);
        if (usuario == null) return null;
        Experiencia experiencia = new Experiencia();
        experiencia.setEmpresa(experienciaDTO.getEmpresa());
        experiencia.setPuesto(experienciaDTO.getPuesto());
        experiencia.setFechaInicio(experienciaDTO.getFechaInicio());
        experiencia.setFechaFin(experienciaDTO.getFechaFin());
        experiencia.setDescripcion(experienciaDTO.getDescripcion());
        experiencia.setUsuario(usuario);
        usuario.getExperiencias().add(experiencia);
        guardar(usuario);
        return experiencia;
    }

    // Actualizar perfil profesional
    public Usuario actualizarPerfilPorEmail(String email, PerfilDTO perfilDTO) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) return null;

        if (perfilDTO.getNombreCompleto() != null && !perfilDTO.getNombreCompleto().isEmpty())
            usuario.setNombreCompleto(perfilDTO.getNombreCompleto());
        if (perfilDTO.getRol() != null && !perfilDTO.getRol().isEmpty())
            usuario.setRol(perfilDTO.getRol());
        if (perfilDTO.getFotoUrl() != null && !perfilDTO.getFotoUrl().isEmpty())
            usuario.setFotoUrl(perfilDTO.getFotoUrl());
        if (perfilDTO.getNacionalidad() != null && !perfilDTO.getNacionalidad().isEmpty())
            usuario.setNacionalidad(perfilDTO.getNacionalidad());
        if (perfilDTO.getNacimiento() != null)
            usuario.setNacimiento(perfilDTO.getNacimiento());
        if (perfilDTO.getGenero() != null && !perfilDTO.getGenero().isEmpty())
            usuario.setGenero(perfilDTO.getGenero());
        if (perfilDTO.getEstadoCivil() != null && !perfilDTO.getEstadoCivil().isEmpty())
            usuario.setEstadoCivil(perfilDTO.getEstadoCivil());
        if (perfilDTO.getLicencia() != null && !perfilDTO.getLicencia().isEmpty())
            usuario.setLicencia(perfilDTO.getLicencia());

        // Mapeo de contacto solo si viene
        if (perfilDTO.getContacto() != null) {
            ContactoDTO c = perfilDTO.getContacto();
            Contacto contacto = usuario.getContacto() != null ? usuario.getContacto() : new Contacto();
            if (c.getCelular() != null && !c.getCelular().isEmpty()) contacto.setCelular(c.getCelular());
            if (c.getTelefono() != null && !c.getTelefono().isEmpty()) contacto.setTelefono(c.getTelefono());
            if (c.getEmail() != null && !c.getEmail().isEmpty()) contacto.setEmail(c.getEmail());
            if (c.getDireccion() != null && !c.getDireccion().isEmpty()) contacto.setDireccion(c.getDireccion());
            usuario.setContacto(contacto);
        }

        if (perfilDTO.getDescripcion() != null && !perfilDTO.getDescripcion().isEmpty())
            usuario.setDescripcion(perfilDTO.getDescripcion());
        if (perfilDTO.getHabilidades() != null && !perfilDTO.getHabilidades().isEmpty())
            usuario.setHabilidades(perfilDTO.getHabilidades());
        if (perfilDTO.getCvAdjunto() != null && !perfilDTO.getCvAdjunto().isEmpty())
            usuario.setCvAdjunto(perfilDTO.getCvAdjunto());

        // Guardar resumen profesional si viene
        if (perfilDTO.getResumenProfesional() != null && !perfilDTO.getResumenProfesional().isEmpty()) {
            usuario.setResumenProfesional(perfilDTO.getResumenProfesional());
            
        }

        guardar(usuario);
        return usuario;
    }
}