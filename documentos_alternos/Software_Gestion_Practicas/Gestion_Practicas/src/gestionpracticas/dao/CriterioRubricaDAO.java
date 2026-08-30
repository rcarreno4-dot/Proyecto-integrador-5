package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.CriterioRubrica;
import com.gestionpracticas.modelo.Rubrica;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CriterioRubricaDAO {
    
    private final Connection con;
    
    public CriterioRubricaDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    public boolean insertar(CriterioRubrica c) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO CRITERIO_RUBRICA (ID_CRITERIO, ID_RUBRICA, NOMBRE, DESCRIPCION, " +
                      "PUNTAJE_MAXIMO, PESO_PORCENTAJE, ORDEN, ESTADO) " +
                      "VALUES (SEQ_CRITERIO_RUBRICA.NEXTVAL, ?, ?, ?, ?, ?, ?, 'ACTIVO')";
            ps = con.prepareStatement(sql);
            ps.setInt(1, c.getRubrica().getIdRubrica());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDescripcion());
            ps.setDouble(4, c.getPuntajeMaximo());
            ps.setDouble(5, c.getPesoPorcentaje());
            ps.setInt(6, c.getOrden());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error CriterioRubricaDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<CriterioRubrica> listarPorRubrica(int idRubrica) {
        List<CriterioRubrica> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM CRITERIO_RUBRICA WHERE ID_RUBRICA = ? AND ESTADO = 'ACTIVO' ORDER BY ORDEN";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idRubrica);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                CriterioRubrica c = new CriterioRubrica();
                c.setIdCriterio(rs.getInt("ID_CRITERIO"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setDescripcion(rs.getString("DESCRIPCION"));
                c.setPuntajeMaximo(rs.getDouble("PUNTAJE_MAXIMO"));
                c.setPesoPorcentaje(rs.getDouble("PESO_PORCENTAJE"));
                c.setOrden(rs.getInt("ORDEN"));
                c.setEstado(rs.getString("ESTADO"));
                
                Rubrica r = new Rubrica();
                r.setIdRubrica(idRubrica);
                c.setRubrica(r);
                
                lista.add(c);
            }
        } catch (SQLException e) { 
            System.err.println("Error CriterioRubricaDAO.listarPorRubrica: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<CriterioRubrica> listarTodos() {
        List<CriterioRubrica> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT C.*, R.NOMBRE AS RUBRICA_NOMBRE FROM CRITERIO_RUBRICA C " +
                      "INNER JOIN RUBRICA R ON C.ID_RUBRICA = R.ID_RUBRICA " +
                      "WHERE C.ESTADO = 'ACTIVO' ORDER BY C.ORDEN";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                CriterioRubrica c = new CriterioRubrica();
                c.setIdCriterio(rs.getInt("ID_CRITERIO"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setDescripcion(rs.getString("DESCRIPCION"));
                c.setPuntajeMaximo(rs.getDouble("PUNTAJE_MAXIMO"));
                c.setPesoPorcentaje(rs.getDouble("PESO_PORCENTAJE"));
                c.setOrden(rs.getInt("ORDEN"));
                c.setEstado(rs.getString("ESTADO"));
                
                Rubrica r = new Rubrica();
                r.setIdRubrica(rs.getInt("ID_RUBRICA"));
                r.setNombre(rs.getString("RUBRICA_NOMBRE"));
                c.setRubrica(r);
                
                lista.add(c);
            }
        } catch (SQLException e) { 
            System.err.println("Error CriterioRubricaDAO.listarTodos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }

    public CriterioRubrica buscarPorId(int id) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM CRITERIO_RUBRICA WHERE ID_CRITERIO = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                CriterioRubrica c = new CriterioRubrica();
                c.setIdCriterio(rs.getInt("ID_CRITERIO"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setDescripcion(rs.getString("DESCRIPCION"));
                c.setPuntajeMaximo(rs.getDouble("PUNTAJE_MAXIMO"));
                c.setPesoPorcentaje(rs.getDouble("PESO_PORCENTAJE"));
                c.setOrden(rs.getInt("ORDEN"));
                c.setEstado(rs.getString("ESTADO"));
                
                Rubrica r = new Rubrica();
                r.setIdRubrica(rs.getInt("ID_RUBRICA"));
                c.setRubrica(r);
                
                return c;
            }
        } catch (SQLException e) { 
            System.err.println("Error CriterioRubricaDAO.buscarPorId: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }

    public boolean actualizar(CriterioRubrica c) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE CRITERIO_RUBRICA SET NOMBRE = ?, DESCRIPCION = ?, " +
                      "PUNTAJE_MAXIMO = ?, PESO_PORCENTAJE = ?, ORDEN = ? " +
                      "WHERE ID_CRITERIO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setDouble(3, c.getPuntajeMaximo());
            ps.setDouble(4, c.getPesoPorcentaje());
            ps.setInt(5, c.getOrden());
            ps.setInt(6, c.getIdCriterio());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error CriterioRubricaDAO.actualizar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE CRITERIO_RUBRICA SET ESTADO = 'INACTIVO' WHERE ID_CRITERIO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error CriterioRubricaDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM CRITERIO_RUBRICA WHERE ID_CRITERIO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error CriterioRubricaDAO.eliminarFisico: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
}