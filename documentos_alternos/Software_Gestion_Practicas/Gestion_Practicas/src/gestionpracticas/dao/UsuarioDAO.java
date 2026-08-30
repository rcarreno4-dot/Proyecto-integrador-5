package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection con;

    public UsuarioDAO() {
        con = ConexionBD.getConnection();
    }

    private String selectBase() {
        return "SELECT ID_USUARIO, " +
             "     NOMBRE, " +
             "     APELLIDO, " +
             "     NVL(EMAIL, CORREO) AS EMAIL, " +
             "     PASSWORD AS CONTRASENA, " +
             "     NVL(TIPO_USUARIO, ROL) AS TIPO_USUARIO, " +
             "     NVL(ESTADO, 'ACTIVO') AS ESTADO " +
             "FROM USUARIO ";
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("ID_USUARIO"));
        u.setNombre(rs.getString("NOMBRE"));
        u.setApellido(rs.getString("APELLIDO"));
        u.setEmail(rs.getString("EMAIL"));
        u.setContrasena(rs.getString("CONTRASENA"));
        u.setTipoUsuario(rs.getString("TIPO_USUARIO"));
        u.setEstado(rs.getString("ESTADO"));
        return u;
    }

    private int obtenerSiguienteId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT NVL(MAX(ID_USUARIO), 0) + 1 AS NUEVO_ID FROM USUARIO");
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("NUEVO_ID");
            }
            return 1;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    // =====================================
    // LOGIN
    // =====================================
    public Usuario login(String email, String password) {
        Usuario usuario = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = selectBase() +
                  "WHERE (LOWER(EMAIL) = LOWER(?) OR LOWER(CORREO) = LOWER(?)) " +
                  "AND PASSWORD = ? " +
                  "AND NVL(UPPER(ESTADO), 'ACTIVO') IN ('ACTIVO', 'ACTIVA', '1')";

            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, email);
            ps.setString(3, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = mapear(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return usuario;
    }

    // =====================================
    // LISTAR TODOS
    // =====================================
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<Usuario>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = selectBase() + "ORDER BY NOMBRE, APELLIDO";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }

    // =====================================
    // BUSCAR POR ID
    // =====================================
    public Usuario buscarPorId(int id) {
        Usuario u = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = selectBase() + "WHERE ID_USUARIO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = mapear(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return u;
    }

    // =====================================
    // BUSCAR EMAIL / CORREO
    // =====================================
    public Usuario buscarPorEmail(String email) {
        Usuario u = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = selectBase() +
                  "WHERE LOWER(EMAIL) = LOWER(?) OR LOWER(CORREO) = LOWER(?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = mapear(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return u;
    }

    // =====================================
    // INSERTAR
    // =====================================
    public boolean insertar(Usuario u) {
        PreparedStatement ps = null;
        try {
            int nuevoId = u.getIdUsuario() > 0 ? u.getIdUsuario() : obtenerSiguienteId();
            String tipo = u.getTipoUsuario() != null ? u.getTipoUsuario().trim().toUpperCase() : "ESTUDIANTE";
            String estado = u.getEstado() != null ? u.getEstado().trim().toUpperCase() : "ACTIVO";

            String sql = "INSERT INTO USUARIO " +
                  "(ID_USUARIO, NOMBRE, APELLIDO, CORREO, PASSWORD, ROL, ESTADO, EMAIL, TIPO_USUARIO) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);
            ps.setInt(1, nuevoId);
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getContrasena());
            ps.setString(6, tipo);
            ps.setString(7, estado);
            ps.setString(8, u.getEmail());
            ps.setString(9, tipo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    // =====================================
    // ACTUALIZAR
    // =====================================
    public boolean actualizar(Usuario u) {
        PreparedStatement ps = null;
        try {
            String tipo = u.getTipoUsuario() != null ? u.getTipoUsuario().trim().toUpperCase() : "ESTUDIANTE";
            String estado = u.getEstado() != null ? u.getEstado().trim().toUpperCase() : "ACTIVO";

            String sql = "UPDATE USUARIO SET " +
                  "NOMBRE = ?, APELLIDO = ?, CORREO = ?, EMAIL = ?, " +
                  "ROL = ?, TIPO_USUARIO = ?, ESTADO = ? " +
                  "WHERE ID_USUARIO = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getEmail());
            ps.setString(5, tipo);
            ps.setString(6, tipo);
            ps.setString(7, estado);
            ps.setInt(8, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    // =====================================
    // ELIMINAR LÓGICO
    // =====================================
    public boolean eliminar(int id) {
        return cambiarEstado(id, "INACTIVO");
    }

    // =====================================
    // CAMBIAR CONTRASEÑA
    // =====================================
    public boolean cambiarContrasena(int id, String pass) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE USUARIO SET PASSWORD = ? WHERE ID_USUARIO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, pass);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }


    // =====================================
    // CAMBIAR CONTRASEÑA POR EMAIL / CORREO
    // =====================================
    public boolean cambiarContrasenaPorEmail(String email, String nuevaPassword) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE USUARIO " +
                  "SET PASSWORD = ? " +
                  "WHERE LOWER(EMAIL) = LOWER(?) OR LOWER(CORREO) = LOWER(?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevaPassword);
            ps.setString(2, email);
            ps.setString(3, email);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    // =====================================
    // CAMBIAR ESTADO
    // =====================================
    public boolean cambiarEstado(int id, String estado) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE USUARIO SET ESTADO = ? WHERE ID_USUARIO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    // =====================================
    // LISTAR POR TIPO / ROL
    // =====================================
    public List<Usuario> listarPorTipo(String tipo) {
        List<Usuario> lista = new ArrayList<Usuario>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = selectBase() +
                  "WHERE UPPER(NVL(TIPO_USUARIO, ROL)) = UPPER(?) " +
                  "ORDER BY NOMBRE, APELLIDO";
            ps = con.prepareStatement(sql);
            ps.setString(1, tipo);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }
}
