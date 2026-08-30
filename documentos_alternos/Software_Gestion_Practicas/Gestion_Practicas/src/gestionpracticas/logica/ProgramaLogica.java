package com.gestionpracticas.logica;

import com.gestionpracticas.dao.ProgramaDAO;
import com.gestionpracticas.modelo.Programa;
import java.util.List;

public class ProgramaLogica {
    
    private final ProgramaDAO dao;
    
    public ProgramaLogica() { 
        this.dao = new ProgramaDAO(); 
    }

    public boolean crearPrograma(Programa p) {
        // Validaciones
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del programa es obligatorio.");
        }
        if (p.getCodigo() == null || p.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del programa es obligatorio.");
        }
        if (p.getDuracionSemestres() <= 0) {
            throw new IllegalArgumentException("La duración en semestres debe ser mayor a 0.");
        }
        
        p.setEstado("ACTIVO");
        return dao.insertar(p);
    }

    public List<Programa> obtenerTodos() { 
        return dao.listarTodos(); 
    }
    
    public List<Programa> obtenerActivos() { 
        return dao.listarActivos(); 
    }
    
    public List<Programa> obtenerPorFacultad(String facultad) {
        if (facultad == null || facultad.trim().isEmpty()) {
            throw new IllegalArgumentException("La facultad es obligatoria.");
        }
        return dao.listarPorFacultad(facultad);
    }
    
    public List<Programa> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        return dao.buscarPorNombre(nombre);
    }

    public Programa buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de programa inválido.");
        }
        return dao.buscarPorId(id);
    }
    
    public Programa buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        return dao.buscarPorCodigo(codigo);
    }

    public boolean actualizar(Programa p) {
        if (p.getIdPrograma() <= 0) {
            throw new IllegalArgumentException("ID de programa inválido.");
        }
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del programa es obligatorio.");
        }
        if (p.getCodigo() == null || p.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del programa es obligatorio.");
        }
        return dao.actualizar(p);
    }
    
    public boolean activar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de programa inválido.");
        }
        return dao.cambiarEstado(id, "ACTIVO");
    }

    public boolean desactivar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de programa inválido.");
        }
        return dao.eliminar(id);
    }
    
    public boolean eliminarFisicamente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de programa inválido.");
        }
        return dao.eliminarFisico(id);
    }
    
    public int contarActivos() {
        return dao.contarActivos();
    }
    
    public boolean existeCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        return dao.buscarPorCodigo(codigo) != null;
    }
}