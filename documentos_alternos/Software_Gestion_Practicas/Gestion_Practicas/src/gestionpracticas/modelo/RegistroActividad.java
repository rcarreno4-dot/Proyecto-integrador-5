package com.gestionpracticas.modelo;

import java.util.Date;

public class RegistroActividad {
    private int idRegistro;
    private MatriculaPractica matricula;
    private Date fechaActividad;
    private String descripcion;
    private double horas;
    private String tipoActividad; // DESARROLLO, REUNION, CAPACITACION, DOCUMENTACION
    private String evidencia;
    private String estado; // PENDIENTE, APROBADO, RECHAZADO, ELIMINADO
    private String observaciones;

    public RegistroActividad() {}

    public RegistroActividad(int idRegistro, MatriculaPractica matricula, Date fechaActividad,
                             String descripcion, double horas, String tipoActividad,
                             String evidencia, String estado, String observaciones) {
        this.idRegistro = idRegistro;
        this.matricula = matricula;
        this.fechaActividad = fechaActividad;
        this.descripcion = descripcion;
        this.horas = horas;
        this.tipoActividad = tipoActividad;
        this.evidencia = evidencia;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getIdRegistro() { return idRegistro; }
    public void setIdRegistro(int idRegistro) { this.idRegistro = idRegistro; }

    public MatriculaPractica getMatricula() { return matricula; }
    public void setMatricula(MatriculaPractica matricula) { this.matricula = matricula; }

    public Date getFechaActividad() { return fechaActividad; }
    public void setFechaActividad(Date fechaActividad) { this.fechaActividad = fechaActividad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getHoras() { return horas; }
    public void setHoras(double horas) { this.horas = horas; }

    public String getTipoActividad() { return tipoActividad; }
    public void setTipoActividad(String tipoActividad) { this.tipoActividad = tipoActividad; }

    public String getEvidencia() { return evidencia; }
    public void setEvidencia(String evidencia) { this.evidencia = evidencia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public boolean isAprobado() {
        return "APROBADO".equals(estado);
    }
    
    public boolean isPendiente() {
        return "PENDIENTE".equals(estado);
    }

    @Override
    public String toString() {
        return tipoActividad + " - " + horas + " horas (" + estado + ")";
    }
}