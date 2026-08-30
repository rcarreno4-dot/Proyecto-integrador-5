package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.AsignacionDocente;
import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDocenteDAO {
    
    private final Connection con;
    
    public AsignacionDocenteDAO() { 
        this.con = ConexionBD.getConnection(); 
    }
    

    public boolean insertar(AsignacionDocente a) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO ASIGNACION_DOCENTE (ID_ASIGNACION_DOCENTE, FECHA_ASIGNACION, ROL_EN_PRACTICA, ID_PRACTICA, ID_USUARIO, ESTADO) " +
                      "VALUES (SEQ_ASIGNACION_DOCENTE.NEXTVAL, SYSDATE, ?, ?, ?, 'ACTIVO')";
            ps = con.prepareStatement(sql);
            ps.setString(1, a.getRolEnPractica());
            ps.setInt(2, a.getPractica().getIdPractica());
            ps.setInt(3, a.getUsuarios().get(0).getIdUsuario());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error AsignacionDocenteDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<AsignacionDocente> listarPorPractica(int idPractica) {
        List<AsignacionDocente> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT A.*, U.NOMBRE, U.APELLIDO, U.EMAIL, P.TITULO, P.DESCRIPCION " +
                      "FROM ASIGNACION_DOCENTE A " +
                      "JOIN USUARIO U ON A.ID_USUARIO = U.ID_USUARIO " +
                      "JOIN PRACTICA P ON A.ID_PRACTICA = P.ID_PRACTICA " +
                      "WHERE A.ID_PRACTICA = ? AND A.ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPractica);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                AsignacionDocente a = new AsignacionDocente();
                a.setIdAsignacionDocente(rs.getInt("ID_ASIGNACION_DOCENTE"));
                a.setRolEnPractica(rs.getString("ROL_EN_PRACTICA"));
                a.setFechaAsignacion(rs.getDate("FECHA_ASIGNACION"));
                a.setEstado(rs.getString("ESTADO"));
                
                // Práctica
                Practica p = new Practica();
                p.setIdPractica(rs.getInt("ID_PRACTICA"));
                p.setTitulo(rs.getString("TITULO"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                a.setPractica(p);
                
                // Usuario
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_USUARIO"));
                u.setNombre(rs.getString("NOMBRE"));
                u.setApellido(rs.getString("APELLIDO"));
                u.setEmail(rs.getString("EMAIL"));
                
                List<Usuario> usuarios = new ArrayList<>();
                usuarios.add(u);
                a.setUsuarios(usuarios);
                
                lista.add(a);
            }
        } catch (SQLException e) { 
            System.err.println("Error AsignacionDocenteDAO.listarPorPractica: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }

    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            // Eliminación lógica
            String sql = "UPDATE ASIGNACION_DOCENTE SET ESTADO = 'INACTIVO' WHERE ID_ASIGNACION_DOCENTE = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error AsignacionDocenteDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public List<AsignacionDocente> listarTodos() {
        List<AsignacionDocente> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT A.*, U.NOMBRE, U.APELLIDO, U.EMAIL, P.TITULO " +
                      "FROM ASIGNACION_DOCENTE A " +
                      "JOIN USUARIO U ON A.ID_USUARIO = U.ID_USUARIO " +
                      "JOIN PRACTICA P ON A.ID_PRACTICA = P.ID_PRACTICA " +
                      "WHERE A.ESTADO = 'ACTIVO'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                AsignacionDocente a = new AsignacionDocente();
                a.setIdAsignacionDocente(rs.getInt("ID_ASIGNACION_DOCENTE"));
                a.setRolEnPractica(rs.getString("ROL_EN_PRACTICA"));
                a.setFechaAsignacion(rs.getDate("FECHA_ASIGNACION"));
                
                Practica p = new Practica();
                p.setIdPractica(rs.getInt("ID_PRACTICA"));
                p.setTitulo(rs.getString("TITULO"));
                a.setPractica(p);
                
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_USUARIO"));
                u.setNombre(rs.getString("NOMBRE"));
                u.setApellido(rs.getString("APELLIDO"));
                List<Usuario> usuarios = new ArrayList<>();
                usuarios.add(u);
                a.setUsuarios(usuarios);
                
                lista.add(a);
            }
        } catch (SQLException e) { 
            System.err.println("Error AsignacionDocenteDAO.listarTodos: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
}