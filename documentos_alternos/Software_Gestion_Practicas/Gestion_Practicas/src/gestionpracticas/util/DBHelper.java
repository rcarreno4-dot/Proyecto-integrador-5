package com.gestionpracticas.util;

import java.sql.*;
import java.util.*;

public class DBHelper {

    private static final Map<String, Set<String>> CACHE_COLUMNAS = new HashMap<String, Set<String>>();

    public static Connection con() {
        return ConexionBD.getConnection();
    }

    public static boolean existeTabla(String tabla) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con().prepareStatement("SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = UPPER(?)");
            ps.setString(1, tabla);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            return false;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    public static Set<String> columnas(String tabla) {
        String t = tabla.toUpperCase();
        if (CACHE_COLUMNAS.containsKey(t)) return CACHE_COLUMNAS.get(t);
        Set<String> cols = new HashSet<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con().prepareStatement("SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = UPPER(?)");
            ps.setString(1, tabla);
            rs = ps.executeQuery();
            while (rs.next()) cols.add(rs.getString(1).toUpperCase());
        } catch (Exception e) {
            System.err.println("DBHelper.columnas(" + tabla + "): " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        CACHE_COLUMNAS.put(t, cols);
        return cols;
    }

    public static void refrescarColumnas(String tabla) {
        if (tabla != null) CACHE_COLUMNAS.remove(tabla.toUpperCase());
    }

    public static boolean tieneColumna(String tabla, String columna) {
        return columnas(tabla).contains(columna.toUpperCase());
    }

    public static String primeraColumna(String tabla, String... opciones) {
        Set<String> cols = columnas(tabla);
        for (int i = 0; i < opciones.length; i++) {
            String c = opciones[i].toUpperCase();
            if (cols.contains(c)) return c;
        }
        return null;
    }

    public static String exprColumna(String tabla, String alias, String... opciones) {
        String c = primeraColumna(tabla, opciones);
        if (c == null) return "NULL AS " + alias;
        return c + " AS " + alias;
    }

    public static int contar(String tabla) {
        Statement st = null;
        ResultSet rs = null;
        try {
            if (!existeTabla(tabla)) return 0;
            st = con().createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM " + tabla);
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            return 0;
        } finally {
            Utilidades.cerrar(rs);
            try { if (st != null) st.close(); } catch (Exception ex) { }
        }
    }

    public static int contarWhere(String tabla, String where) {
        Statement st = null;
        ResultSet rs = null;
        try {
            if (!existeTabla(tabla)) return 0;
            st = con().createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM " + tabla + " WHERE " + where);
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            return 0;
        } finally {
            Utilidades.cerrar(rs);
            try { if (st != null) st.close(); } catch (Exception ex) { }
        }
    }

    public static double sumarWhere(String tabla, String columna, String where) {
        Statement st = null;
        ResultSet rs = null;
        try {
            if (!existeTabla(tabla) || !tieneColumna(tabla, columna)) return 0;
            st = con().createStatement();
            rs = st.executeQuery("SELECT NVL(SUM(" + columna + "),0) FROM " + tabla + " WHERE " + where);
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (Exception e) {
            return 0;
        } finally {
            Utilidades.cerrar(rs);
            try { if (st != null) st.close(); } catch (Exception ex) { }
        }
    }

    public static int siguienteId(String tabla, String idCol) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con().prepareStatement("SELECT NVL(MAX(" + idCol + "),0) + 1 FROM " + tabla);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 1;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
    }

    public static Integer buscarIdPorTexto(String tabla, String idCol, String textoCol, String valor) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (!existeTabla(tabla) || !tieneColumna(tabla, idCol) || !tieneColumna(tabla, textoCol)) return null;
            ps = con().prepareStatement("SELECT " + idCol + " FROM " + tabla + " WHERE UPPER(" + textoCol + ") = UPPER(?)");
            ps.setString(1, valor);
            rs = ps.executeQuery();
            if (rs.next()) return Integer.valueOf(rs.getInt(1));
        } catch (Exception e) {
            System.err.println("DBHelper.buscarIdPorTexto: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return null;
    }

    public static boolean insertar(String tabla, LinkedHashMap<String, Object> datos) {
        PreparedStatement ps = null;
        try {
            Set<String> colsTabla = columnas(tabla);
            StringBuilder cols = new StringBuilder();
            StringBuilder qs = new StringBuilder();
            ArrayList<Object> valores = new ArrayList<Object>();
            Iterator<Map.Entry<String, Object>> it = datos.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> e = it.next();
                String col = e.getKey().toUpperCase();
                if (!colsTabla.contains(col)) continue;
                if (cols.length() > 0) { cols.append(", "); qs.append(", "); }
                cols.append(col);
                qs.append("?");
                valores.add(e.getValue());
            }
            if (valores.isEmpty()) return false;
            String sql = "INSERT INTO " + tabla + " (" + cols.toString() + ") VALUES (" + qs.toString() + ")";
            ps = con().prepareStatement(sql);
            for (int i = 0; i < valores.size(); i++) setParam(ps, i + 1, valores.get(i));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("DBHelper.insertar " + tabla + ": " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    public static boolean actualizar(String tabla, String idCol, Object id, LinkedHashMap<String, Object> datos) {
        PreparedStatement ps = null;
        try {
            Set<String> colsTabla = columnas(tabla);
            StringBuilder set = new StringBuilder();
            ArrayList<Object> valores = new ArrayList<Object>();
            Iterator<Map.Entry<String, Object>> it = datos.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> e = it.next();
                String col = e.getKey().toUpperCase();
                if (!colsTabla.contains(col) || col.equalsIgnoreCase(idCol)) continue;
                if (set.length() > 0) set.append(", ");
                set.append(col).append(" = ?");
                valores.add(e.getValue());
            }
            if (valores.isEmpty()) return false;
            String sql = "UPDATE " + tabla + " SET " + set.toString() + " WHERE " + idCol + " = ?";
            ps = con().prepareStatement(sql);
            for (int i = 0; i < valores.size(); i++) setParam(ps, i + 1, valores.get(i));
            setParam(ps, valores.size() + 1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("DBHelper.actualizar " + tabla + ": " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    public static boolean eliminarLogico(String tabla, String idCol, Object id) {
        try {
            if (tieneColumna(tabla, "ESTADO")) {
                LinkedHashMap<String, Object> datos = new LinkedHashMap<String, Object>();
                datos.put("ESTADO", "INACTIVO");
                return actualizar(tabla, idCol, id, datos);
            }
            return borrar(tabla, idCol, id);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean borrar(String tabla, String idCol, Object id) {
        PreparedStatement ps = null;
        try {
            ps = con().prepareStatement("DELETE FROM " + tabla + " WHERE " + idCol + " = ?");
            setParam(ps, 1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("DBHelper.borrar " + tabla + ": " + e.getMessage());
            return false;
        } finally {
            Utilidades.cerrar(ps);
        }
    }

    public static ArrayList<String[]> consultaTabla(String sql, int columnas) {
        ArrayList<String[]> filas = new ArrayList<String[]>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con().createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                String[] fila = new String[columnas];
                for (int i = 0; i < columnas; i++) fila[i] = rs.getString(i + 1);
                filas.add(fila);
            }
        } catch (Exception e) {
            System.err.println("DBHelper.consultaTabla: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs);
            try { if (st != null) st.close(); } catch (Exception ex) { }
        }
        return filas;
    }


    public static int prioridadEstado(String estado) {
        if (estado == null) return 99;
        String e = estado.trim().toUpperCase();
        if (e.equals("ACTIVO")) return 1;
        if (e.equals("PENDIENTE")) return 2;
        if (e.equals("APROBADO")) return 3;
        if (e.equals("REGISTRADO") || e.equals("REGISTRADA")) return 4;
        if (e.equals("ENVIADA")) return 5;
        if (e.equals("RECHAZADO") || e.equals("REPROBADO")) return 6;
        if (e.equals("FINALIZADO") || e.equals("FINALIZADA")) return 7;
        if (e.equals("INACTIVO")) return 8;
        if (e.equals("ELIMINADO")) return 9;
        return 50;
    }

    public static String ordenEstadoSql(String tabla, String idCol) {
        String id = (idCol == null || idCol.trim().length() == 0) ? "1" : idCol;
        if (!tieneColumna(tabla, "ESTADO")) return id;
        return "CASE NVL(UPPER(ESTADO),'ACTIVO') " +
               "WHEN 'ACTIVO' THEN 1 " +
               "WHEN 'PENDIENTE' THEN 2 " +
               "WHEN 'APROBADO' THEN 3 " +
               "WHEN 'REGISTRADO' THEN 4 " +
               "WHEN 'REGISTRADA' THEN 4 " +
               "WHEN 'ENVIADA' THEN 5 " +
               "WHEN 'RECHAZADO' THEN 6 " +
               "WHEN 'REPROBADO' THEN 6 " +
               "WHEN 'FINALIZADO' THEN 7 " +
               "WHEN 'FINALIZADA' THEN 7 " +
               "WHEN 'INACTIVO' THEN 8 " +
               "WHEN 'ELIMINADO' THEN 9 " +
               "ELSE 50 END, " + id;
    }

    public static boolean cambiarEstado(String tabla, String idCol, Object id, String estado) {
        try {
            if (!tieneColumna(tabla, "ESTADO")) return false;
            LinkedHashMap<String, Object> datos = new LinkedHashMap<String, Object>();
            datos.put("ESTADO", estado);
            return actualizar(tabla, idCol, id, datos);
        } catch (Exception e) {
            System.err.println("DBHelper.cambiarEstado " + tabla + ": " + e.getMessage());
            return false;
        }
    }

    public static Object convertirValor(String tabla, String columna, String valor) {
        if (valor == null || valor.trim().length() == 0) return null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con().prepareStatement("SELECT DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = UPPER(?) AND COLUMN_NAME = UPPER(?)");
            ps.setString(1, tabla);
            ps.setString(2, columna);
            rs = ps.executeQuery();
            if (rs.next()) {
                String tipo = rs.getString(1);
                if (tipo != null) {
                    tipo = tipo.toUpperCase();
                    if (tipo.indexOf("NUMBER") >= 0) {
                        if (valor.indexOf('.') >= 0 || valor.indexOf(',') >= 0) return Double.valueOf(valor.replace(',', '.'));
                        return Integer.valueOf(valor);
                    }
                    if (tipo.indexOf("DATE") >= 0) {
                        try { return java.sql.Date.valueOf(valor); } catch (Exception ex) { return valor; }
                    }
                }
            }
        } catch (Exception e) {
            return valor;
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        return valor;
    }

    private static void setParam(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) ps.setNull(index, Types.VARCHAR);
        else if (value instanceof Integer) ps.setInt(index, ((Integer)value).intValue());
        else if (value instanceof Long) ps.setLong(index, ((Long)value).longValue());
        else if (value instanceof Double) ps.setDouble(index, ((Double)value).doubleValue());
        else if (value instanceof Float) ps.setFloat(index, ((Float)value).floatValue());
        else if (value instanceof java.util.Date) ps.setDate(index, new java.sql.Date(((java.util.Date)value).getTime()));
        else ps.setString(index, String.valueOf(value));
    }
}
