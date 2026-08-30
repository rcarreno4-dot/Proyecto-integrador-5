package com.gestionpracticas.modelo;

import java.util.Date;

public class Institucion {
    private int idInstitucion;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String nit;
    private String ciudad;
    private String representante;
    private String sitioWeb;
    private Date fechaRegistro;
    private String estado;

    public Institucion() {}

    public Institucion(int idInstitucion, String nombre, String direccion,
                       String telefono, String email, String nit,
                       String ciudad, String representante, String sitioWeb,
                       Date fechaRegistro, String estado) {
        this.idInstitucion = idInstitucion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.nit = nit;
        this.ciudad = ciudad;
        this.representante = representante;
        this.sitioWeb = sitioWeb;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdInstitucion() { return idInstitucion; }
    public void setIdInstitucion(int idInstitucion) { this.idInstitucion = idInstitucion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getRepresentante() { return representante; }
    public void setRepresentante(String representante) { this.representante = representante; }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public boolean isActivo() {
        return "ACTIVO".equals(estado);
    }

    @Override
    public String toString() {
        return nombre + " - " + ciudad;
    }
}