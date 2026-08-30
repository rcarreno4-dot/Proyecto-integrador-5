package com.gestionpracticas.modelo;

import java.util.Date;
import java.util.List;

public class AsignacionDocente {
    private int idAsignacionDocente;
    private Practica practica;
    private List<Usuario> usuarios;
    private String rolEnPractica;
    private Date fechaAsignacion;
    private String estado;
    private Usuario docente;

    public Usuario getDocente() {
        return docente;
    }

    public void setDocente(Usuario docente) {
        this.docente = docente;
    }

    public AsignacionDocente() {}

    // Getters y Setters
    public int getIdAsignacionDocente() { return idAsignacionDocente; }
    public void setIdAsignacionDocente(int idAsignacionDocente) { this.idAsignacionDocente = idAsignacionDocente; }

    public Practica getPractica() { return practica; }
    public void setPractica(Practica practica) { this.practica = practica; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }

    public String getRolEnPractica() { return rolEnPractica; }
    public void setRolEnPractica(String rolEnPractica) { this.rolEnPractica = rolEnPractica; }

    public Date getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(Date fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}