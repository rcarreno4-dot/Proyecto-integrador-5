package com.gestionpracticas.logica;

import com.gestionpracticas.dao.CriterioRubricaDAO;
import com.gestionpracticas.modelo.CriterioRubrica;
import com.gestionpracticas.util.Utilidades;
import java.util.List;

public class CriterioRubricaLogica {
    
    private final CriterioRubricaDAO dao;
    
    public CriterioRubricaLogica() {
        this.dao = new CriterioRubricaDAO();
    }
    
    public boolean guardar(CriterioRubrica criterio) {
        if (Utilidades.esVacio(criterio.getNombre())) {
            throw new IllegalArgumentException("El nombre del criterio es obligatorio");
        }
        if (criterio.getRubrica() == null || criterio.getRubrica().getIdRubrica() <= 0) {
            throw new IllegalArgumentException("La rúbrica es obligatoria");
        }
        if (criterio.getPesoPorcentaje() <= 0 || criterio.getPesoPorcentaje() > 100) {
            throw new IllegalArgumentException("El peso debe ser entre 1 y 100");
        }
        if (criterio.getPuntajeMaximo() <= 0) {
            throw new IllegalArgumentException("El puntaje máximo debe ser mayor a 0");
        }
        return dao.insertar(criterio);
    }
    
    public List<CriterioRubrica> listarPorRubrica(int idRubrica) {
        if (idRubrica <= 0) {
            throw new IllegalArgumentException("ID de rúbrica inválido");
        }
        return dao.listarPorRubrica(idRubrica);
    }
    
    public List<CriterioRubrica> listarTodos() {
        return dao.listarTodos();
    }
    
    public CriterioRubrica buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de criterio inválido");
        }
        return dao.buscarPorId(id);
    }
    
    public boolean actualizar(CriterioRubrica criterio) {
        if (criterio.getIdCriterio() <= 0) {
            throw new IllegalArgumentException("ID de criterio inválido");
        }
        return dao.actualizar(criterio);
    }
    
    public boolean eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de criterio inválido");
        }
        return dao.eliminar(id);
    }
}