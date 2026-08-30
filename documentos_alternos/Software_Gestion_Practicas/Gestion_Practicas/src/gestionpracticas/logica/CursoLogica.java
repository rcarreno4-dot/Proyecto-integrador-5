package com.gestionpracticas.logica;

import com.gestionpracticas.dao.CursoDAO;
import com.gestionpracticas.modelo.Curso;
import com.gestionpracticas.util.Utilidades;
import java.util.List;

public class CursoLogica {
    
    private final CursoDAO dao;
    
    public CursoLogica() {
        this.dao = new CursoDAO();
    }
    
    public boolean guardar(Curso curso) {
        if (Utilidades.esVacio(curso.getNombre())) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio");
        }
        if (Utilidades.esVacio(curso.getCodigo())) {
            throw new IllegalArgumentException("El código del curso es obligatorio");
        }
        if (curso.getCreditos() <= 0) {
            throw new IllegalArgumentException("Los créditos deben ser mayores a 0");
        }
        if (curso.getCreditos() > 10) {
            throw new IllegalArgumentException("Los créditos no pueden superar 10");
        }
        if (curso.getPrograma() == null || curso.getPrograma().getIdPrograma() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un programa");
        }
        return dao.insertar(curso);
    }
    
    public List<Curso> listarTodos() {
        return dao.listarTodos();
    }
    
    public List<Curso> listarActivos() {
        return dao.listarActivos();
    }
    
    public Curso buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de curso inválido");
        }
        return dao.buscarPorId(id);
    }
    
    public List<Curso> buscarPorPrograma(int idPrograma) {
        if (idPrograma <= 0) {
            throw new IllegalArgumentException("ID de programa inválido");
        }
        return dao.buscarPorPrograma(idPrograma);
    }
    
    public boolean actualizar(Curso curso) {
        if (curso.getIdCurso() <= 0) {
            throw new IllegalArgumentException("ID de curso inválido");
        }
        return dao.actualizar(curso);
    }
    
    public boolean eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de curso inválido");
        }
        return dao.eliminar(id);
    }
}
