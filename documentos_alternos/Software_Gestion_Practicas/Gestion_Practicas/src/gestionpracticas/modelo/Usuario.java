package com.gestionpracticas.modelo;

public class Usuario {

    private int idUsuario;

    private String nombre;

    private String apellido;

    private String email;

    private String contrasena;

    private String tipoUsuario;

    private String estado;
    
    private String telefono;
    private String documento;

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public Usuario() {
    }

    // =====================================
    // GETTERS Y SETTERS
    // =====================================

    public int getIdUsuario() {

        return idUsuario;
    }

    public void setIdUsuario(
        int idUsuario
    ) {

        this.idUsuario = idUsuario;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(
        String nombre
    ) {

        this.nombre = nombre;
    }

    public String getApellido() {

        return apellido;
    }

    public void setApellido(
        String apellido
    ) {

        this.apellido = apellido;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(
        String email
    ) {

        this.email = email;
    }

    public String getContrasena() {

        return contrasena;
    }

    public void setContrasena(
        String contrasena
    ) {

        this.contrasena = contrasena;
    }

    public String getTipoUsuario() {

        return tipoUsuario;
    }

    public void setTipoUsuario(
        String tipoUsuario
    ) {

        this.tipoUsuario = tipoUsuario;
    }

    public String getEstado() {

        return estado;
    }

    public void setEstado(
        String estado
    ) {

        this.estado = estado;
    }

    // =====================================
    // COMPATIBILIDAD CÓDIGO ANTIGUO
    // =====================================

    public String getPassword() {

        return contrasena;
    }

    public void setPassword(
        String password
    ) {

        this.contrasena = password;
    }

    public String getRol() {

        return tipoUsuario;
    }

    public void setRol(
        String rol
    ) {

        this.tipoUsuario = rol;
    }
}