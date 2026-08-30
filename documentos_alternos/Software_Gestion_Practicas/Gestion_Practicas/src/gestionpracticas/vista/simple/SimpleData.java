package com.gestionpracticas.vista.simple;

import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utilidades SQL usadas por las pantallas simplificadas.
 * Ocultan los ID al usuario y hacen las consultas compatibles con las
 * variaciones de esquema que hay entre las entregas del proyecto.
 */
public final class SimpleData {
    private SimpleData() {}

    public static class Item {
        public final int id;
        public final String texto;
        public Item(int id, String texto) { this.id = id; this.texto = texto == null ? "" : texto; }
        public String toString() { return texto; }
    }

    public static Connection con() { return ConexionBD.getConnection(); }

    public static int nextId(String tabla, String idCol, String seq) throws SQLException {
        Statement st = null; ResultSet rs = null;
        try {
            st = con().createStatement();
            rs = st.executeQuery("SELECT " + seq + ".NEXTVAL FROM DUAL");
            if (rs.next()) return rs.getInt(1);
        } catch (Exception ex) {
            // En bases importadas puede no existir la secuencia. Se usa MAX+1.
        } finally {
            Utilidades.cerrar(rs);
            try { if (st != null) st.close(); } catch (Exception e) { }
        }
        return DBHelper.siguienteId(tabla, idCol);
    }

    public static java.sql.Date parseDate(String valor) {
        if (valor == null || valor.trim().length() == 0) return new java.sql.Date(System.currentTimeMillis());
        String v = valor.trim();
        String[] formatos = {"yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy"};
        for (int i = 0; i < formatos.length; i++) {
            try { return new java.sql.Date(new SimpleDateFormat(formatos[i]).parse(v).getTime()); }
            catch (ParseException ex) { }
        }
        return new java.sql.Date(System.currentTimeMillis());
    }

    public static String nvl(String s, String def) { return s == null || s.trim().length() == 0 ? def : s.trim(); }

    public static boolean has(String tabla, String columna) { return DBHelper.tieneColumna(tabla, columna); }

    public static String col(String tabla, String... opciones) { return DBHelper.primeraColumna(tabla, opciones); }

    public static String q(String alias, String tabla, String... opciones) {
        String c = col(tabla, opciones);
        return c == null ? null : alias + "." + c;
    }

    public static String lit(String s) { return "'" + s.replace("'", "''") + "'"; }

    public static String nvlExpr(String alias, String tabla, String def, String... opciones) {
        String c = q(alias, tabla, opciones);
        return c == null ? lit(def) : "NVL(" + c + ", " + lit(def) + ")";
    }

    public static String usuarioNombreExpr(String alias) {
        String nombre = q(alias, "USUARIO", "NOMBRE", "NOMBRES");
        String apellido = q(alias, "USUARIO", "APELLIDO", "APELLIDOS");
        if (nombre == null && apellido == null) return lit("Usuario");
        if (nombre == null) return "TRIM(NVL(" + apellido + ",''))";
        if (apellido == null) return "TRIM(NVL(" + nombre + ",''))";
        return "TRIM(NVL(" + nombre + ",'') || ' ' || NVL(" + apellido + ",''))";
    }

    public static String practicaTituloExpr(String alias) { return nvlExpr(alias, "PRACTICA", "Sin práctica", "TITULO", "NOMBRE", "NOMBRE_PRACTICA"); }
    public static String institucionNombreExpr(String alias) { return nvlExpr(alias, "INSTITUCION", "Sin institución", "NOMBRE", "RAZON_SOCIAL"); }
    public static String grupoNombreExpr(String alias) { return nvlExpr(alias, "GRUPO", "Sin grupo", "NOMBRE", "NOMBRE_GRUPO"); }

    public static String matriculaEstudianteCol() { return col("MATRICULA_PRACTICA", "ID_ESTUDIANTE", "ID_USUARIO"); }
    public static String matriculaInstitucionExpr(String aliasM, String aliasP) {
        String m = q(aliasM, "MATRICULA_PRACTICA", "ID_INSTITUCION");
        if (m != null) return m;
        String p = q(aliasP, "PRACTICA", "ID_INSTITUCION");
        return p;
    }

