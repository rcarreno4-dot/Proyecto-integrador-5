package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.modelo.Programa;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PracticaDAO {

    private final Connection con;

    public PracticaDAO() {
        this.con = ConexionBD.getConnection();
    }

    private boolean columnaExiste(String tabla, String columna) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) AS TOTAL FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, tabla.toUpperCase());
            ps.setString(2, columna.toUpperCase());
            rs = ps.executeQuery();
            return rs.next() && rs.getInt("TOTAL") > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    private int obtenerSiguienteId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT NVL(MAX(ID_PRACTICA), 0) + 1 AS NUEVO_ID FROM PRACTICA");
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

    public List<Practica> listarTodas() {
        return listarTodos();
    }

    public List<Practica> listarTodos() {
        List<Practica> lista = new ArrayList<Practica>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            boolean tienePrograma = columnaExiste("PRACTICA", "ID_PROGRAMA");
            String sql;
            if (tienePrograma) {
                sql = "SELECT p.ID_PRACTICA, p.TITULO, p.DESCRIPCION, p.TIPO_PRACTICA, " +
                      "p.HORAS_REQUERIDAS, NVL(p.ESTADO, 'ACTIVO') AS ESTADO, " +
                      "p.ID_PROGRAMA, pr.NOMBRE AS NOMBRE_PROGRAMA " +
                      "FROM PRACTICA p LEFT JOIN PROGRAMA pr ON p.ID_PROGRAMA = pr.ID_PROGRAMA " +
                      "ORDER BY p.ID_PRACTICA";
            } else {
                sql = "SELECT ID_PRACTICA, TITULO, DESCRIPCION, TIPO_PRACTICA, " +
                      "HORAS_REQUERIDAS, NVL(ESTADO, 'ACTIVO') AS ESTADO " +
                      "FROM PRACTICA ORDER BY ID_PRACTICA";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearBasico(rs, tienePrograma));
            }
        } catch (SQLException e) {
            System.err.println("Error PracticaDAO.listarTodos: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }

    public List<Practica> listarActivas() {
        List<Practica> lista = new ArrayList<Practica>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            boolean tienePrograma = columnaExiste("PRACTICA", "ID_PROGRAMA");
            String sql;
            if (tienePrograma) {
                sql = "SELECT p.ID_PRACTICA, p.TITULO, p.DESCRIPCION, p.TIPO_PRACTICA, " +
                      "p.HORAS_REQUERIDAS, NVL(p.ESTADO, 'ACTIVO') AS ESTADO, " +
                      "p.ID_PROGRAMA, pr.NOMBRE AS NOMBRE_PROGRAMA " +
                      "FROM PRACTICA p LEFT JOIN PROGRAMA pr ON p.ID_PROGRAMA = pr.ID_PROGRAMA " +
                      "WHERE NVL(UPPER(p.ESTADO), 'ACTIVO') IN ('ACTIVO','ACTIVA','PENDIENTE','EN CURSO') " +
                      "ORDER BY p.ID_PRACTICA";
            } else {
                sql = "SELECT ID_PRACTICA, TITULO, DESCRIPCION, TIPO_PRACTICA, " +
                      "HORAS_REQUERIDAS, NVL(ESTADO, 'ACTIVO') AS ESTADO " +
                      "FROM PRACTICA " +
                      "WHERE NVL(UPPER(ESTADO), 'ACTIVO') IN ('ACTIVO','ACTIVA','PENDIENTE','EN CURSO') " +
                      "ORDER BY ID_PRACTICA";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearBasico(rs, tienePrograma));
            }
        } catch (SQLException e) {
            System.err.println("Error PracticaDAO.listarActivas: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }

    public List<Practica> listarPorPrograma(int idPrograma) {
        List<Practica> lista = new ArrayList<Practica>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (!columnaExiste("PRACTICA", "ID_PROGRAMA")) {
                return lista;
            }
            String sql = "SELECT p.ID_PRACTICA, p.TITULO, p.DESCRIPCION, p.TIPO_PRACTICA, " +
                         "p.HORAS_REQUERIDAS, NVL(p.ESTADO, 'ACTIVO') AS ESTADO, " +
                         "p.ID_PROGRAMA, pr.NOMBRE AS NOMBRE_PROGRAMA " +
                         "FROM PRACTICA p LEFT JOIN PROGRAMA pr ON p.ID_PROGRAMA = pr.ID_PROGRAMA " +
                         "WHERE p.ID_PROGRAMA = ? ORDER BY p.ID_PRACTICA";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPrograma);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearBasico(rs, true));
            }
        } catch (SQLException e) {
            System.err.println("Error PracticaDAO.listarPorPrograma: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }

    public Practica buscarPorId(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            boolean tienePrograma = columnaExiste("PRACTICA", "ID_PROGRAMA");
            String sql;
            if (tienePrograma) {
                sql = "SELECT p.ID_PRACTICA, p.TITULO, p.DESCRIPCION, p.TIPO_PRACTICA, " +
                      "p.HORAS_REQUERIDAS, NVL(p.ESTADO, 'ACTIVO') AS ESTADO, " +
                      "p.ID_PROGRAMA, pr.NOMBRE AS NOMBRE_PROGRAMA " +
                      "FROM PRACTICA p LEFT JOIN PROGRAMA pr ON p.ID_PROGRAMA = pr.ID_PROGRAMA " +
                      "WHERE p.ID_PRACTICA = ?";
            } else {
                sql = "SELECT ID_PRACTICA, TITULO, DESCRIPCION, TIPO_PRACTICA, " +
                      "HORAS_REQUERIDAS, NVL(ESTADO, 'ACTIVO') AS ESTADO " +
                      "FROM PRACTICA WHERE ID_PRACTICA = ?";
            }
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapearBasico(rs, tienePrograma);
            }
        } catch (SQLException e) {
            System.err.println("Error PracticaDAO.buscarPorId: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return null;
    }

    public boolean insertar(Practica p) {
        PreparedStatement ps = null;
        try {
            int nuevoId = p.getIdPractica() > 0 ? p.getIdPractica() : obtenerSiguienteId();
            String estado = p.getEstado() != null ? p.getEstado() : "ACTIVO";
            boolean tienePrograma = columnaExiste("PRACTICA", "ID_PROGRAMA");

            String sql;
            if (tienePrograma) {
                sql = "INSERT INTO PRACTICA " +
                      "(ID_PRACTICA, TITULO, DESCRIPCION, TIPO_PRACTICA, HORAS_REQUERIDAS, ESTADO, ID_PROGRAMA) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, nuevoId);
                ps.setString(2, p.getTitulo());
                ps.setString(3, p.getDescripcion());
                ps.setString(4, p.getTipoPractica());
                ps.setInt(5, p.getHorasRequeridas());
                ps.setString(6, estado);
                ps.setInt(7, p.getPrograma().getIdPrograma());
            } else {
                sql = "INSERT INTO PRACTICA " +
                      "(ID_PRACTICA, TITULO, DESCRIPCION, TIPO_PRACTICA, HORAS_REQUERIDAS, ESTADO) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, nuevoId);
                ps.setString(2, p.getTitulo());
                ps.setString(3, p.getDescripcion());
                ps.setString(4, p.getTipoPractica());
                ps.setInt(5, p.getHorasRequeridas());
                ps.setString(6, estado);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error PracticaDAO.insertar: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    public boolean actualizar(Practica p) {
        PreparedStatement ps = null;
        try {
            String estado = p.getEstado() != null ? p.getEstado() : "ACTIVO";
            boolean tienePrograma = columnaExiste("PRACTICA", "ID_PROGRAMA");
            String sql;
            if (tienePrograma) {
                sql = "UPDATE PRACTICA SET TITULO = ?, DESCRIPCION = ?, TIPO_PRACTICA = ?, " +
                      "HORAS_REQUERIDAS = ?, ESTADO = ?, ID_PROGRAMA = ? WHERE ID_PRACTICA = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, p.getTitulo());
                ps.setString(2, p.getDescripcion());
                ps.setString(3, p.getTipoPractica());
                ps.setInt(4, p.getHorasRequeridas());
                ps.setString(5, estado);
                ps.setInt(6, p.getPrograma().getIdPrograma());
                ps.setInt(7, p.getIdPractica());
            } else {
                sql = "UPDATE PRACTICA SET TITULO = ?, DESCRIPCION = ?, TIPO_PRACTICA = ?, " +
                      "HORAS_REQUERIDAS = ?, ESTADO = ? WHERE ID_PRACTICA = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, p.getTitulo());
                ps.setString(2, p.getDescripcion());
                ps.setString(3, p.getTipoPractica());
                ps.setInt(4, p.getHorasRequeridas());
                ps.setString(5, estado);
                ps.setInt(6, p.getIdPractica());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error PracticaDAO.actualizar: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    public boolean cambiarEstado(int id, String estado) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE PRACTICA SET ESTADO = ? WHERE ID_PRACTICA = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error PracticaDAO.cambiarEstado: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    public boolean eliminar(int id) {
        return cambiarEstado(id, "INACTIVO");
    }

    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;

        try {
            String sql = "DELETE FROM PRACTICA WHERE ID_PRACTICA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            Utilidades.cerrar(ps);
        }    
    }

    private Practica mapearBasico(ResultSet rs, boolean tienePrograma) throws SQLException {
        Practica p = new Practica();
        p.setIdPractica(rs.getInt("ID_PRACTICA"));
        p.setTitulo(rs.getString("TITULO"));
        p.setDescripcion(rs.getString("DESCRIPCION"));
        p.setTipoPractica(rs.getString("TIPO_PRACTICA"));
        p.setHorasRequeridas(rs.getInt("HORAS_REQUERIDAS"));
        p.setEstado(rs.getString("ESTADO"));
        if (tienePrograma) {
            int idPrograma = rs.getInt("ID_PROGRAMA");
            if (!rs.wasNull()) {
                Programa programa = new Programa();
                programa.setIdPrograma(idPrograma);
                try {
                    programa.setNombre(rs.getString("NOMBRE_PROGRAMA"));
                } catch (SQLException ex) {
                    programa.setNombre("Programa " + idPrograma);
                }
                p.setPrograma(programa);
            }
        }
        return p;
    }
}
