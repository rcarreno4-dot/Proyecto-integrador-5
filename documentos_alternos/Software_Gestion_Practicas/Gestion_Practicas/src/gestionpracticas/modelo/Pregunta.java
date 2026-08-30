package com.gestionpracticas.modelo;

public class Pregunta {
    private int idPregunta;
    private String textoPregunta;
    private String tipoPregunta; // TEXTO, OPCION_MULTIPLE, NUMERICO
    private double puntajeMaximo;
    private int orden;
    private Evaluacion evaluacion;
    private Practica practica;
    private String estado;

    public Pregunta() {}

    public Pregunta(int idPregunta, String textoPregunta, String tipoPregunta,
                    double puntajeMaximo, int orden, Evaluacion evaluacion,
                    Practica practica, String estado) {
        this.idPregunta = idPregunta;
        this.textoPregunta = textoPregunta;
        this.tipoPregunta = tipoPregunta;
        this.puntajeMaximo = puntajeMaximo;
        this.orden = orden;
        this.evaluacion = evaluacion;
        this.practica = practica;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdPregunta() { return idPregunta; }
    public void setIdPregunta(int idPregunta) { this.idPregunta = idPregunta; }

    public String getTextoPregunta() { return textoPregunta; }
    public void setTextoPregunta(String textoPregunta) { this.textoPregunta = textoPregunta; }

    public String getTipoPregunta() { return tipoPregunta; }
    public void setTipoPregunta(String tipoPregunta) { this.tipoPregunta = tipoPregunta; }

    public double getPuntajeMaximo() { return puntajeMaximo; }
    public void setPuntajeMaximo(double puntajeMaximo) { this.puntajeMaximo = puntajeMaximo; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public Evaluacion getEvaluacion() { return evaluacion; }
    public void setEvaluacion(Evaluacion evaluacion) { this.evaluacion = evaluacion; }

    public Practica getPractica() { return practica; }
    public void setPractica(Practica practica) { this.practica = practica; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public boolean isActivo() {
        return "ACTIVO".equals(estado);
    }

    @Override
    public String toString() {
        return textoPregunta + " (" + puntajeMaximo + " pts)";
    }
}