    public static String estadoExpr(String alias, String tabla, String def) { return nvlExpr(alias, tabla, def, "ESTADO"); }

    public static String etapaMatriculaExpr(String alias) {
        String e = q(alias, "MATRICULA_PRACTICA", "ETAPA_ACADEMICA", "ETAPA");
        return e == null ? lit("PRODUCTIVA") : "NVL(" + e + ", 'PRODUCTIVA')";
    }

    public static String notaExpr(String alias) {
        String p = q(alias, "EVALUACION", "PUNTAJE_OBTENIDO", "PUNTAJE", "CALIFICACION", "NOTA_FINAL");
        String n = q(alias, "EVALUACION", "NOTA_EVALUACION", "NOTA");
        if (p != null && n != null) return "NVL(" + p + ", " + n + ")";
        if (p != null) return p;
        if (n != null) return n;
        return "0";
    }

    public static String preguntaTextoExpr(String alias) {
        String a = q(alias, "PREGUNTA", "ENUNCIADO");
        String b = q(alias, "PREGUNTA", "TEXTO_PREGUNTA", "PREGUNTA");
        if (a != null && b != null) return "NVL(" + a + "," + b + ")";
        if (a != null) return a;
        if (b != null) return b;
        return lit("Pregunta sin texto");
    }

    public static String respuestaTextoExpr(String alias) {
        String a = q(alias, "RESPUESTA", "CONTENIDO_RESPUESTA");
        String b = q(alias, "RESPUESTA", "RESPUESTA", "TEXTO_RESPUESTA");
        if (a != null && b != null) return "NVL(" + a + "," + b + ")";
        if (a != null) return a;
        if (b != null) return b;
        return lit("");
    }

    public static String fechaExpr(String alias, String tabla, String def, String... opciones) {
        String c = q(alias, tabla, opciones);
        return c == null ? "SYSDATE" : c;
    }

    public static List<Item> estudiantes() {
        List<Item> lista = new ArrayList<Item>();
        String rol = col("USUARIO", "ROL", "TIPO_USUARIO", "PERFIL");
        String estado = col("USUARIO", "ESTADO");
        String where = "1=1";
        if (estado != null) where += " AND NVL(UPPER(" + estado + "),'ACTIVO') <> 'INACTIVO'";
        if (rol != null) where += " AND UPPER(NVL(" + rol + ",'')) LIKE '%ESTUDIANTE%'";
        String sql = "SELECT ID_USUARIO, " + usuarioNombreExpr("U") + " AS NOM FROM USUARIO U WHERE " + where + " ORDER BY NOM";
        consultarItems(sql, lista);
        return lista;
    }

    public static List<Item> docentes() {
        List<Item> lista = new ArrayList<Item>();
        String rol = col("USUARIO", "ROL", "TIPO_USUARIO", "PERFIL");
        String estado = col("USUARIO", "ESTADO");
        String where = "1=1";
        if (estado != null) where += " AND NVL(UPPER(" + estado + "),'ACTIVO') <> 'INACTIVO'";
        if (rol != null) where += " AND UPPER(NVL(" + rol + ",'')) LIKE '%DOCENTE%'";
        String sql = "SELECT ID_USUARIO, " + usuarioNombreExpr("U") + " AS NOM FROM USUARIO U WHERE " + where + " ORDER BY NOM";
        consultarItems(sql, lista);
        return lista;
    }

    public static List<Item> practicas() {
        List<Item> lista = new ArrayList<Item>();
        String titulo = practicaTituloExpr("P");
        String tipo = nvlExpr("P", "PRACTICA", "Práctica", "TIPO_PRACTICA", "TIPO_MODALIDAD");
        String estado = estadoExpr("P", "PRACTICA", "ACTIVO");
        String where = has("PRACTICA", "ESTADO") ? "WHERE NVL(UPPER(P.ESTADO),'ACTIVO') <> 'INACTIVO'" : "";
        String sql = "SELECT P.ID_PRACTICA, " + titulo + " || ' - ' || " + tipo + " || ' (' || " + estado + " || ')' FROM PRACTICA P " + where + " ORDER BY " + titulo;
        consultarItems(sql, lista);
        return lista;
    }

