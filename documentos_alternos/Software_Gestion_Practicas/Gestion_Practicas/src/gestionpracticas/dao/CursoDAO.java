package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.Curso;
import com.gestionpracticas.modelo.Programa;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    
    private final Connection con;
    
    public CursoDAO() {
        this.con = ConexionBD.getConnection();
    }
    
    public boolean insertar(Curso c) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO CURSO (ID_CURSO, NOMBRE, CODIGO, CREDITOS, ID_PROGRAMA, DESCRIPCION, ESTADO) " +
                      "VALUES (SEQ_CURSO.NEXTVAL, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getCodigo());
            ps.setInt(3, c.getCreditos());
            
            // Validar que el programa no sea null
            if (c.getPrograma() == null) {
                throw new SQLException("El curso debe tener un programa asociado");
            }
            ps.setInt(4, c.getPrograma().getIdPrograma());
            
            ps.setString(5, c.getDescripcion());
            ps.setString(6, c.getEstado());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.err.println("Error CursoDAO.insertar: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }
    
    public List listarTodos() {

        List lista = new ArrayList();

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {

            String sql =
              "SELECT " +
              "C.ID_CURSO, " +
              "C.NOMBRE AS CURSO_NOMBRE, " +
              "C.CODIGO, " +
              "C.CREDITOS, " +
              "C.DESCRIPCION, " +
              "C.ESTADO, " +
              "C.ID_PROGRAMA, " +
              "P.NOMBRE AS PROGRAMA_NOMBRE " +
              "FROM CURSO C " +
              "LEFT JOIN PROGRAMA P " +
              "ON C.ID_PROGRAMA = P.ID_PROGRAMA " +
              "ORDER BY C.ID_CURSO";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                Curso c = new Curso();

                c.setIdCurso(
                    rs.getInt("ID_CURSO")
                );

                c.setNombre(
                    rs.getString("CURSO_NOMBRE")
                );

                c.setCodigo(
                    rs.getString("CODIGO")
                );

                c.setCreditos(
                    rs.getInt("CREDITOS")
                );

                c.setDescripcion(
                    rs.getString("DESCRIPCION")
                );

                c.setEstado(
                    rs.getString("ESTADO")
                );

                Programa p = new Programa();

                p.setIdPrograma(
                    rs.getInt("ID_PROGRAMA")
                );

                p.setNombre(
                    rs.getString("PROGRAMA_NOMBRE")
                );

                c.setPrograma(p);

                lista.add(c);
            }

        } catch (SQLException e) {

            System.err.println(
              "Error CursoDAO.listarTodos: "
                + e.getMessage()
            );

        } finally {

            Utilidades.cerrar(rs);

            Utilidades.cerrar(ps);
        }

        return lista;
    }
    
    public List listarActivos() {
        List lista = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT C.*, P.NOMBRE AS PROGRAMA_NOMBRE FROM CURSO C " +
                      "LEFT JOIN PROGRAMA P ON C.ID_PROGRAMA = P.ID_PROGRAMA " +
                      "WHERE C.ESTADO = 'ACTIVO' ORDER BY C.NOMBRE";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Curso c = new Curso();
                c.setIdCurso(rs.getInt("ID_CURSO"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setCodigo(rs.getString("CODIGO"));
                c.setCreditos(rs.getInt("CREDITOS"));
                c.setDescripcion(rs.getString("DESCRIPCION"));
                c.setEstado(rs.getString("ESTADO"));
                
                Programa p = new Programa();
                p.setIdPrograma(rs.getInt("ID_PROGRAMA"));
                p.setNombre(rs.getString("PROGRAMA_NOMBRE"));
                c.setPrograma(p);
                
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error CursoDAO.listarActivos: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }
    
    public Curso buscarPorId(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT C.*, P.NOMBRE AS PROGRAMA_NOMBRE FROM CURSO C " +
                      "LEFT JOIN PROGRAMA P ON C.ID_PROGRAMA = P.ID_PROGRAMA " +
                      "WHERE C.ID_CURSO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Curso c = new Curso();
                c.setIdCurso(rs.getInt("ID_CURSO"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setCodigo(rs.getString("CODIGO"));
                c.setCreditos(rs.getInt("CREDITOS"));
                c.setDescripcion(rs.getString("DESCRIPCION"));
                c.setEstado(rs.getString("ESTADO"));
                
                Programa p = new Programa();
                p.setIdPrograma(rs.getInt("ID_PROGRAMA"));
                p.setNombre(rs.getString("PROGRAMA_NOMBRE"));
                c.setPrograma(p);
                
                return c;
            }
        } catch (SQLException e) {
            System.err.println("Error CursoDAO.buscarPorId: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return null;
    }
    
    public List buscarPorPrograma(int idPrograma) {
        List lista = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT C.*, P.NOMBRE AS PROGRAMA_NOMBRE FROM CURSO C " +
                      "LEFT JOIN PROGRAMA P ON C.ID_PROGRAMA = P.ID_PROGRAMA " +
                      "WHERE C.ID_PROGRAMA = ? AND C.ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPrograma);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Curso c = new Curso();
                c.setIdCurso(rs.getInt("ID_CURSO"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setCodigo(rs.getString("CODIGO"));
                c.setCreditos(rs.getInt("CREDITOS"));
                c.setDescripcion(rs.getString("DESCRIPCION"));
                c.setEstado(rs.getString("ESTADO"));
                
                Programa p = new Programa();
                p.setIdPrograma(rs.getInt("ID_PROGRAMA"));
                p.setNombre(rs.getString("PROGRAMA_NOMBRE"));
                c.setPrograma(p);
                
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error CursoDAO.buscarPorPrograma: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return lista;
    }
    
    public boolean actualizar(Curso c) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE CURSO SET NOMBRE = ?, CODIGO = ?, CREDITOS = ?, " +
                      "ID_PROGRAMA = ?, DESCRIPCION = ?, ESTADO = ? WHERE ID_CURSO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getCodigo());
            ps.setInt(3, c.getCreditos());
            
            if (c.getPrograma() == null) {
                throw new SQLException("El curso debe tener un programa asociado");
            }
            ps.setInt(4, c.getPrograma().getIdPrograma());
            
            ps.setString(5, c.getDescripcion());
            ps.setString(6, c.getEstado());
            ps.setInt(7, c.getIdCurso());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.err.println("Error CursoDAO.actualizar: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }
    
    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE CURSO SET ESTADO = 'INACTIVO' WHERE ID_CURSO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.err.println("Error CursoDAO.eliminar: " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }
}