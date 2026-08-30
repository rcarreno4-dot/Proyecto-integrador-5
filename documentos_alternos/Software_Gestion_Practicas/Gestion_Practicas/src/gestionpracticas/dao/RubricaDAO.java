package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Rubrica;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RubricaDAO {
    
    private final Connection con;
    
    public RubricaDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    public boolean insertar(Rubrica r) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO RUBRICA (ID_RUBRICA, NOMBRE, DESCRIPCION, ID_DOCENTE, ID_PRACTICA, PUNTAJE_TOTAL, FECHA_CREACION, ESTADO) " +
                      "VALUES (SEQ_RUBRICA.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE, 'ACTIVO')";
            ps = con.prepareStatement(sql);
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getDescripcion());
            ps.setInt(3, r.getDocente().getIdUsuario());
            ps.setInt(4, r.getPractica().getIdPractica());
            ps.setDouble(5, r.getPuntajeTotal());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error RubricaDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<Rubrica> listarTodos() {
        List<Rubrica> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM RUBRICA WHERE ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Rubrica r = new Rubrica();
                r.setIdRubrica(rs.getInt("ID_RUBRICA"));
                r.setNombre(rs.getString("NOMBRE"));
                r.setDescripcion(rs.getString("DESCRIPCION"));
                r.setPuntajeTotal(rs.getDouble("PUNTAJE_TOTAL"));
                r.setEstado(rs.getString("ESTADO"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error listarTodos: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }
    
    public List<Rubrica> listarTodas() {
        return listarTodos();
    }
    
    public List<Rubrica> listarPorDocente(int idDocente) {
        List<Rubrica> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM RUBRICA WHERE ID_DOCENTE = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDocente);
            rs = ps.executeQuery();
            while (rs.next()) {
                Rubrica r = new Rubrica();
                r.setIdRubrica(rs.getInt("ID_RUBRICA"));
                r.setNombre(rs.getString("NOMBRE"));
                r.setDescripcion(rs.getString("DESCRIPCION"));
                r.setPuntajeTotal(rs.getDouble("PUNTAJE_TOTAL"));
                r.setEstado(rs.getString("ESTADO"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error listarPorDocente: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }
    
    public List<Rubrica> listarPorPractica(int idPractica) {
        List<Rubrica> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM RUBRICA WHERE ID_PRACTICA = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPractica);
            rs = ps.executeQuery();
            while (rs.next()) {
                Rubrica r = new Rubrica();
                r.setIdRubrica(rs.getInt("ID_RUBRICA"));
                r.setNombre(rs.getString("NOMBRE"));
                r.setDescripcion(rs.getString("DESCRIPCION"));
                r.setPuntajeTotal(rs.getDouble("PUNTAJE_TOTAL"));
                r.setEstado(rs.getString("ESTADO"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error listarPorPractica: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }
    
    public List<Rubrica> listarActivas() {
        return listarTodos();
    }
    
    public Rubrica buscarPorId(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM RUBRICA WHERE ID_RUBRICA = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Rubrica r = new Rubrica();
                r.setIdRubrica(rs.getInt("ID_RUBRICA"));
                r.setNombre(rs.getString("NOMBRE"));
                r.setDescripcion(rs.getString("DESCRIPCION"));
                r.setPuntajeTotal(rs.getDouble("PUNTAJE_TOTAL"));
                r.setEstado(rs.getString("ESTADO"));
                return r;
            }
        } catch (SQLException e) {
            System.err.println("Error buscarPorId: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return null;
    }
    
    public Rubrica buscarPorPractica(int idPractica) {
        List<Rubrica> lista = listarPorPractica(idPractica);
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }
    
    public boolean actualizar(Rubrica r) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE RUBRICA SET NOMBRE = ?, DESCRIPCION = ?, PUNTAJE_TOTAL = ? WHERE ID_RUBRICA = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getDescripcion());
            ps.setDouble(3, r.getPuntajeTotal());
            ps.setInt(4, r.getIdRubrica());
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizar: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }
    
    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE RUBRICA SET ESTADO = 'INACTIVO' WHERE ID_RUBRICA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminar: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }
    
    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM RUBRICA WHERE ID_RUBRICA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminarFisico: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }
    
    public int contarPorDocente(int idDocente) {
        List<Rubrica> lista = listarPorDocente(idDocente);
        return lista.size();
    }
    
    public int contarPorPractica(int idPractica) {
        List<Rubrica> lista = listarPorPractica(idPractica);
        return lista.size();
    }
}