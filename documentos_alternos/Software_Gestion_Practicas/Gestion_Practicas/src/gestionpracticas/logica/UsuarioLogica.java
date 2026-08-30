package com.gestionpracticas.logica;

import com.gestionpracticas.dao.UsuarioDAO;
import com.gestionpracticas.modelo.Usuario;

import java.util.List;

public class UsuarioLogica {

    private UsuarioDAO dao;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public UsuarioLogica() {

        dao = new UsuarioDAO();
    }

    // =========================================
    // LOGIN
    // =========================================

    public Usuario iniciarSesion(
        String email,
        String password
    ) {

        if(email == null ||
           email.trim().equals("")) {

            throw new IllegalArgumentException(
              "El email no puede estar vacío"
            );
        }

        if(password == null ||
           password.trim().equals("")) {

            throw new IllegalArgumentException(
              "La contraseña no puede estar vacía"
            );
        }

        Usuario usuario =
        dao.login(
            email,
            password
        );

        if(usuario == null) {

            return null;
        }

        // Validar estado activo

        if(usuario.getEstado() != null) {

            if(
                usuario.getEstado()
                .equalsIgnoreCase("INACTIVO")
            ) {

                throw new IllegalArgumentException(
                  "Usuario inactivo"
                );
            }
        }

        return usuario;
    }

    // =========================================
    // LISTAR TODOS
    // =========================================

    public List<Usuario> obtenerTodos() {

        return dao.listarTodos();
    }

    // =========================================
    // BUSCAR POR ID
    // =========================================

    public Usuario obtenerPorId(
        int idUsuario
    ) {

        if(idUsuario <= 0) {

            throw new IllegalArgumentException(
              "ID inválido"
            );
        }

        return dao.buscarPorId(
            idUsuario
        );
    }

    // =========================================
    // BUSCAR POR EMAIL
    // =========================================

    public Usuario buscarPorEmail(
        String email
    ) {

        if(email == null ||
           email.trim().equals("")) {

            return null;
        }

        return dao.buscarPorEmail(
            email
        );
    }

    // =========================================
    // REGISTRAR USUARIO
    // =========================================

    public boolean registrar(
        Usuario usuario
    ) {

        if(usuario == null) {

            throw new IllegalArgumentException(
              "Usuario nulo"
            );
        }

        // ==========================
        // VALIDAR NOMBRE
        // ==========================

        if(usuario.getNombre() == null ||
           usuario.getNombre().trim().equals("")) {

            throw new IllegalArgumentException(
              "Nombre obligatorio"
            );
        }

        // ==========================
        // VALIDAR EMAIL
        // ==========================

        if(usuario.getEmail() == null ||
           usuario.getEmail().trim().equals("")) {

            throw new IllegalArgumentException(
              "Email obligatorio"
            );
        }

        // ==========================
        // VALIDAR CONTRASEÑA
        // ==========================

        if(usuario.getContrasena() == null ||
           usuario.getContrasena().trim().equals("")) {

            throw new IllegalArgumentException(
              "Contraseña obligatoria"
            );
        }

        // ==========================
        // VALIDAR TIPO
        // ==========================

        if(usuario.getTipoUsuario() == null ||
           usuario.getTipoUsuario().trim().equals("")) {

            throw new IllegalArgumentException(
              "Tipo usuario obligatorio"
            );
        }

        // ==========================
        // VALIDAR EMAIL REPETIDO
        // ==========================

        Usuario existente =
        dao.buscarPorEmail(
            usuario.getEmail()
        );

        if(existente != null) {

            throw new IllegalArgumentException(
              "El email ya existe"
            );
        }

        // ==========================
        // ESTADO POR DEFECTO
        // ==========================

        if(usuario.getEstado() == null ||
           usuario.getEstado().trim().equals("")) {

            usuario.setEstado(
              "ACTIVO"
            );
        }

        return dao.insertar(
            usuario
        );
    }

    // =========================================
    // ACTUALIZAR
    // =========================================

    public boolean actualizar(
        Usuario usuario
    ) {

        if(usuario == null) {

            throw new IllegalArgumentException(
              "Usuario nulo"
            );
        }

        if(usuario.getIdUsuario() <= 0) {

            throw new IllegalArgumentException(
              "ID inválido"
            );
        }

        return dao.actualizar(
            usuario
        );
    }

    // =========================================
    // ELIMINAR LÓGICO
    // =========================================

    public boolean eliminar(
        int idUsuario
    ) {

        if(idUsuario <= 0) {

            throw new IllegalArgumentException(
              "ID inválido"
            );
        }

        return dao.eliminar(
            idUsuario
        );
    }

    // =========================================
    // CAMBIAR CONTRASEÑA
    // =========================================

    public boolean cambiarContrasena(
        int idUsuario,
        String nueva
    ) {

        if(idUsuario <= 0) {

            throw new IllegalArgumentException(
              "ID inválido"
            );
        }

        if(nueva == null ||
           nueva.trim().equals("")) {

            throw new IllegalArgumentException(
              "Nueva contraseña vacía"
            );
        }

        return dao.cambiarContrasena(
            idUsuario,
            nueva
        );
    }


    // =========================================
    // RESTABLECER CONTRASEÑA DESDE LOGIN
    // =========================================
    public boolean restablecerContrasena(
        String email,
        String nueva,
        String confirmacion
    ) {

        if(email == null || email.trim().equals("")) {
            throw new IllegalArgumentException(
              "Ingrese el correo del usuario"
            );
        }

        if(nueva == null || nueva.trim().equals("")) {
            throw new IllegalArgumentException(
              "Ingrese la nueva contraseña"
            );
        }

        if(confirmacion == null || confirmacion.trim().equals("")) {
            throw new IllegalArgumentException(
              "Confirme la nueva contraseña"
            );
        }

        if(!nueva.equals(confirmacion)) {
            throw new IllegalArgumentException(
              "Las contraseñas no coinciden"
            );
        }

        if(nueva.trim().length() < 4) {
            throw new IllegalArgumentException(
              "La contraseña debe tener mínimo 4 caracteres"
            );
        }

        Usuario existente = dao.buscarPorEmail(email.trim());

        if(existente == null) {
            throw new IllegalArgumentException(
              "No existe un usuario registrado con ese correo"
            );
        }

        if(existente.getEstado() != null &&
           existente.getEstado().equalsIgnoreCase("INACTIVO")) {
            throw new IllegalArgumentException(
              "El usuario está inactivo. Contacte al director o coordinador"
            );
        }

        return dao.cambiarContrasenaPorEmail(
            email.trim(),
            nueva.trim()
        );
    }

    // =========================================
    // ACTIVAR USUARIO
    // =========================================

    public boolean activarUsuario(
        int idUsuario
    ) {

        return dao.cambiarEstado(
            idUsuario,
          "ACTIVO"
        );
    }

    // =========================================
    // DESACTIVAR USUARIO
    // =========================================

    public boolean desactivarUsuario(
        int idUsuario
    ) {

        return dao.cambiarEstado(
            idUsuario,
          "INACTIVO"
        );
    }

    // =========================================
    // LISTAR POR TIPO
    // =========================================

    public List<Usuario> listarPorTipo(
        String tipo
    ) {

        if(tipo == null ||
           tipo.trim().equals("")) {

            throw new IllegalArgumentException(
              "Tipo inválido"
            );
        }

        return dao.listarPorTipo(
            tipo
        );
    }
    public boolean desactivar(int id) {
        return dao.cambiarEstado(id, "INACTIVO");
    }
}