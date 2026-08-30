package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Institucion;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstitucionDAO {
    
    private final Connection con;
    
    public InstitucionDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    public boolean insertar(Institucion i) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO INSTITUCION (ID_INSTITUCION, NOMBRE, DIRECCION, TELEFONO, " +
                      "EMAIL, NIT, CIUDAD, REPRESENTANTE, SITIO_WEB, FECHA_REGISTRO, ESTADO) " +
                      "VALUES (SEQ_INSTITUCION.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 'ACTIVO')";
            ps = con.prepareStatement(sql);
            ps.setString(1, i.getNombre());
            ps.setString(2, i.getDireccion());
            ps.setString(3, i.getTelefono());
            ps.setString(4, i.getEmail());
            ps.setString(5, i.getNit());
            ps.setString(6, i.getCiudad());
            ps.setString(7, i.getRepresentante());
            ps.setString(8, i.getSitioWeb());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<Institucion> listarTodos() {
        List<Institucion> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM INSTITUCION WHERE ESTADO = 'ACTIVO' ORDER BY NOMBRE";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.listarTodos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Institucion> listarPorCiudad(String ciudad) {
        List<Institucion> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM INSTITUCION WHERE CIUDAD = ? AND ESTADO = 'ACTIVO' ORDER BY NOMBRE";
            ps = con.prepareStatement(sql);
            ps.setString(1, ciudad);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.listarPorCiudad: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Institucion> listarActivas() {
        List<Institucion> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM INSTITUCION WHERE ESTADO = 'ACTIVO' ORDER BY NOMBRE";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.listarActivas: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }

    public Institucion buscarPorId(int id) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM INSTITUCION WHERE ID_INSTITUCION = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.buscarPorId: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }
    
    public Institucion buscarPorNit(String nit) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM INSTITUCION WHERE NIT = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setString(1, nit);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.buscarPorNit: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }
    
    public Institucion buscarPorEmail(String email) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM INSTITUCION WHERE EMAIL = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.buscarPorEmail: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }

    public boolean actualizar(Institucion i) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE INSTITUCION SET NOMBRE = ?, DIRECCION = ?, TELEFONO = ?, " +
                      "EMAIL = ?, NIT = ?, CIUDAD = ?, REPRESENTANTE = ?, SITIO_WEB = ? " +
                      "WHERE ID_INSTITUCION = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, i.getNombre());
            ps.setString(2, i.getDireccion());
            ps.setString(3, i.getTelefono());
            ps.setString(4, i.getEmail());
            ps.setString(5, i.getNit());
            ps.setString(6, i.getCiudad());
            ps.setString(7, i.getRepresentante());
            ps.setString(8, i.getSitioWeb());
            ps.setInt(9, i.getIdInstitucion());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.actualizar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            // Eliminación lógica
            String sql = "UPDATE INSTITUCION SET ESTADO = 'INACTIVO' WHERE ID_INSTITUCION = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM INSTITUCION WHERE ID_INSTITUCION = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.eliminarFisico: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public int contarActivas() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) AS TOTAL FROM INSTITUCION WHERE ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("TOTAL");
            }
        } catch (SQLException e) { 
            System.err.println("Error InstitucionDAO.contarActivas: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return 0;
    }

    private Institucion mapear(ResultSet rs) throws SQLException {
        Institucion i = new Institucion();
        i.setIdInstitucion(rs.getInt("ID_INSTITUCION"));
        i.setNombre(rs.getString("NOMBRE"));
        i.setDireccion(rs.getString("DIRECCION"));
        i.setTelefono(rs.getString("TELEFONO"));
        i.setEmail(rs.getString("EMAIL"));
        i.setNit(rs.getString("NIT"));
        i.setCiudad(rs.getString("CIUDAD"));
        i.setRepresentante(rs.getString("REPRESENTANTE"));
        i.setSitioWeb(rs.getString("SITIO_WEB"));
        i.setFechaRegistro(rs.getDate("FECHA_REGISTRO"));
        i.setEstado(rs.getString("ESTADO"));
        return i;
    }
}