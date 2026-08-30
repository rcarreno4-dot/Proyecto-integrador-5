package com.gestionpracticas.modelo;

public class CriterioRubrica {
    private int idCriterio;
    private Rubrica rubrica;
    private String nombre;
    private String descripcion;
    private double puntajeMaximo;
    private double pesoPorcentaje;
    private int orden;
    private String estado;

    public CriterioRubrica() {}

    public int getIdCriterio() { return idCriterio; }
    public void setIdCriterio(int idCriterio) { this.idCriterio = idCriterio; }

    public Rubrica getRubrica() { return rubrica; }
    public void setRubrica(Rubrica rubrica) { this.rubrica = rubrica; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPuntajeMaximo() { return puntajeMaximo; }
    public void setPuntajeMaximo(double puntajeMaximo) { this.puntajeMaximo = puntajeMaximo; }

    public double getPesoPorcentaje() { return pesoPorcentaje; }
    public void setPesoPorcentaje(double pesoPorcentaje) { this.pesoPorcentaje = pesoPorcentaje; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}