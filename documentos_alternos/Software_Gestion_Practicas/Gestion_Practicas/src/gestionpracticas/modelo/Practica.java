package com.gestionpracticas.modelo;

import java.util.Date;

public class Practica {
    private int idPractica;
    private String titulo;
    private String descripcion;
    private String tipoPractica;
    private Date fechaInicio;
    private Date fechaFin;
    private Usuario institucion;
    private Curso curso;
    private Grupo grupo;
    private int horasRequeridas;
    private String estado;

    public Practica() {}

    public Practica(int idPractica, String titulo, String descripcion, String tipoPractica,
                    Date fechaInicio, Date fechaFin, Usuario institucion, Curso curso, 
                    Grupo grupo, int horasRequeridas, String estado) {
        this.idPractica = idPractica;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoPractica = tipoPractica;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.institucion = institucion;
        this.curso = curso;
        this.grupo = grupo;
        this.horasRequeridas = horasRequeridas;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipoPractica() { return tipoPractica; }
    public void setTipoPractica(String tipoPractica) { this.tipoPractica = tipoPractica; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public Usuario getInstitucion() { return institucion; }
    public void setInstitucion(Usuario institucion) { this.institucion = institucion; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    public int getHorasRequeridas() { return horasRequeridas; }
    public void setHorasRequeridas(int horasRequeridas) { this.horasRequeridas = horasRequeridas; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public boolean isActiva() {
        return "ACTIVA".equals(estado);
    }
    
    public boolean isPendiente() {
        return "PENDIENTE".equals(estado);
    }

    @Override
    public String toString() {
        return titulo + " (" + estado + ")";
    }
    private Programa programa;

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }
}