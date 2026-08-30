package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Evaluacion;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionDAO {
    
    private final Connection con;
    
    public EvaluacionDAO() {
        this.con = ConexionBD.getConnection();
    }
    
    public boolean insertar(Evaluacion e) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO EVALUACION (ID_EVALUACION, ID_MATRICULA_PRACTICA, ID_RUBRICA, " +
                      "ID_EVALUADOR, PUNTAJE_OBTENIDO, OBSERVACIONES, FECHA_EVALUACION, ESTADO) " +
                      "VALUES (SEQ_EVALUACION.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, e.getMatricula().getIdMatriculaPractica());
            ps.setInt(2, e.getRubrica().getIdRubrica());
            ps.setInt(3, e.getEvaluador().getIdUsuario());
            ps.setDouble(4, e.getPuntajeObtenido());
            ps.setString(5, e.getObservaciones());
            ps.setString(6, e.getEstado());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) { 
            System.err.println("Error insertar: " + ex.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public List<Evaluacion> listarPorEvaluador(int idEvaluador) {
        List<Evaluacion> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT E.*, M.ID_ESTUDIANTE, U.NOMBRE AS EST_NOMBRE, R.NOMBRE AS RUBRICA_NOMBRE " +
                      "FROM EVALUACION E " +
                      "JOIN MATRICULA_PRACTICA M ON E.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                      "JOIN RUBRICA R ON E.ID_RUBRICA = R.ID_RUBRICA " +
                      "WHERE E.ID_EVALUADOR = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEvaluador);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Evaluacion e = new Evaluacion();
                e.setIdEvaluacion(rs.getInt("ID_EVALUACION"));
                e.setPuntajeObtenido(rs.getDouble("PUNTAJE_OBTENIDO"));
                e.setObservaciones(rs.getString("OBSERVACIONES"));
                e.setEstado(rs.getString("ESTADO"));
                e.setFechaEvaluacion(rs.getDate("FECHA_EVALUACION"));
                lista.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error listarPorEvaluador: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }
    
    public List<Evaluacion> listarTodas() {
        List<Evaluacion> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM EVALUACION";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Evaluacion e = new Evaluacion();
                e.setIdEvaluacion(rs.getInt("ID_EVALUACION"));
                e.setPuntajeObtenido(rs.getDouble("PUNTAJE_OBTENIDO"));
                e.setObservaciones(rs.getString("OBSERVACIONES"));
                e.setEstado(rs.getString("ESTADO"));
                e.setFechaEvaluacion(rs.getDate("FECHA_EVALUACION"));
                lista.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error listarTodas: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }
    
    public boolean actualizar(Evaluacion e) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE EVALUACION SET PUNTAJE_OBTENIDO = ?, OBSERVACIONES = ?, ESTADO = ? WHERE ID_EVALUACION = ?";
            ps = con.prepareStatement(sql);
            ps.setDouble(1, e.getPuntajeObtenido());
            ps.setString(2, e.getObservaciones());
            ps.setString(3, e.getEstado());
            ps.setInt(4, e.getIdEvaluacion());
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            System.err.println("Error actualizar: " + ex.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }
}