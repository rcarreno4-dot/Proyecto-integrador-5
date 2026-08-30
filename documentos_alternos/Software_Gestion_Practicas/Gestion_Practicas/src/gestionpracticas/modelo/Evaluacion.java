package com.gestionpracticas.modelo;

import java.util.Date;

public class Evaluacion {
    private int idEvaluacion;
    private MatriculaPractica matricula;
    private Rubrica rubrica;
    private Usuario evaluador;
    private double puntajeObtenido;
    private String observaciones;
    private Date fechaEvaluacion;
    private String estado; // PENDIENTE, REALIZADA, APROBADA, REPROBADA

    public Evaluacion() {}

    // Getters y Setters
    public int getIdEvaluacion() { return idEvaluacion; }
    public void setIdEvaluacion(int idEvaluacion) { this.idEvaluacion = idEvaluacion; }

    public MatriculaPractica getMatricula() { return matricula; }
    public void setMatricula(MatriculaPractica matricula) { this.matricula = matricula; }

    public Rubrica getRubrica() { return rubrica; }
    public void setRubrica(Rubrica rubrica) { this.rubrica = rubrica; }

    public Usuario getEvaluador() { return evaluador; }
    public void setEvaluador(Usuario evaluador) { this.evaluador = evaluador; }

    public double getPuntajeObtenido() { return puntajeObtenido; }
    public void setPuntajeObtenido(double puntajeObtenido) { this.puntajeObtenido = puntajeObtenido; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Date getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(Date fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Evaluacion{" + "id=" + idEvaluacion + ", puntaje=" + puntajeObtenido + ", estado=" + estado + '}';
    }
}