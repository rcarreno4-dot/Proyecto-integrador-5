package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Curso;
import com.gestionpracticas.modelo.Grupo;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.modelo.Evaluacion;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO {
    
    private final Connection con;
    
    public GrupoDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    public boolean insertar(Grupo g) {
        String sql = "INSERT INTO GRUPO (ID_GRUPO, NOMBRE, ID_CURSO, ID_DOCENTE, SEMESTRE, ANIO, ESTADO) " +
                   "VALUES (SEQ_GRUPO.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, g.getNombre());
            ps.setInt(2, g.getCurso().getIdCurso());
            ps.setInt(3, g.getDocente().getIdUsuario());
            ps.setString(4, g.getSemestre());
            ps.setInt(5, g.getAnio());
            ps.setString(6, g.getEstado());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<Grupo> listarTodos() {
        List<Grupo> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        
        try {
            String sql = "SELECT G.*, C.NOMBRE AS NOM_CURSO, C.CODIGO, " +
                      "U.NOMBRE AS NOM_DOC, U.APELLIDO AS APE_DOC, U.EMAIL " +
                      "FROM GRUPO G " +
                      "INNER JOIN CURSO C ON G.ID_CURSO = C.ID_CURSO " +
                      "INNER JOIN USUARIO U ON G.ID_DOCENTE = U.ID_USUARIO " +
                      "WHERE G.ESTADO = 'ACTIVO' " +
                      "ORDER BY G.ID_GRUPO";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) { 
                lista.add(mapear(rs)); 
            }
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.listarTodos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Grupo> listarPorCurso(int idCurso) {
        List<Grupo> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        
        try {
            String sql = "SELECT G.*, C.NOMBRE AS NOM_CURSO, C.CODIGO, " +
                      "U.NOMBRE AS NOM_DOC, U.APELLIDO AS APE_DOC, U.EMAIL " +
                      "FROM GRUPO G " +
                      "INNER JOIN CURSO C ON G.ID_CURSO = C.ID_CURSO " +
                      "INNER JOIN USUARIO U ON G.ID_DOCENTE = U.ID_USUARIO " +
                      "WHERE G.ID_CURSO = ? AND G.ESTADO = 'ACTIVO' " +
                      "ORDER BY G.NOMBRE";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCurso);
            rs = ps.executeQuery();
            
            while (rs.next()) { 
                lista.add(mapear(rs)); 
            }
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.listarPorCurso: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Grupo> listarPorDocente(int idDocente) {
        List<Grupo> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        
        try {
            String sql = "SELECT G.*, C.NOMBRE AS NOM_CURSO, C.CODIGO, " +
                      "U.NOMBRE AS NOM_DOC, U.APELLIDO AS APE_DOC, U.EMAIL " +
                      "FROM GRUPO G " +
                      "INNER JOIN CURSO C ON G.ID_CURSO = C.ID_CURSO " +
                      "INNER JOIN USUARIO U ON G.ID_DOCENTE = U.ID_USUARIO " +
                      "WHERE G.ID_DOCENTE = ? AND G.ESTADO = 'ACTIVO' " +
                      "ORDER BY G.NOMBRE";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDocente);
            rs = ps.executeQuery();
            
            while (rs.next()) { 
                lista.add(mapear(rs)); 
            }
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.listarPorDocente: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<Grupo> listarActivos() {
        List<Grupo> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        
        try {
            String sql = "SELECT G.*, C.NOMBRE AS NOM_CURSO, C.CODIGO, " +
                      "U.NOMBRE AS NOM_DOC, U.APELLIDO AS APE_DOC, U.EMAIL " +
                      "FROM GRUPO G " +
                      "INNER JOIN CURSO C ON G.ID_CURSO = C.ID_CURSO " +
                      "INNER JOIN USUARIO U ON G.ID_DOCENTE = U.ID_USUARIO " +
                      "WHERE G.ESTADO = 'ACTIVO' " +
                      "ORDER BY G.NOMBRE";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) { 
                lista.add(mapear(rs)); 
            }
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.listarActivos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }

    public Grupo buscarPorId(int id) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        
        try {
            String sql = "SELECT G.*, C.NOMBRE AS NOM_CURSO, C.CODIGO, " +
                      "U.NOMBRE AS NOM_DOC, U.APELLIDO AS APE_DOC, U.EMAIL " +
                      "FROM GRUPO G " +
                      "INNER JOIN CURSO C ON G.ID_CURSO = C.ID_CURSO " +
                      "INNER JOIN USUARIO U ON G.ID_DOCENTE = U.ID_USUARIO " +
                      "WHERE G.ID_GRUPO = ? AND G.ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.buscarPorId: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }

    public boolean actualizar(Grupo g) {
        PreparedStatement ps = null;
        
        try {
            String sql = "UPDATE GRUPO SET NOMBRE = ?, ID_CURSO = ?, ID_DOCENTE = ?, " +
                      "SEMESTRE = ?, ANIO = ?, ESTADO = ? WHERE ID_GRUPO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, g.getNombre());
            ps.setInt(2, g.getCurso().getIdCurso());
            ps.setInt(3, g.getDocente().getIdUsuario());
            ps.setString(4, g.getSemestre());
            ps.setInt(5, g.getAnio());
            ps.setString(6, g.getEstado());
            ps.setInt(7, g.getIdGrupo());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.actualizar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        
        try {
            String sql = "UPDATE GRUPO SET ESTADO = 'INACTIVO' WHERE ID_GRUPO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;
        
        try {
            String sql = "DELETE FROM GRUPO WHERE ID_GRUPO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) { 
            System.err.println("Error GrupoDAO.eliminarFisico: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    private Grupo mapear(ResultSet rs) throws SQLException {
        Grupo g = new Grupo();
        g.setIdGrupo(rs.getInt("ID_GRUPO"));
        g.setNombre(rs.getString("NOMBRE"));
        g.setSemestre(rs.getString("SEMESTRE"));
        g.setAnio(rs.getInt("ANIO"));
        g.setEstado(rs.getString("ESTADO"));
        
        Curso c = new Curso();
        c.setIdCurso(rs.getInt("ID_CURSO"));
        c.setNombre(rs.getString("NOM_CURSO"));
        c.setCodigo(rs.getString("CODIGO"));
        g.setCurso(c);
        
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("ID_DOCENTE"));
        u.setNombre(rs.getString("NOM_DOC"));
        u.setApellido(rs.getString("APE_DOC"));
        u.setEmail(rs.getString("EMAIL"));
        g.setDocente(u);
        
        return g;
    }
}