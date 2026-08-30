package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.MatriculaPractica;
import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.modelo.Grupo;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatriculaPracticaDAO {
    
    private final Connection con;
    
    public MatriculaPracticaDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    public boolean insertar(MatriculaPractica m) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO MATRICULA_PRACTICA (ID_MATRICULA_PRACTICA, ID_ESTUDIANTE, " +
                      "ID_PRACTICA, ID_GRUPO, FECHA_MATRICULA, ESTADO) " +
                      "VALUES (SEQ_MATRICULA_PRACTICA.NEXTVAL, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, m.getEstudiante().getIdUsuario());
            ps.setInt(2, m.getPractica().getIdPractica());
            ps.setInt(3, m.getGrupo().getIdGrupo());
            ps.setDate(4, new java.sql.Date(m.getFechaMatricula().getTime()));
            ps.setString(5, m.getEstado());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<MatriculaPractica> listarPorEstudiante(int idEstudiante) {
        List<MatriculaPractica> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT M.*, P.TITULO, P.DESCRIPCION, U.NOMBRE, U.APELLIDO, U.EMAIL, " +
                      "G.NOMBRE AS NOMBRE_GRUPO " +
                      "FROM MATRICULA_PRACTICA M " +
                      "INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                      "INNER JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                      "INNER JOIN GRUPO G ON M.ID_GRUPO = G.ID_GRUPO " +
                      "WHERE M.ID_ESTUDIANTE = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstudiante);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.listarPorEstudiante: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<MatriculaPractica> listarPorPractica(int idPractica) {
        List<MatriculaPractica> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT M.*, P.TITULO, P.DESCRIPCION, U.NOMBRE, U.APELLIDO, U.EMAIL, " +
                      "G.NOMBRE AS NOMBRE_GRUPO " +
                      "FROM MATRICULA_PRACTICA M " +
                      "INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                      "INNER JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                      "INNER JOIN GRUPO G ON M.ID_GRUPO = G.ID_GRUPO " +
                      "WHERE M.ID_PRACTICA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPractica);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.listarPorPractica: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<MatriculaPractica> listarPorGrupo(int idGrupo) {
        List<MatriculaPractica> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT M.*, P.TITULO, P.DESCRIPCION, U.NOMBRE, U.APELLIDO, U.EMAIL, " +
                      "G.NOMBRE AS NOMBRE_GRUPO " +
                      "FROM MATRICULA_PRACTICA M " +
                      "INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                      "INNER JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                      "INNER JOIN GRUPO G ON M.ID_GRUPO = G.ID_GRUPO " +
                      "WHERE M.ID_GRUPO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idGrupo);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.listarPorGrupo: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<MatriculaPractica> listarTodas() {
        List<MatriculaPractica> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT M.*, P.TITULO, P.DESCRIPCION, U.NOMBRE, U.APELLIDO, U.EMAIL, " +
                      "G.NOMBRE AS NOMBRE_GRUPO " +
                      "FROM MATRICULA_PRACTICA M " +
                      "INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                      "INNER JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                      "INNER JOIN GRUPO G ON M.ID_GRUPO = G.ID_GRUPO " +
                      "ORDER BY M.FECHA_MATRICULA DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.listarTodas: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public MatriculaPractica buscarPorId(int id) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT M.*, P.TITULO, P.DESCRIPCION, U.NOMBRE, U.APELLIDO, U.EMAIL, " +
                      "G.NOMBRE AS NOMBRE_GRUPO " +
                      "FROM MATRICULA_PRACTICA M " +
                      "INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                      "INNER JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                      "INNER JOIN GRUPO G ON M.ID_GRUPO = G.ID_GRUPO " +
                      "WHERE M.ID_MATRICULA_PRACTICA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.buscarPorId: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }
    
    public List<MatriculaPractica> buscarPorEstudianteYPractica(int idEstudiante, int idPractica) {
        List<MatriculaPractica> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT M.*, P.TITULO, P.DESCRIPCION, U.NOMBRE, U.APELLIDO, U.EMAIL, " +
                      "G.NOMBRE AS NOMBRE_GRUPO " +
                      "FROM MATRICULA_PRACTICA M " +
                      "INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                      "INNER JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                      "INNER JOIN GRUPO G ON M.ID_GRUPO = G.ID_GRUPO " +
                      "WHERE M.ID_ESTUDIANTE = ? AND M.ID_PRACTICA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idPractica);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.buscarPorEstudianteYPractica: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }

    public boolean actualizarEstado(int idMatricula, String estado) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE MATRICULA_PRACTICA SET ESTADO = ? WHERE ID_MATRICULA_PRACTICA = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idMatricula);
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.actualizarEstado: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public List<MatriculaPractica> listarPorEstado(String estado) {
        List<MatriculaPractica> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT M.*, P.TITULO, P.DESCRIPCION, U.NOMBRE, U.APELLIDO, U.EMAIL, " +
                      "G.NOMBRE AS NOMBRE_GRUPO " +
                      "FROM MATRICULA_PRACTICA M " +
                      "INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                      "INNER JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                      "INNER JOIN GRUPO G ON M.ID_GRUPO = G.ID_GRUPO " +
                      "WHERE M.ESTADO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, estado);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.listarPorEstado: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public int contarMatriculadosPorPractica(int idPractica) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) AS TOTAL FROM MATRICULA_PRACTICA WHERE ID_PRACTICA = ? AND ESTADO IN ('ACTIVO', 'APROBADO')";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPractica);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("TOTAL");
            }
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.contarMatriculados: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return 0;
    }
    
    public boolean eliminar(int idMatricula) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM MATRICULA_PRACTICA WHERE ID_MATRICULA_PRACTICA = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMatricula);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error MatriculaPracticaDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    private MatriculaPractica mapear(ResultSet rs) throws SQLException {
        MatriculaPractica m = new MatriculaPractica();
        m.setIdMatriculaPractica(rs.getInt("ID_MATRICULA_PRACTICA"));
        m.setFechaMatricula(rs.getDate("FECHA_MATRICULA"));
        m.setEstado(rs.getString("ESTADO"));
        
        Usuario estudiante = new Usuario();
        estudiante.setIdUsuario(rs.getInt("ID_ESTUDIANTE"));
        estudiante.setNombre(rs.getString("NOMBRE"));
        estudiante.setApellido(rs.getString("APELLIDO"));
        estudiante.setEmail(rs.getString("EMAIL"));
        m.setEstudiante(estudiante);
        
        Practica practica = new Practica();
        practica.setIdPractica(rs.getInt("ID_PRACTICA"));
        practica.setTitulo(rs.getString("TITULO"));
        practica.setDescripcion(rs.getString("DESCRIPCION"));
        m.setPractica(practica);
        
        Grupo grupo = new Grupo();
        grupo.setIdGrupo(rs.getInt("ID_GRUPO"));
        grupo.setNombre(rs.getString("NOMBRE_GRUPO"));
        m.setGrupo(grupo);
        
        return m;
    }
}