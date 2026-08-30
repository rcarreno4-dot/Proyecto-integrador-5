package com.gestionpracticas.dao;

import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DAO genérico para operar tablas auxiliares sin crear una pantalla específica
 * por cada una de las 39 estructuras del modelo. Valida nombres de tabla y
 * columna contra USER_TABLES y USER_TAB_COLUMNS para evitar SQL dinámico inseguro.
 */
public class GenericCrudDAO {

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

    public List<Map<String, Object>> listar(String tabla, int maxFilas) throws Exception {
        validarTabla(tabla);
        String orden = detectarId(tabla);
        String sql = "SELECT * FROM " + tabla + (orden != null ? " ORDER BY " + orden : "") + " FETCH FIRST " + maxFilas + " ROWS ONLY";
        // Oracle 10g no soporta FETCH FIRST: se usa ROWNUM para compatibilidad.
        sql = "SELECT * FROM (SELECT * FROM " + tabla + (orden != null ? " ORDER BY " + orden : "") + ") WHERE ROWNUM <= ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ConexionBD.getConnection().prepareStatement(sql);
            ps.setInt(1, maxFilas <= 0 ? 200 : maxFilas);
            rs = ps.executeQuery();
            return mapear(rs);
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    public Map<String, Object> buscarPorId(String tabla, String idCol, Object id) throws Exception {
        validarTabla(tabla);
        validarColumna(tabla, idCol);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ConexionBD.getConnection().prepareStatement("SELECT * FROM " + tabla + " WHERE " + idCol + " = ?");
            ps.setObject(1, id);
            rs = ps.executeQuery();
            List<Map<String, Object>> filas = mapear(rs);
            return filas.isEmpty() ? null : filas.get(0);
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    public boolean insertar(String tabla, LinkedHashMap<String, Object> datos, Integer idUsuario) throws Exception {
        validarTabla(tabla);
        validarColumnas(tabla, datos.keySet());
        boolean ok = DBHelper.insertar(tabla, datos);
        if (ok) {
            auditoriaDAO.registrar(idUsuario, tabla, detectarValorId(datos), "INSERT", null, datos.toString(), "GenericCrudDAO");
        }
        return ok;
    }

    public boolean actualizar(String tabla, String idCol, Object id, LinkedHashMap<String, Object> datos, Integer idUsuario) throws Exception {
        validarTabla(tabla);
        validarColumna(tabla, idCol);
        validarColumnas(tabla, datos.keySet());
        Map<String, Object> anterior = buscarPorId(tabla, idCol, id);
        boolean ok = DBHelper.actualizar(tabla, idCol, id, datos);
        if (ok) {
            auditoriaDAO.registrar(idUsuario, tabla, String.valueOf(id), "UPDATE", String.valueOf(anterior), datos.toString(), "GenericCrudDAO");
        }
        return ok;
    }

    public boolean eliminarLogico(String tabla, String idCol, Object id, Integer idUsuario) throws Exception {
        validarTabla(tabla);
        validarColumna(tabla, idCol);
        Map<String, Object> anterior = buscarPorId(tabla, idCol, id);
        boolean ok = DBHelper.eliminarLogico(tabla, idCol, id);
        if (ok) {
            auditoriaDAO.registrar(idUsuario, tabla, String.valueOf(id), "DELETE", String.valueOf(anterior), "ESTADO=INACTIVO", "GenericCrudDAO");
        }
        return ok;
    }

    public String detectarId(String tabla) throws Exception {
        validarTabla(tabla);
        Set<String> columnas = DBHelper.columnas(tabla);
        for (String c : columnas) {
            if (c.equals("ID_" + tabla)) return c;
        }
        for (String c : columnas) {
            if (c.startsWith("ID_")) return c;
        }
        return null;
    }

    private static List<Map<String, Object>> mapear(ResultSet rs) throws Exception {
        List<Map<String, Object>> filas = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        int total = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> fila = new LinkedHashMap<>();
            for (int i = 1; i <= total; i++) {
                fila.put(md.getColumnLabel(i), rs.getObject(i));
            }
            filas.add(fila);
        }
        return filas;
    }

    private static void validarTabla(String tabla) throws Exception {
        if (tabla == null || !tabla.matches("[A-Za-z0-9_]+")) {
            throw new IllegalArgumentException("Nombre de tabla inválido: " + tabla);
        }
        if (!DBHelper.existeTabla(tabla.toUpperCase())) {
            throw new IllegalArgumentException("La tabla no existe en el esquema Oracle: " + tabla);
        }
    }

    private static void validarColumna(String tabla, String columna) throws Exception {
        if (columna == null || !columna.matches("[A-Za-z0-9_]+") || !DBHelper.tieneColumna(tabla, columna)) {
            throw new IllegalArgumentException("Columna inválida: " + columna + " para tabla " + tabla);
        }
    }

    private static void validarColumnas(String tabla, Set<String> columnas) throws Exception {
        for (String c : columnas) {
            validarColumna(tabla, c);
        }
    }

    private static String detectarValorId(LinkedHashMap<String, Object> datos) {
        for (Map.Entry<String, Object> e : datos.entrySet()) {
            if (e.getKey().toUpperCase().startsWith("ID_")) {
                return String.valueOf(e.getValue());
            }
        }
        return null;
    }
}
