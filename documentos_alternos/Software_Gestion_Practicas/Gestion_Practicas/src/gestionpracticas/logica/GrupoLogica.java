package com.gestionpracticas.logica;

import com.gestionpracticas.dao.GrupoDAO;
import com.gestionpracticas.modelo.Grupo;
import java.util.List;

public class GrupoLogica {
    
    private final GrupoDAO dao;
    
    public GrupoLogica() {
        this.dao = new GrupoDAO();
    }
    
    // AGREGAR ESTE MÉTODO
    public List<Grupo> obtenerTodos() {
        return dao.listarTodos();
    }
}