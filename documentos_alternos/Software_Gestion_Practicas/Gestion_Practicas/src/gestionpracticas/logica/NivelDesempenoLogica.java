package com.gestionpracticas.logica;

import com.gestionpracticas.dao.NivelDesempenoDAO;
import com.gestionpracticas.modelo.NivelDesempeno;
import java.util.List;

public class NivelDesempenoLogica {
    
    private final NivelDesempenoDAO dao;
    
    public NivelDesempenoLogica() {
        this.dao = new NivelDesempenoDAO();
    }
    
    public boolean guardar(NivelDesempeno nivel) {
        if (nivel.getNombre() == null || nivel.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del nivel es obligatorio");
        }
        if (nivel.getCriterio() == null || nivel.getCriterio().getIdCriterio() <= 0) {
            throw new IllegalArgumentException("El criterio es obligatorio");
        }
        if (nivel.getPuntajeMinimo() < 0) {
            throw new IllegalArgumentException("El puntaje mínimo no puede ser negativo");
        }
        if (nivel.getPuntajeMaximo() <= nivel.getPuntajeMinimo()) {
            throw new IllegalArgumentException("El puntaje máximo debe ser mayor al mínimo");
        }
        if (nivel.getPuntajeMaximo() > 100) {
            throw new IllegalArgumentException("El puntaje máximo no puede superar 100");
        }
        
        return dao.insertar(nivel);
    }
    
    public List<NivelDesempeno> listarPorCriterio(int idCriterio) {
        if (idCriterio <= 0) {
            throw new IllegalArgumentException("ID de criterio inválido");
        }
        return dao.listarPorCriterio(idCriterio);
    }
    
    public List<NivelDesempeno> listarTodos() {
        return dao.listarTodos();
    }
    
    public NivelDesempeno buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de nivel inválido");
        }
        return dao.buscarPorId(id);
    }
    
    public NivelDesempeno buscarPorPuntaje(int idCriterio, double puntaje) {
        if (idCriterio <= 0) {
            throw new IllegalArgumentException("ID de criterio inválido");
        }
        if (puntaje < 0 || puntaje > 100) {
            throw new IllegalArgumentException("El puntaje debe estar entre 0 y 100");
        }
        return dao.buscarPorPuntaje(idCriterio, puntaje);
    }
    
    public boolean actualizar(NivelDesempeno nivel) {
        return dao.actualizar(nivel);
    }
    
    public boolean eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de nivel inválido");
        }
        return dao.eliminar(id);
    }
    
    public boolean eliminarPorCriterio(int idCriterio) {
        if (idCriterio <= 0) {
            throw new IllegalArgumentException("ID de criterio inválido");
        }
        return dao.eliminarPorCriterio(idCriterio);
    }
}