package com.gestionpracticas.modelo;

public class NivelDesempeno {
    private int idNivel;
    private CriterioRubrica criterio;
    private String nombre;
    private String descripcion;
    private double puntajeMinimo;
    private double puntajeMaximo;
    private String estado;

    public NivelDesempeno() {}

    public int getIdNivel() { return idNivel; }
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }

    public CriterioRubrica getCriterio() { return criterio; }
    public void setCriterio(CriterioRubrica criterio) { this.criterio = criterio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPuntajeMinimo() { return puntajeMinimo; }
    public void setPuntajeMinimo(double puntajeMinimo) { this.puntajeMinimo = puntajeMinimo; }

    public double getPuntajeMaximo() { return puntajeMaximo; }
    public void setPuntajeMaximo(double puntajeMaximo) { this.puntajeMaximo = puntajeMaximo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean contienePuntaje(double puntaje) {
        return puntaje >= puntajeMinimo && puntaje <= puntajeMaximo;
    }

    @Override
    public String toString() {
        return nombre + " (" + puntajeMinimo + "-" + puntajeMaximo + " pts)";
    }
}