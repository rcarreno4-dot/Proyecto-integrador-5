package com.gestionpracticas.logica;

import com.gestionpracticas.dao.RegistroActividadDAO;
import com.gestionpracticas.modelo.RegistroActividad;
import java.util.Date;
import java.util.List;

public class RegistroActividadLogica {
    
    private final RegistroActividadDAO dao;
    
    public RegistroActividadLogica() {
        this.dao = new RegistroActividadDAO();
    }
    
    public boolean registrar(RegistroActividad r) {
        // Validaciones
        if (r.getMatricula() == null || r.getMatricula().getIdMatriculaPractica() <= 0) {
            throw new IllegalArgumentException("La matrícula es obligatoria.");
        }
        if (r.getDescripcion() == null || r.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        if (r.getHoras() <= 0) {
            throw new IllegalArgumentException("Las horas deben ser mayores a 0.");
        }
        if (r.getHoras() > 8) {
            throw new IllegalArgumentException("No se pueden registrar más de 8 horas por día.");
        }
        if (r.getTipoActividad() == null || r.getTipoActividad().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de actividad es obligatorio.");
        }
        
        r.setEstado("PENDIENTE");
        r.setFechaActividad(new Date());
        return dao.insertar(r);
    }
    
    public List<RegistroActividad> listarPorMatricula(int idMatriculaPractica) {
        if (idMatriculaPractica <= 0) {
            throw new IllegalArgumentException("ID de matrícula inválido.");
        }
        return dao.listarPorMatricula(idMatriculaPractica);
    }
    
    public List<RegistroActividad> listarPorEstudiante(int idEstudiante) {
        if (idEstudiante <= 0) {
            throw new IllegalArgumentException("ID de estudiante inválido.");
        }
        return dao.listarPorEstudiante(idEstudiante);
    }
    
    public List<RegistroActividad> listarPorPractica(int idPractica) {
        if (idPractica <= 0) {
            throw new IllegalArgumentException("ID de práctica inválido.");
        }
        return dao.listarPorPractica(idPractica);
    }
    
    public List<RegistroActividad> listarPendientes() {
        return dao.listarPorEstado("PENDIENTE");
    }
    
    public List<RegistroActividad> listarAprobados() {
        return dao.listarPorEstado("APROBADO");
    }
    
    public List<RegistroActividad> listarTodas() {
        return dao.listarTodas();
    }
    
    public RegistroActividad buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de registro inválido.");
        }
        return dao.buscarPorId(id);
    }
    
    public boolean actualizar(RegistroActividad r) {
        if (r.getIdRegistro() <= 0) {
            throw new IllegalArgumentException("ID de registro inválido.");
        }
        return dao.actualizar(r);
    }
    
    public boolean aprobar(int idRegistro, String observaciones) {
        if (idRegistro <= 0) {
            throw new IllegalArgumentException("ID de registro inválido.");
        }
        return dao.aprobar(idRegistro, observaciones);
    }
    
    public boolean rechazar(int idRegistro, String motivo) {
        if (idRegistro <= 0) {
            throw new IllegalArgumentException("ID de registro inválido.");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un motivo para rechazar.");
        }
        return dao.rechazar(idRegistro, motivo);
    }
    
    public double sumHorasPorMatricula(int idMatriculaPractica) {
        if (idMatriculaPractica <= 0) {
            throw new IllegalArgumentException("ID de matrícula inválido.");
        }
        return dao.sumHorasPorMatricula(idMatriculaPractica);
    }
    
    public double sumHorasPorEstudiante(int idEstudiante) {
    if (idEstudiante <= 0) {
        throw new IllegalArgumentException("ID de estudiante inválido");
    }
    return dao.sumHorasPorEstudiante(idEstudiante);
    }
    
    public boolean eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de registro inválido.");
        }
        return dao.eliminar(id);
    }
}