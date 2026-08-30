package com.gestionpracticas.dao;

import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO especializado en bitácora. Inserta registros en LOG_ACTIVIDAD sin asumir
 * una única variante de columnas, para mantener compatibilidad con esquemas
 * académicos que ya tienen nombres históricos.
 */
public class AuditoriaDAO {

    private static final Logger LOGGER = Logger.getLogger(AuditoriaDAO.class.getName());

    public boolean registrar(Integer idUsuario, String tabla, String idRegistro, String operacion,
                             String valorAnterior, String valorNuevo, String modulo) {
        if (!DBHelper.existeTabla("LOG_ACTIVIDAD")) {
            return false;
        }
        PreparedStatement ps = null;
        try {
            LinkedHashMap<String, Object> datos = new LinkedHashMap<>();
            putSiExiste(datos, "LOG_ACTIVIDAD", "ID_LOG", siguienteIdSeguro());
            putSiExiste(datos, "LOG_ACTIVIDAD", "ID_USUARIO", idUsuario);
            putSiExiste(datos, "LOG_ACTIVIDAD", "TABLA_AFECTADA", tabla);
            putSiExiste(datos, "LOG_ACTIVIDAD", "TABLA", tabla);
            putSiExiste(datos, "LOG_ACTIVIDAD", "ID_REGISTRO", idRegistro);
            putSiExiste(datos, "LOG_ACTIVIDAD", "TIPO_OPERACION", operacion);
            putSiExiste(datos, "LOG_ACTIVIDAD", "ACCION", operacion);
            putSiExiste(datos, "LOG_ACTIVIDAD", "VALOR_ANTERIOR", valorAnterior);
            putSiExiste(datos, "LOG_ACTIVIDAD", "VALOR_NUEVO", valorNuevo);
            putSiExiste(datos, "LOG_ACTIVIDAD", "MODULO", modulo);
            putSiExiste(datos, "LOG_ACTIVIDAD", "USUARIO_BD", System.getProperty("user.name"));
            putSiExiste(datos, "LOG_ACTIVIDAD", "IP_EQUIPO", ipLocal());
            return insertarFlexible("LOG_ACTIVIDAD", datos);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No fue posible registrar auditoría", e);
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    private static void putSiExiste(Map<String, Object> datos, String tabla, String columna, Object valor) {
        if (DBHelper.tieneColumna(tabla, columna)) {
            datos.put(columna, valor);
        }
    }

    private static boolean insertarFlexible(String tabla, LinkedHashMap<String, Object> datos) {
        return DBHelper.insertar(tabla, datos);
    }

    private static Integer siguienteIdSeguro() {
        try {
            return Integer.valueOf(DBHelper.siguienteId("LOG_ACTIVIDAD", "ID_LOG"));
        } catch (Exception e) {
            return Integer.valueOf(1);
        }
    }

    private static String ipLocal() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "LOCAL";
        }
    }
}
