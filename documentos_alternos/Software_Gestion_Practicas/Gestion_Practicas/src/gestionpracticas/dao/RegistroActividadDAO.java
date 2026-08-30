package com.gestionpracticas.dao;

import com.gestionpracticas.modelo.MatriculaPractica;
import com.gestionpracticas.modelo.RegistroActividad;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroActividadDAO {
    
    private final Connection con;
    
    public RegistroActividadDAO() { 
        this.con = ConexionBD.getConnection(); 
    }

    public boolean insertar(RegistroActividad r) {
        String sql = "INSERT INTO REGISTRO_ACTIVIDAD (ID_REGISTRO, ID_MATRICULA_PRACTICA, FECHA_ACTIVIDAD, " +
                   "DESCRIPCION, HORAS, TIPO_ACTIVIDAD, EVIDENCIA, ESTADO, OBSERVACIONES) " +
                   "VALUES (SEQ_REGISTRO_ACTIVIDAD.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, r.getMatricula().getIdMatriculaPractica());
            ps.setDate(2, r.getFechaActividad() != null ? new java.sql.Date(r.getFechaActividad().getTime()) : new java.sql.Date(System.currentTimeMillis()));
            ps.setString(3, r.getDescripcion());
            ps.setDouble(4, r.getHoras());
            ps.setString(5, r.getTipoActividad());
            ps.setString(6, r.getEvidencia());
            ps.setString(7, r.getEstado());
            ps.setString(8, r.getObservaciones());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.insertar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean insertarVarios(List<RegistroActividad> registros) {
        if (registros == null || registros.isEmpty()) {
            return false;
        }
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO REGISTRO_ACTIVIDAD (ID_REGISTRO, ID_MATRICULA_PRACTICA, FECHA_ACTIVIDAD, " +
                      "DESCRIPCION, HORAS, TIPO_ACTIVIDAD, EVIDENCIA, ESTADO, OBSERVACIONES) " +
                      "VALUES (SEQ_REGISTRO_ACTIVIDAD.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            
            for (RegistroActividad r : registros) {
                ps.setInt(1, r.getMatricula().getIdMatriculaPractica());
                ps.setDate(2, r.getFechaActividad() != null ? new java.sql.Date(r.getFechaActividad().getTime()) : new java.sql.Date(System.currentTimeMillis()));
                ps.setString(3, r.getDescripcion());
                ps.setDouble(4, r.getHoras());
                ps.setString(5, r.getTipoActividad());
                ps.setString(6, r.getEvidencia());
                ps.setString(7, r.getEstado());
                ps.setString(8, r.getObservaciones());
                ps.addBatch();
            }
            
            int[] resultados = ps.executeBatch();
            return resultados.length > 0;
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.insertarVarios: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public List<RegistroActividad> listarPorMatricula(int idMatriculaPractica) {
        List<RegistroActividad> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT R.*, M.ID_ESTUDIANTE " +
                      "FROM REGISTRO_ACTIVIDAD R " +
                      "JOIN MATRICULA_PRACTICA M ON R.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "WHERE R.ID_MATRICULA_PRACTICA = ? " +
                      "ORDER BY R.FECHA_ACTIVIDAD DESC";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.listarPorMatricula: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<RegistroActividad> listarPorEstudiante(int idEstudiante) {
        List<RegistroActividad> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT R.*, M.ID_ESTUDIANTE " +
                      "FROM REGISTRO_ACTIVIDAD R " +
                      "JOIN MATRICULA_PRACTICA M ON R.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "WHERE M.ID_ESTUDIANTE = ? " +
                      "ORDER BY R.FECHA_ACTIVIDAD DESC";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstudiante);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.listarPorEstudiante: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<RegistroActividad> listarPorPractica(int idPractica) {
        List<RegistroActividad> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT R.*, M.ID_ESTUDIANTE, M.ID_PRACTICA " +
                      "FROM REGISTRO_ACTIVIDAD R " +
                      "JOIN MATRICULA_PRACTICA M ON R.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "WHERE M.ID_PRACTICA = ? " +
                      "ORDER BY R.FECHA_ACTIVIDAD DESC";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPractica);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.listarPorPractica: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<RegistroActividad> listarPorEstado(String estado) {
        List<RegistroActividad> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT R.*, M.ID_ESTUDIANTE " +
                      "FROM REGISTRO_ACTIVIDAD R " +
                      "JOIN MATRICULA_PRACTICA M ON R.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "WHERE R.ESTADO = ? " +
                      "ORDER BY R.FECHA_ACTIVIDAD DESC";
            ps = con.prepareStatement(sql);
            ps.setString(1, estado);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.listarPorEstado: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<RegistroActividad> listarPorRangoFechas(Date inicio, Date fin) {
        List<RegistroActividad> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT R.*, M.ID_ESTUDIANTE " +
                      "FROM REGISTRO_ACTIVIDAD R " +
                      "JOIN MATRICULA_PRACTICA M ON R.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "WHERE R.FECHA_ACTIVIDAD BETWEEN ? AND ? " +
                      "ORDER BY R.FECHA_ACTIVIDAD DESC";
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(inicio.getTime()));
            ps.setDate(2, new java.sql.Date(fin.getTime()));
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.listarPorRangoFechas: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }
    
    public List<RegistroActividad> listarTodas() {
        List<RegistroActividad> lista = new ArrayList<>();
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT R.*, M.ID_ESTUDIANTE " +
                      "FROM REGISTRO_ACTIVIDAD R " +
                      "JOIN MATRICULA_PRACTICA M ON R.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "ORDER BY R.FECHA_ACTIVIDAD DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.listarTodas: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return lista;
    }

    public RegistroActividad buscarPorId(int id) {
        PreparedStatement ps = null; 
        ResultSet rs = null;
        try {
            String sql = "SELECT R.*, M.ID_ESTUDIANTE " +
                      "FROM REGISTRO_ACTIVIDAD R " +
                      "JOIN MATRICULA_PRACTICA M ON R.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "WHERE R.ID_REGISTRO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.buscarPorId: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return null;
    }

    public boolean actualizar(RegistroActividad r) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE REGISTRO_ACTIVIDAD SET DESCRIPCION = ?, HORAS = ?, " +
                      "TIPO_ACTIVIDAD = ?, EVIDENCIA = ?, OBSERVACIONES = ? " +
                      "WHERE ID_REGISTRO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, r.getDescripcion());
            ps.setDouble(2, r.getHoras());
            ps.setString(3, r.getTipoActividad());
            ps.setString(4, r.getEvidencia());
            ps.setString(5, r.getObservaciones());
            ps.setInt(6, r.getIdRegistro());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.actualizar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public boolean actualizarEstado(int idRegistro, String nuevoEstado) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE REGISTRO_ACTIVIDAD SET ESTADO = ? WHERE ID_REGISTRO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idRegistro);
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.actualizarEstado: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean aprobar(int idRegistro, String observaciones) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE REGISTRO_ACTIVIDAD SET ESTADO = 'APROBADO', OBSERVACIONES = ? WHERE ID_REGISTRO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, observaciones);
            ps.setInt(2, idRegistro);
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.aprobar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public boolean rechazar(int idRegistro, String motivo) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE REGISTRO_ACTIVIDAD SET ESTADO = 'RECHAZADO', OBSERVACIONES = ? WHERE ID_REGISTRO = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, motivo);
            ps.setInt(2, idRegistro);
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.rechazar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        try {
            // Eliminación lógica
            String sql = "UPDATE REGISTRO_ACTIVIDAD SET ESTADO = 'ELIMINADO' WHERE ID_REGISTRO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.eliminar: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }
    
    public double sumHorasPorMatricula(int idMatriculaPractica) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT SUM(HORAS) AS TOTAL_HORAS FROM REGISTRO_ACTIVIDAD " +
                      "WHERE ID_MATRICULA_PRACTICA = ? AND ESTADO = 'APROBADO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("TOTAL_HORAS");
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.sumHorasPorMatricula: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return 0;
    }
    
    public double sumHorasPorEstudiante(int idEstudiante) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT SUM(R.HORAS) AS TOTAL_HORAS " +
                      "FROM REGISTRO_ACTIVIDAD R " +
                      "JOIN MATRICULA_PRACTICA M ON R.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                      "WHERE M.ID_ESTUDIANTE = ? AND R.ESTADO = 'APROBADO'";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstudiante);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("TOTAL_HORAS");
            }
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.sumHorasPorEstudiante: " + e.getMessage()); 
        } finally { 
            Utilidades.cerrar(rs); 
            Utilidades.cerrar(ps); 
        }
        return 0;
    }
    
    public boolean eliminarFisico(int id) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM REGISTRO_ACTIVIDAD WHERE ID_REGISTRO = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) { 
            System.err.println("Error RegistroActividadDAO.eliminarFisico: " + e.getMessage()); 
            return false; 
        } finally { 
            Utilidades.cerrar(ps); 
        }
    }

    private RegistroActividad mapear(ResultSet rs) throws SQLException {
        RegistroActividad r = new RegistroActividad();
        r.setIdRegistro(rs.getInt("ID_REGISTRO"));
        r.setFechaActividad(rs.getDate("FECHA_ACTIVIDAD"));
        r.setDescripcion(rs.getString("DESCRIPCION"));
        r.setHoras(rs.getDouble("HORAS"));
        r.setTipoActividad(rs.getString("TIPO_ACTIVIDAD"));
        r.setEvidencia(rs.getString("EVIDENCIA"));
        r.setEstado(rs.getString("ESTADO"));
        r.setObservaciones(rs.getString("OBSERVACIONES"));
        
        MatriculaPractica m = new MatriculaPractica();
        m.setIdMatriculaPractica(rs.getInt("ID_MATRICULA_PRACTICA"));
        r.setMatricula(m);
        
        return r;
    }
}