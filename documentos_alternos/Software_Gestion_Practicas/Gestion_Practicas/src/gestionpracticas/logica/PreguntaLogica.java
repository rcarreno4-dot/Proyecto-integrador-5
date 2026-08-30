package com.gestionpracticas.logica;

import com.gestionpracticas.dao.PreguntaDAO;
import com.gestionpracticas.modelo.Pregunta;
import java.util.List;

public class PreguntaLogica {
    
    private final PreguntaDAO dao;
    
    public PreguntaLogica() {
        this.dao = new PreguntaDAO();
    }
    
    public boolean guardar(Pregunta p) {
        // Validaciones
        if (p.getTextoPregunta() == null || p.getTextoPregunta().trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la pregunta es obligatorio.");
        }
        if (p.getTipoPregunta() == null || p.getTipoPregunta().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de pregunta es obligatorio.");
        }
        if (p.getPuntajeMaximo() <= 0) {
            throw new IllegalArgumentException("El puntaje máximo debe ser mayor a 0.");
        }
        if (p.getEvaluacion() == null || p.getEvaluacion().getIdEvaluacion() <= 0) {
            throw new IllegalArgumentException("La evaluación es obligatoria.");
        }
        
        return dao.insertar(p);
    }
    
    public boolean guardarVarias(List<Pregunta> preguntas) {
        if (preguntas == null || preguntas.isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos una pregunta.");
        }
        for (Pregunta p : preguntas) {
            if (p.getTextoPregunta() == null || p.getTextoPregunta().trim().isEmpty()) {
                throw new IllegalArgumentException("Todas las preguntas deben tener texto.");
            }
        }
        return dao.insertarVarias(preguntas);
    }
    
    public List<Pregunta> listarPorEvaluacion(int idEvaluacion) {
        if (idEvaluacion <= 0) {
            throw new IllegalArgumentException("ID de evaluación inválido.");
        }
        return dao.listarPorEvaluacion(idEvaluacion);
    }
    
    public List<Pregunta> listarPorPractica(int idPractica) {
        if (idPractica <= 0) {
            throw new IllegalArgumentException("ID de práctica inválido.");
        }
        return dao.listarPorPractica(idPractica);
    }
    
    public List<Pregunta> listarTodas() {
        return dao.listarTodas();
    }
    
    public Pregunta buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de pregunta inválido.");
        }
        return dao.buscarPorId(id);
    }
    
    public boolean actualizar(Pregunta p) {
        if (p.getIdPregunta() <= 0) {
            throw new IllegalArgumentException("ID de pregunta inválido.");
        }
        return dao.actualizar(p);
    }
    
    public boolean eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de pregunta inválido.");
        }
        return dao.eliminar(id);
    }
    
    public boolean eliminarPorEvaluacion(int idEvaluacion) {
        if (idEvaluacion <= 0) {
            throw new IllegalArgumentException("ID de evaluación inválido.");
        }
        return dao.eliminarPorEvaluacion(idEvaluacion);
    }
    
    public int contarPorEvaluacion(int idEvaluacion) {
        if (idEvaluacion <= 0) {
            throw new IllegalArgumentException("ID de evaluación inválido.");
        }
        return dao.contarPorEvaluacion(idEvaluacion);
    }
}