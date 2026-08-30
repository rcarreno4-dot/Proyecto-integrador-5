package com.gestionpracticas.logica;

import com.gestionpracticas.dao.RubricaDAO;
import com.gestionpracticas.modelo.Rubrica;
import java.util.List;

public class RubricaLogica {
    
    private final RubricaDAO dao;
    
    public RubricaLogica() {
        this.dao = new RubricaDAO();
    }
    
    public boolean guardar(Rubrica r) {
        return dao.insertar(r);
    }
    
    public List<Rubrica> listarTodas() {
        return dao.listarTodas();
    }
    
    public List<Rubrica> listarPorDocente(int idDocente) {
        return dao.listarPorDocente(idDocente);
    }
    
    public List<Rubrica> listarPorPractica(int idPractica) {
        return dao.listarPorPractica(idPractica);
    }
    
    public List<Rubrica> listarActivas() {
        return dao.listarActivas();
    }
    
    public Rubrica buscarPorId(int id) {
        return dao.buscarPorId(id);
    }
    
    public Rubrica buscarPorPractica(int idPractica) {
        return dao.buscarPorPractica(idPractica);
    }
    
    public boolean actualizar(Rubrica r) {
        return dao.actualizar(r);
    }
    
    public boolean eliminar(int id) {
        return dao.eliminar(id);
    }
    
    public boolean eliminarFisico(int id) {
        return dao.eliminarFisico(id);
    }
    
    public int contarPorDocente(int idDocente) {
        return dao.contarPorDocente(idDocente);
    }
    
    public int contarPorPractica(int idPractica) {
        return dao.contarPorPractica(idPractica);
    }
    public boolean existeRubricaParaPractica(int idPractica) {
        return false;
    }

    public boolean rubricaEnUso(int id) {
        return false;
    }
}