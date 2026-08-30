package com.gestionpracticas.logica;

import com.gestionpracticas.dao.MatriculaPracticaDAO;
import com.gestionpracticas.modelo.MatriculaPractica;
import java.util.List;

public class MatriculaPracticaLogica {
    
    private final MatriculaPracticaDAO dao;
    
    public MatriculaPracticaLogica() {
        this.dao = new MatriculaPracticaDAO();
    }
    public List<MatriculaPractica> obtenerTodas() {
    return dao.listarTodas();
    }
    
    // AGREGAR ESTE MÉTODO
    public List<MatriculaPractica> listarPorEstudiante(int idEstudiante) {
        if (idEstudiante <= 0) {
            throw new IllegalArgumentException("ID de estudiante inválido.");
        }
        return dao.listarPorEstudiante(idEstudiante);
    }
}