    public static List<Item> instituciones() {
        List<Item> lista = new ArrayList<Item>();
        String nombre = institucionNombreExpr("I");
        String estado = estadoExpr("I", "INSTITUCION", "ACTIVO");
        String where = has("INSTITUCION", "ESTADO") ? "WHERE NVL(UPPER(I.ESTADO),'ACTIVO') <> 'INACTIVO'" : "";
        String sql = "SELECT I.ID_INSTITUCION, " + nombre + " || ' (' || " + estado + " || ')' FROM INSTITUCION I " + where + " ORDER BY " + nombre;
        consultarItems(sql, lista);
        return lista;
    }

    public static List<Item> grupos(Integer idPractica, Integer idInstitucion) {
        List<Item> lista = new ArrayList<Item>();
        String nombre = grupoNombreExpr("G");
        String estado = estadoExpr("G", "GRUPO", "ACTIVO");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT G.ID_GRUPO, ").append(nombre).append(" || ' - ' || ").append(estado).append(" FROM GRUPO G WHERE 1=1 ");
        if (idPractica != null && has("GRUPO", "ID_PRACTICA")) sql.append(" AND (G.ID_PRACTICA = ").append(idPractica.intValue()).append(" OR G.ID_PRACTICA IS NULL) ");
        if (idInstitucion != null && has("GRUPO", "ID_INSTITUCION")) sql.append(" AND (G.ID_INSTITUCION = ").append(idInstitucion.intValue()).append(" OR G.ID_INSTITUCION IS NULL) ");
        if (has("GRUPO", "ESTADO")) sql.append(" AND NVL(UPPER(G.ESTADO),'ACTIVO') <> 'INACTIVO' ");
        sql.append(" ORDER BY ").append(nombre);
        consultarItems(sql.toString(), lista);
        return lista;
    }

