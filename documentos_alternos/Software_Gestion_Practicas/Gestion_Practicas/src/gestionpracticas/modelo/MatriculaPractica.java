package com.gestionpracticas.modelo;

import java.util.Date;

public class MatriculaPractica {
    private int idMatriculaPractica;
    private Usuario estudiante;
    private Practica practica;
    private Grupo grupo;
    private Date fechaMatricula;
    private String estado; // ACTIVO, INACTIVO, APROBADO, RECHAZADO, FINALIZADO

    public MatriculaPractica() {}

    public MatriculaPractica(int idMatriculaPractica, Usuario estudiante, Practica practica, 
                             Grupo grupo, Date fechaMatricula, String estado) {
        this.idMatriculaPractica = idMatriculaPractica;
        this.estudiante = estudiante;
        this.practica = practica;
        this.grupo = grupo;
        this.fechaMatricula = fechaMatricula;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdMatriculaPractica() { return idMatriculaPractica; }
    public void setIdMatriculaPractica(int idMatriculaPractica) { this.idMatriculaPractica = idMatriculaPractica; }

    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }

    public Practica getPractica() { return practica; }
    public void setPractica(Practica practica) { this.practica = practica; }

    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    public Date getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(Date fechaMatricula) { this.fechaMatricula = fechaMatricula; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Matricula{" + "estudiante=" + estudiante.getNombre() + 
             ", practica=" + practica.getTitulo() + ", estado=" + estado + '}';
    }
}