package com.gestionpracticas.servicio;

import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

/**
 * Reglas de negocio para controlar el paso de etapa electiva a etapa productiva.
 * La regla base exige horas completas, evaluación aprobada y documentos obligatorios.
 */
public class EtapaProductivaService {

    private final AuditoriaService auditoriaService = new AuditoriaService();

    public boolean cumpleRequisitosProductiva(int idMatriculaPractica) {
        return horasCumplidas(idMatriculaPractica)
                && evaluacionAprobada(idMatriculaPractica)
                && documentosAprobados(idMatriculaPractica);
    }

    public boolean pasarAProductiva(int idMatriculaPractica, int idUsuarioResponsable, String observacion) throws Exception {
        if (!cumpleRequisitosProductiva(idMatriculaPractica)) {
            throw new IllegalStateException("El estudiante aún no cumple horas, evaluación y documentos requeridos.");
        }
        Connection con = ConexionBD.getConnection();
        boolean auto = con.getAutoCommit();
        PreparedStatement ps = null;
        try {
            con.setAutoCommit(false);
            String anterior = etapaActual(idMatriculaPractica);
            LinkedHashMap<String, Object> datos = new LinkedHashMap<>();
            if (DBHelper.tieneColumna("MATRICULA_PRACTICA", "ETAPA_ACADEMICA")) {
                datos.put("ETAPA_ACADEMICA", "PRODUCTIVA");
            }
            if (DBHelper.tieneColumna("MATRICULA_PRACTICA", "ESTADO")) {
                datos.put("ESTADO", "PRODUCTIVA");
            }
            DBHelper.actualizar("MATRICULA_PRACTICA", "ID_MATRICULA_PRACTICA", Integer.valueOf(idMatriculaPractica), datos);
            registrarHistorial(idMatriculaPractica, idUsuarioResponsable, anterior, "PRODUCTIVA", observacion);
            auditoriaService.registrar(Integer.valueOf(idUsuarioResponsable), "MATRICULA_PRACTICA", String.valueOf(idMatriculaPractica),
                    "UPDATE", "ETAPA=" + anterior, "ETAPA=PRODUCTIVA", "EtapaProductivaService");
            con.commit();
            return true;
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(auto);
            Utilidades.cerrar(ps);
        }
    }

    public double totalHorasAprobadas(int idMatriculaPractica) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT NVL(SUM(HORAS),0) FROM REGISTRO_ACTIVIDAD " +
                    "WHERE ID_MATRICULA_PRACTICA = ? AND NVL(UPPER(ESTADO),'APROBADO') IN ('APROBADO','CONFIRMADO','VALIDADO')";
            ps = ConexionBD.getConnection().prepareStatement(sql);
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (Exception e) {
            return 0.0;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    public double horasRequeridas(int idMatriculaPractica) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT NVL(MAX(NVL(P.HORAS_REGLAMENTARIAS, P.HORAS_REQUERIDAS)),0) " +
                    "FROM MATRICULA_PRACTICA M INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                    "WHERE M.ID_MATRICULA_PRACTICA = ?";
            ps = ConexionBD.getConnection().prepareStatement(sql);
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (Exception e) {
            return 0.0;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    private boolean horasCumplidas(int idMatriculaPractica) {
        double requeridas = horasRequeridas(idMatriculaPractica);
        return requeridas > 0 && totalHorasAprobadas(idMatriculaPractica) >= requeridas;
    }

    private boolean evaluacionAprobada(int idMatriculaPractica) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ConexionBD.getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM EVALUACION WHERE ID_MATRICULA_PRACTICA = ? " +
                    "AND NVL(NOTA_EVALUACION, NVL(PUNTAJE_OBTENIDO,0)) >= 3.0");
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            return false;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    private boolean documentosAprobados(int idMatriculaPractica) {
        if (!DBHelper.existeTabla("DOCUMENTO_ESTUDIANTE")) {
            return true;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ConexionBD.getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM DOCUMENTO_ESTUDIANTE WHERE ID_MATRICULA_PRACTICA = ? " +
                    "AND NVL(UPPER(ESTADO),'APROBADO') IN ('APROBADO','VALIDADO')");
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) >= 1;
        } catch (Exception e) {
            return false;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    private String etapaActual(int idMatriculaPractica) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String columna = DBHelper.tieneColumna("MATRICULA_PRACTICA", "ETAPA_ACADEMICA") ? "ETAPA_ACADEMICA" : "ESTADO";
            ps = ConexionBD.getConnection().prepareStatement("SELECT " + columna + " FROM MATRICULA_PRACTICA WHERE ID_MATRICULA_PRACTICA = ?");
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "ELECTIVA";
        } catch (Exception e) {
            return "ELECTIVA";
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    private void registrarHistorial(int idMatriculaPractica, int idUsuarioResponsable, String anterior, String nuevo, String observacion) throws Exception {
        if (!DBHelper.existeTabla("HISTORIAL_REVISION")) {
            return;
        }
        LinkedHashMap<String, Object> datos = new LinkedHashMap<>();
        if (DBHelper.tieneColumna("HISTORIAL_REVISION", "ID_HISTORIAL_REVISION")) {
            datos.put("ID_HISTORIAL_REVISION", Integer.valueOf(DBHelper.siguienteId("HISTORIAL_REVISION", "ID_HISTORIAL_REVISION")));
        }
        datos.put("ID_MATRICULA_PRACTICA", Integer.valueOf(idMatriculaPractica));
        if (DBHelper.tieneColumna("HISTORIAL_REVISION", "ID_USUARIO_REVISA")) {
            datos.put("ID_USUARIO_REVISA", Integer.valueOf(idUsuarioResponsable));
        }
        if (DBHelper.tieneColumna("HISTORIAL_REVISION", "ESTADO_ANTERIOR")) {
            datos.put("ESTADO_ANTERIOR", anterior);
        }
        if (DBHelper.tieneColumna("HISTORIAL_REVISION", "ESTADO_NUEVO")) {
            datos.put("ESTADO_NUEVO", nuevo);
        }
        if (DBHelper.tieneColumna("HISTORIAL_REVISION", "OBSERVACION")) {
            datos.put("OBSERVACION", observacion);
        }
        if (DBHelper.tieneColumna("HISTORIAL_REVISION", "TIPO_REVISION")) {
            datos.put("TIPO_REVISION", "CAMBIO_ETAPA");
        }
        DBHelper.insertar("HISTORIAL_REVISION", datos);
    }
}