    public static List<Item> matriculasDeEstudiante(int idEstudiante) {
        List<Item> lista = new ArrayList<Item>();
        String idEstCol = matriculaEstudianteCol();
        if (idEstCol == null || !DBHelper.existeTabla("MATRICULA_PRACTICA")) return lista;
        String joins = " LEFT JOIN PRACTICA P ON P.ID_PRACTICA = M.ID_PRACTICA ";
        String instJoinExpr = matriculaInstitucionExpr("M", "P");
        if (instJoinExpr != null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION = " + instJoinExpr + " ";
        String instText = instJoinExpr == null ? lit("Sin institución") : institucionNombreExpr("I");
        String fecha = has("MATRICULA_PRACTICA", "FECHA_MATRICULA") ? "M.FECHA_MATRICULA" : "M.ID_MATRICULA_PRACTICA";
        String sql = "SELECT M.ID_MATRICULA_PRACTICA, " + practicaTituloExpr("P") + " || ' - ' || " + instText + " || ' - ' || " + estadoExpr("M", "MATRICULA_PRACTICA", "ACTIVO") +
                " FROM MATRICULA_PRACTICA M " + joins + " WHERE M." + idEstCol + " = " + idEstudiante + " ORDER BY " + fecha + " DESC";
        consultarItems(sql, lista);
        return lista;
    }

    public static List<Item> matriculasTodas() {
        List<Item> lista = new ArrayList<Item>();
        String idEstCol = matriculaEstudianteCol();
        if (idEstCol == null || !DBHelper.existeTabla("MATRICULA_PRACTICA")) return lista;
        String joins = " JOIN USUARIO U ON U.ID_USUARIO = M." + idEstCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA = M.ID_PRACTICA ";
        String instJoinExpr = matriculaInstitucionExpr("M", "P");
        if (instJoinExpr != null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION = " + instJoinExpr + " ";
        String instText = instJoinExpr == null ? lit("Sin institución") : institucionNombreExpr("I");
        String sql = "SELECT M.ID_MATRICULA_PRACTICA, " + usuarioNombreExpr("U") + " || ' - ' || " + practicaTituloExpr("P") + " || ' - ' || " + instText + " || ' - ' || " + estadoExpr("M", "MATRICULA_PRACTICA", "ACTIVO") +
                " FROM MATRICULA_PRACTICA M " + joins + " ORDER BY " + usuarioNombreExpr("U") + ", " + practicaTituloExpr("P");
        consultarItems(sql, lista);
        return lista;
    }

    public static int asegurarRubrica(int idPractica, int idDocente) throws SQLException {
        if (!DBHelper.existeTabla("RUBRICA") || !has("RUBRICA", "ID_RUBRICA")) return 0;
        String idP = col("RUBRICA", "ID_PRACTICA");
        String idD = col("RUBRICA", "ID_DOCENTE", "ID_USUARIO");
        String where = "WHERE 1=1";
        if (idP != null) where += " AND ID_PRACTICA=" + idPractica;
        if (idD != null) where += " AND " + idD + "=" + idDocente;
        Statement st = null; ResultSet rs = null;
        try {
            st = con().createStatement(); rs = st.executeQuery("SELECT ID_RUBRICA FROM RUBRICA " + where + " AND ROWNUM=1");
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { }
        finally { Utilidades.cerrar(rs); try { if (st != null) st.close(); } catch (Exception e) { } }

        int id = nextId("RUBRICA", "ID_RUBRICA", "SEQ_RUBRICA");
        LinkedHashMap<String,Object> r = new LinkedHashMap<String,Object>();
        r.put("ID_RUBRICA", id);
        r.put("NOMBRE", "Rúbrica general de práctica");
        r.put("DESCRIPCION", "Rúbrica creada automáticamente para publicar preguntas y calificar al estudiante.");
        r.put("ID_PRACTICA", idPractica);
        r.put("ID_DOCENTE", idDocente);
        r.put("ID_USUARIO", idDocente);
        r.put("PUNTAJE_TOTAL", Double.valueOf(100));
        r.put("FECHA_CREACION", new java.sql.Date(System.currentTimeMillis()));
        r.put("ESTADO", "ACTIVO");
        DBHelper.insertar("RUBRICA", r);
        return id;
    }

    private static void consultarItems(String sql, List<Item> lista) {
        Statement st = null; ResultSet rs = null;
        try {
            st = con().createStatement(); rs = st.executeQuery(sql);
            while (rs.next()) lista.add(new Item(rs.getInt(1), rs.getString(2)));
        } catch (Exception e) {
            System.err.println("SimpleData.consultarItems: " + e.getMessage() + " SQL=" + sql);
        } finally {
            Utilidades.cerrar(rs); try { if (st != null) st.close(); } catch (Exception e) { }
        }
    }

    public static boolean insertarLog(Integer idUsuario, String tabla, String operacion, String nuevo, String modulo) {
        try {
            if (!DBHelper.existeTabla("LOG_ACTIVIDAD")) return true;
            LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>();
            datos.put("ID_LOG", nextId("LOG_ACTIVIDAD", "ID_LOG", "SEQ_LOG_ACTIVIDAD"));
            datos.put("ID_USUARIO", idUsuario);
            datos.put("TABLA_AFECTADA", tabla);
            datos.put("TABLA", tabla);
            datos.put("TIPO_OPERACION", operacion);
            datos.put("ACCION", operacion);
            datos.put("VALOR_NUEVO", nuevo);
            datos.put("MODULO", modulo);
            datos.put("FECHA_HORA", new java.sql.Date(System.currentTimeMillis()));
            datos.put("FECHA_LOG", new java.sql.Date(System.currentTimeMillis()));
            datos.put("USUARIO_BD", System.getProperty("user.name", "APP"));
            return DBHelper.insertar("LOG_ACTIVIDAD", datos);
        } catch (Exception e) {
            System.err.println("SimpleData.insertarLog: " + e.getMessage()); return false;
        }
    }
}
