package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Programa;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO {
    
    private final Connection con;
    
    public ProgramaDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    private int obtenerSiguienteId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT NVL(MAX(ID_PROGRAMA), 0) + 1 AS NUEVO_ID FROM PROGRAMA");
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

    public boolean insertar(Programa p) {
        PreparedStatement ps = null;
        try {
            int nuevoId = p.getIdPrograma() > 0 ? p.getIdPrograma() : obtenerSiguienteId();
            String sql = "INSERT INTO PROGRAMA (ID_PROGRAMA, NOMBRE, CODIGO, DESCRIPCION, " +
                      "FACULTAD, DURACION_SEMESTRES, ESTADO) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, nuevoId);
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCodigo());
            ps.setString(4, p.getDescripcion());
            ps.setString(5, p.getFacultad());
            ps.setInt(6, p.getDuracionSemestres());
            ps.setString(7, p.getEstado());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<Programa> listarTodos() {
        List<Programa> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM PROGRAMA WHERE ESTADO = 'ACTIVO' ORDER BY NOMBRE";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.listarTodos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Programa> listarActivos() {
        List<Programa> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM PROGRAMA WHERE ESTADO = 'ACTIVO' ORDER BY NOMBRE";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.listarActivos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Programa> listarPorFacultad(String facultad) {
        List<Programa> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM PROGRAMA WHERE FACULTAD = ? AND ESTADO = 'ACTIVO' ORDER BY NOMBRE";
            ps = con.prepareStatement(sql);
            ps.setString(1, facultad);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.listarPorFacultad: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Programa> buscarPorNombre(String nombre) {
        List<Programa> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM PROGRAMA WHERE UPPER(NOMBRE) LIKE ? AND ESTADO = 'ACTIVO' ORDER BY NOMBRE";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nombre.toUpperCase() + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.buscarPorNombre: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }

    public Programa buscarPorId(int id) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM PROGRAMA WHERE ID_PROGRAMA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.buscarPorId: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }
    
    public Programa buscarPorCodigo(String codigo) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM PROGRAMA WHERE CODIGO = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.buscarPorCodigo: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }

    public boolean actualizar(Programa p) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE PROGRAMA SET NOMBRE = ?, CODIGO = ?, DESCRIPCION = ?, " +
                      "FACULTAD = ?, DURACION_SEMESTRES = ?, ESTADO = ? " +
                      "WHERE ID_PROGRAMA = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCodigo());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getFacultad());
            ps.setInt(5, p.getDuracionSemestres());
            ps.setString(6, p.getEstado());
            ps.setInt(7, p.getIdPrograma());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.actualizar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean cambiarEstado(int id, String estado) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE PROGRAMA SET ESTADO = ? WHERE ID_PROGRAMA = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id);
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.cambiarEstado: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            // Eliminación lógica
            String sql = "UPDATE PROGRAMA SET ESTADO = 'INACTIVO' WHERE ID_PROGRAMA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM PROGRAMA WHERE ID_PROGRAMA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.eliminarFisico: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public int contarActivos() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) AS TOTAL FROM PROGRAMA WHERE ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("TOTAL");
            }
        } catch (SQLException e) { 
            System.err.println("Error ProgramaDAO.contarActivos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return 0;
    }

    private Programa mapear(ResultSet rs) throws SQLException {

        Programa p = new Programa();

        p.setIdPrograma(rs.getInt("ID_PROGRAMA"));
        p.setNombre(rs.getString("NOMBRE"));
        p.setCodigo(rs.getString("CODIGO"));
        p.setDescripcion(rs.getString("DESCRIPCION"));
        p.setFacultad(rs.getString("FACULTAD"));
        p.setDuracionSemestres(rs.getInt("DURACION_SEMESTRES"));
        p.setEstado(rs.getString("ESTADO"));

        return p;
    }
}