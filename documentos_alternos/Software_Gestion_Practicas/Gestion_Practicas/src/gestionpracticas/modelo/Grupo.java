package com.gestionpracticas.modelo;

public class Grupo {
    private int idGrupo;
    private String nombre;
    private Curso curso;
    private Usuario docente;
    private String semestre;
    private int anio;
    private String estado;

    public Grupo() {}

    public Grupo(int idGrupo, String nombre, Curso curso, Usuario docente, 
                 String semestre, int anio, String estado) {
        this.idGrupo = idGrupo;
        this.nombre = nombre;
        this.curso = curso;
        this.docente = docente;
        this.semestre = semestre;
        this.anio = anio;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public Usuario getDocente() { return docente; }
    public void setDocente(Usuario docente) { this.docente = docente; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return nombre + " - " + curso.getNombre() + " (" + semestre + "/" + anio + ")";
    }
}