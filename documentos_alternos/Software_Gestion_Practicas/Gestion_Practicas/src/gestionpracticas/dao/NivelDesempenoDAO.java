package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.CriterioRubrica;
import com.gestionpracticas.modelo.NivelDesempeno;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NivelDesempenoDAO {
    
    private final Connection con;
    
    public NivelDesempenoDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    public boolean insertar(NivelDesempeno n) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO NIVEL_DESEMPENO (ID_NIVEL, ID_CRITERIO, NOMBRE, DESCRIPCION, " +
                      "PUNTAJE_MINIMO, PUNTAJE_MAXIMO, ESTADO) " +
                      "VALUES (SEQ_NIVEL_DESEMPENO.NEXTVAL, ?, ?, ?, ?, ?, 'ACTIVO')";
            ps = con.prepareStatement(sql);
            ps.setInt(1, n.getCriterio().getIdCriterio());
            ps.setString(2, n.getNombre());
            ps.setString(3, n.getDescripcion());
            ps.setDouble(4, n.getPuntajeMinimo());
            ps.setDouble(5, n.getPuntajeMaximo());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean insertarVarios(List<NivelDesempeno> niveles) {
        if (niveles == null || niveles.isEmpty()) {
            return false;
        }
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO NIVEL_DESEMPENO (ID_NIVEL, ID_CRITERIO, NOMBRE, DESCRIPCION, " +
                      "PUNTAJE_MINIMO, PUNTAJE_MAXIMO, ESTADO) " +
                      "VALUES (SEQ_NIVEL_DESEMPENO.NEXTVAL, ?, ?, ?, ?, ?, 'ACTIVO')";
            ps = con.prepareStatement(sql);
            
            for (NivelDesempeno n : niveles) {
                ps.setInt(1, n.getCriterio().getIdCriterio());
                ps.setString(2, n.getNombre());
                ps.setString(3, n.getDescripcion());
                ps.setDouble(4, n.getPuntajeMinimo());
                ps.setDouble(5, n.getPuntajeMaximo());
                ps.addBatch();
            }
            
            int[] resultados = ps.executeBatch();
            return resultados.length > 0;
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.insertarVarios: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<NivelDesempeno> listarPorCriterio(int idCriterio) {
        List<NivelDesempeno> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM NIVEL_DESEMPENO WHERE ID_CRITERIO = ? AND ESTADO = 'ACTIVO' ORDER BY PUNTAJE_MINIMO ASC";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCriterio);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                NivelDesempeno n = new NivelDesempeno();
                n.setIdNivel(rs.getInt("ID_NIVEL"));
                n.setNombre(rs.getString("NOMBRE"));
                n.setDescripcion(rs.getString("DESCRIPCION"));
                n.setPuntajeMinimo(rs.getDouble("PUNTAJE_MINIMO"));
                n.setPuntajeMaximo(rs.getDouble("PUNTAJE_MAXIMO"));
                n.setEstado(rs.getString("ESTADO"));
                
                CriterioRubrica c = new CriterioRubrica();
                c.setIdCriterio(idCriterio);
                n.setCriterio(c);
                
                lista.add(n);
            }
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.listarPorCriterio: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<NivelDesempeno> listarTodos() {
        List<NivelDesempeno> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT N.*, C.NOMBRE AS CRITERIO_NOMBRE FROM NIVEL_DESEMPENO N " +
                      "INNER JOIN CRITERIO_RUBRICA C ON N.ID_CRITERIO = C.ID_CRITERIO " +
                      "WHERE N.ESTADO = 'ACTIVO' " +
                      "ORDER BY N.ID_CRITERIO, N.PUNTAJE_MINIMO";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                NivelDesempeno n = mapearConCriterio(rs);
                lista.add(n);
            }
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.listarTodos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public NivelDesempeno buscarPorId(int id) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT N.*, C.NOMBRE AS CRITERIO_NOMBRE FROM NIVEL_DESEMPENO N " +
                      "INNER JOIN CRITERIO_RUBRICA C ON N.ID_CRITERIO = C.ID_CRITERIO " +
                      "WHERE N.ID_NIVEL = ? AND N.ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearConCriterio(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.buscarPorId: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }
    
    public NivelDesempeno buscarPorPuntaje(int idCriterio, double puntaje) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM NIVEL_DESEMPENO WHERE ID_CRITERIO = ? " +
                      "AND PUNTAJE_MINIMO <= ? AND PUNTAJE_MAXIMO >= ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCriterio);
            ps.setDouble(2, puntaje);
            ps.setDouble(3, puntaje);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                NivelDesempeno n = new NivelDesempeno();
                n.setIdNivel(rs.getInt("ID_NIVEL"));
                n.setNombre(rs.getString("NOMBRE"));
                n.setDescripcion(rs.getString("DESCRIPCION"));
                n.setPuntajeMinimo(rs.getDouble("PUNTAJE_MINIMO"));
                n.setPuntajeMaximo(rs.getDouble("PUNTAJE_MAXIMO"));
                return n;
            }
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.buscarPorPuntaje: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }

    public boolean actualizar(NivelDesempeno n) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE NIVEL_DESEMPENO SET NOMBRE = ?, DESCRIPCION = ?, " +
                      "PUNTAJE_MINIMO = ?, PUNTAJE_MAXIMO = ? WHERE ID_NIVEL = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, n.getNombre());
            ps.setString(2, n.getDescripcion());
            ps.setDouble(3, n.getPuntajeMinimo());
            ps.setDouble(4, n.getPuntajeMaximo());
            ps.setInt(5, n.getIdNivel());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.actualizar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE NIVEL_DESEMPENO SET ESTADO = 'INACTIVO' WHERE ID_NIVEL = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean eliminarPorCriterio(int idCriterio) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE NIVEL_DESEMPENO SET ESTADO = 'INACTIVO' WHERE ID_CRITERIO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCriterio);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.eliminarPorCriterio: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM NIVEL_DESEMPENO WHERE ID_NIVEL = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error NivelDesempenoDAO.eliminarFisico: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    private NivelDesempeno mapearConCriterio(ResultSet rs) throws SQLException {
        NivelDesempeno n = new NivelDesempeno();
        n.setIdNivel(rs.getInt("ID_NIVEL"));
        n.setNombre(rs.getString("NOMBRE"));
        n.setDescripcion(rs.getString("DESCRIPCION"));
        n.setPuntajeMinimo(rs.getDouble("PUNTAJE_MINIMO"));
        n.setPuntajeMaximo(rs.getDouble("PUNTAJE_MAXIMO"));
        n.setEstado(rs.getString("ESTADO"));
        
        CriterioRubrica c = new CriterioRubrica();
        c.setIdCriterio(rs.getInt("ID_CRITERIO"));
        c.setNombre(rs.getString("CRITERIO_NOMBRE"));
        n.setCriterio(c);
        
        return n;
    }
}