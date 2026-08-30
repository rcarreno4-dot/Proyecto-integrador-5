package com.gestionpracticas.logica;

import com.gestionpracticas.dao.EvaluacionDAO;
import com.gestionpracticas.modelo.Evaluacion;
import com.gestionpracticas.util.Utilidades;
import java.util.List;

public class EvaluacionLogica {

    private final EvaluacionDAO dao;

    public EvaluacionLogica() {
        this.dao = new EvaluacionDAO();
    }
    public List<Evaluacion> listarTodas() {
    return dao.listarTodas();
    }

    public boolean registrarEvaluacion(Evaluacion e) {
        if (e.getMatricula() == null) {
            throw new IllegalArgumentException("La matrícula es obligatoria.");
        }
        if (e.getRubrica() == null) {
            throw new IllegalArgumentException("La rúbrica es obligatoria.");
        }
        if (e.getEvaluador() == null) {
            throw new IllegalArgumentException("El evaluador es obligatorio.");
        }
        if (e.getPuntajeObtenido() < 0) {
            throw new IllegalArgumentException("El puntaje no puede ser negativo.");
        }
        if (e.getPuntajeObtenido() > 100) {
            throw new IllegalArgumentException("El puntaje no puede superar 100.");
        }
        
        e.setEstado(Utilidades.ESTADO_REALIZADA);
        return dao.insertar(e);
    }

    // AGREGAR ESTE MÉTODO
    public List<Evaluacion> listarPorEvaluador(int idEvaluador) {
        if (idEvaluador <= 0) {
            throw new IllegalArgumentException("ID de evaluador inválido.");
        }
        return dao.listarPorEvaluador(idEvaluador);
    }

    // ... resto de métodos
}