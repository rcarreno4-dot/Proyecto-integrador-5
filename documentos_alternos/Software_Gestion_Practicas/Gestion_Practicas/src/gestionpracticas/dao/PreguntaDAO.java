package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Evaluacion;
import com.gestionpracticas.modelo.Pregunta;
import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAO {
    
    private final Connection con;
    
    public PreguntaDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    public boolean insertar(Pregunta p) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO PREGUNTA (ID_PREGUNTA, TEXTO_PREGUNTA, TIPO_PREGUNTA, " +
                      "PUNTAJE_MAXIMO, ORDEN, ID_EVALUACION, ID_PRACTICA, ESTADO) " +
                      "VALUES (SEQ_PREGUNTA.NEXTVAL, ?, ?, ?, ?, ?, ?, 'ACTIVO')";
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getTextoPregunta());
            ps.setString(2, p.getTipoPregunta());
            ps.setDouble(3, p.getPuntajeMaximo());
            ps.setInt(4, p.getOrden());
            ps.setInt(5, p.getEvaluacion().getIdEvaluacion());
            ps.setInt(6, p.getPractica().getIdPractica());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean insertarVarias(List<Pregunta> preguntas) {
        if (preguntas == null || preguntas.isEmpty()) {
            return false;
        }
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO PREGUNTA (ID_PREGUNTA, TEXTO_PREGUNTA, TIPO_PREGUNTA, " +
                      "PUNTAJE_MAXIMO, ORDEN, ID_EVALUACION, ID_PRACTICA, ESTADO) " +
                      "VALUES (SEQ_PREGUNTA.NEXTVAL, ?, ?, ?, ?, ?, ?, 'ACTIVO')";
            ps = con.prepareStatement(sql);
            
            for (Pregunta p : preguntas) {
                ps.setString(1, p.getTextoPregunta());
                ps.setString(2, p.getTipoPregunta());
                ps.setDouble(3, p.getPuntajeMaximo());
                ps.setInt(4, p.getOrden());
                ps.setInt(5, p.getEvaluacion().getIdEvaluacion());
                ps.setInt(6, p.getPractica().getIdPractica());
                ps.addBatch();
            }
            
            int[] resultados = ps.executeBatch();
            return resultados.length > 0;
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.insertarVarias: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<Pregunta> listarPorEvaluacion(int idEvaluacion) {
        List<Pregunta> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT P.*, ('Evaluación ' || E.ID_EVALUACION) AS EVALUACION_TITULO, PR.TITULO AS PRACTICA_TITULO " +
                      "FROM PREGUNTA P " +
                      "LEFT JOIN EVALUACION E ON P.ID_EVALUACION = E.ID_EVALUACION " +
                      "LEFT JOIN PRACTICA PR ON P.ID_PRACTICA = PR.ID_PRACTICA " +
                      "WHERE P.ID_EVALUACION = ? AND P.ESTADO = 'ACTIVO' " +
                      "ORDER BY P.ORDEN";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEvaluacion);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.listarPorEvaluacion: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Pregunta> listarPorPractica(int idPractica) {
        List<Pregunta> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT P.*, ('Evaluación ' || E.ID_EVALUACION) AS EVALUACION_TITULO, PR.TITULO AS PRACTICA_TITULO " +
                      "FROM PREGUNTA P " +
                      "LEFT JOIN EVALUACION E ON P.ID_EVALUACION = E.ID_EVALUACION " +
                      "LEFT JOIN PRACTICA PR ON P.ID_PRACTICA = PR.ID_PRACTICA " +
                      "WHERE P.ID_PRACTICA = ? AND P.ESTADO = 'ACTIVO' " +
                      "ORDER BY P.ORDEN";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPractica);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.listarPorPractica: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Pregunta> listarTodas() {
        List<Pregunta> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT P.*, ('Evaluación ' || E.ID_EVALUACION) AS EVALUACION_TITULO, PR.TITULO AS PRACTICA_TITULO " +
                      "FROM PREGUNTA P " +
                      "LEFT JOIN EVALUACION E ON P.ID_EVALUACION = E.ID_EVALUACION " +
                      "LEFT JOIN PRACTICA PR ON P.ID_PRACTICA = PR.ID_PRACTICA " +
                      "WHERE P.ESTADO = 'ACTIVO' " +
                      "ORDER BY P.ID_EVALUACION, P.ORDEN";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.listarTodas: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }

    public Pregunta buscarPorId(int id) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT P.*, ('Evaluación ' || E.ID_EVALUACION) AS EVALUACION_TITULO, PR.TITULO AS PRACTICA_TITULO " +
                      "FROM PREGUNTA P " +
                      "LEFT JOIN EVALUACION E ON P.ID_EVALUACION = E.ID_EVALUACION " +
                      "LEFT JOIN PRACTICA PR ON P.ID_PRACTICA = PR.ID_PRACTICA " +
                      "WHERE P.ID_PREGUNTA = ? AND P.ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.buscarPorId: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }

    public boolean actualizar(Pregunta p) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE PREGUNTA SET TEXTO_PREGUNTA = ?, TIPO_PREGUNTA = ?, " +
                      "PUNTAJE_MAXIMO = ?, ORDEN = ?, ID_EVALUACION = ?, ID_PRACTICA = ? " +
                      "WHERE ID_PREGUNTA = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getTextoPregunta());
            ps.setString(2, p.getTipoPregunta());
            ps.setDouble(3, p.getPuntajeMaximo());
            ps.setInt(4, p.getOrden());
            ps.setInt(5, p.getEvaluacion().getIdEvaluacion());
            ps.setInt(6, p.getPractica().getIdPractica());
            ps.setInt(7, p.getIdPregunta());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.actualizar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE PREGUNTA SET ESTADO = 'INACTIVO' WHERE ID_PREGUNTA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean eliminarPorEvaluacion(int idEvaluacion) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE PREGUNTA SET ESTADO = 'INACTIVO' WHERE ID_EVALUACION = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEvaluacion);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.eliminarPorEvaluacion: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM PREGUNTA WHERE ID_PREGUNTA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.eliminarFisico: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public int contarPorEvaluacion(int idEvaluacion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) AS TOTAL FROM PREGUNTA WHERE ID_EVALUACION = ? AND ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEvaluacion);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("TOTAL");
            }
        } catch (SQLException e) { 
            System.err.println("Error PreguntaDAO.contarPorEvaluacion: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return 0;
    }

    private Pregunta mapear(ResultSet rs) throws SQLException {
        Pregunta p = new Pregunta();
        p.setIdPregunta(rs.getInt("ID_PREGUNTA"));
        p.setTextoPregunta(rs.getString("TEXTO_PREGUNTA"));
        p.setTipoPregunta(rs.getString("TIPO_PREGUNTA"));
        p.setPuntajeMaximo(rs.getDouble("PUNTAJE_MAXIMO"));
        p.setOrden(rs.getInt("ORDEN"));
        p.setEstado(rs.getString("ESTADO"));
        
        Evaluacion e = new Evaluacion();
        e.setIdEvaluacion(rs.getInt("ID_EVALUACION"));
        p.setEvaluacion(e);
        
        Practica pr = new Practica();
        pr.setIdPractica(rs.getInt("ID_PRACTICA"));
        p.setPractica(pr);
        
        return p;
    }
}