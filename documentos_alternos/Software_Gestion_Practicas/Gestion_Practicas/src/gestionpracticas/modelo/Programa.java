package com.gestionpracticas.modelo;

public class Programa {
    private int idPrograma;
    private String nombre;
    private String codigo;
    private String descripcion;
    private String facultad;
    private int duracionSemestres;
    private String estado;

    public Programa() {}

    public Programa(int idPrograma, String nombre, String codigo, String descripcion,
                    String facultad, int duracionSemestres, String estado) {
        this.idPrograma = idPrograma;
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.facultad = facultad;
        this.duracionSemestres = duracionSemestres;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdPrograma() { return idPrograma; }
    public void setIdPrograma(int idPrograma) { this.idPrograma = idPrograma; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }

    public int getDuracionSemestres() { return duracionSemestres; }
    public void setDuracionSemestres(int duracionSemestres) { this.duracionSemestres = duracionSemestres; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public boolean isActivo() {
        return "ACTIVO".equals(estado);
    }

    @Override
    public String toString() {

        return nombre;
    }
}