package com.gestionpracticas.logica;

import com.gestionpracticas.dao.InstitucionDAO;
import com.gestionpracticas.modelo.Institucion;
import java.util.List;

public class InstitucionLogica {
    
    private final InstitucionDAO dao;
    
    public InstitucionLogica() {
        this.dao = new InstitucionDAO();
    }
    
    public boolean registrar(Institucion i) {
        // Validaciones
        if (i.getNombre() == null || i.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la institución es obligatorio.");
        }
        if (i.getNit() == null || i.getNit().trim().isEmpty()) {
            throw new IllegalArgumentException("El NIT es obligatorio.");
        }
        if (i.getEmail() == null || i.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (i.getCiudad() == null || i.getCiudad().trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }
        
        // Validar formato de email
        if (!i.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email no es válido.");
        }
        
        // Verificar si ya existe una institución con el mismo NIT
        Institucion existente = dao.buscarPorNit(i.getNit());
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe una institución con ese NIT.");
        }
        
        i.setEstado("ACTIVO");
        return dao.insertar(i);
    }
    
    public List<Institucion> listarTodas() {
        return dao.listarTodos();
    }
    
    public List<Institucion> listarActivas() {
        return dao.listarActivas();
    }
    
    public List<Institucion> listarPorCiudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }
        return dao.listarPorCiudad(ciudad);
    }
    
    public Institucion buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de institución inválido.");
        }
        return dao.buscarPorId(id);
    }
    
    public Institucion buscarPorNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) {
            throw new IllegalArgumentException("El NIT es obligatorio.");
        }
        return dao.buscarPorNit(nit);
    }
    
    public Institucion buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        return dao.buscarPorEmail(email);
    }
    
    public boolean actualizar(Institucion i) {
        if (i.getIdInstitucion() <= 0) {
            throw new IllegalArgumentException("ID de institución inválido.");
        }
        return dao.actualizar(i);
    }
    
    public boolean desactivar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de institución inválido.");
        }
        return dao.eliminar(id);
    }
    
    public boolean activar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de institución inválido.");
        }
        // Actualizar estado a ACTIVO
        Institucion i = dao.buscarPorId(id);
        if (i != null) {
            i.setEstado("ACTIVO");
            return dao.actualizar(i);
        }
        return false;
    }
    
    public int contarActivas() {
        return dao.contarActivas();
    }
}