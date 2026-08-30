package com.gestionpracticas.util;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

    // ==================== FORMATOS DE FECHA ====================
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String FORMATO_FECHA_HORA = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMATO_FECHA_DB = "yyyy-MM-dd";

    // ==================== CIERRE DE RECURSOS ====================
    public static void cerrar(ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error cerrando ResultSet: " + e.getMessage());
        }
    }

    public static void cerrar(PreparedStatement ps) {
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Error cerrando PreparedStatement: " + e.getMessage());
        }
    }

    public static void cerrar(PreparedStatement ps, ResultSet rs) {
        cerrar(rs);
        cerrar(ps);
    }

    // ==================== VALIDACIONES ====================
    public static boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean noEsVacio(String texto) {
        return !esVacio(texto);
    }

    public static boolean esEmailValido(String email) {
        if (esVacio(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean esPositivo(double numero) {
        return numero > 0;
    }

    public static boolean estaEnRango(double numero, double min, double max) {
        return numero >= min && numero <= max;
    }

    public static boolean idValido(int id) {
        return id > 0;
    }

    // ==================== FORMATEO DE FECHAS ====================
    public static String fechaAString(Date fecha) {
        if (fecha == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        return sdf.format(fecha);
    }

    public static String fechaAString(Date fecha, String formato) {
        if (fecha == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(fecha);
    }

    public static Date stringAFecha(String fechaStr) {
        if (esVacio(fechaStr)) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
            return sdf.parse(fechaStr);
        } catch (ParseException e) {
            System.err.println("Error parseando fecha: " + e.getMessage());
            return null;
        }
    }

    public static Date stringAFecha(String fechaStr, String formato) {
        if (esVacio(fechaStr)) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            return sdf.parse(fechaStr);
        } catch (ParseException e) {
            System.err.println("Error parseando fecha: " + e.getMessage());
            return null;
        }
    }

    public static String fechaParaDB(Date fecha) {
        if (fecha == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA_DB);
        return sdf.format(fecha);
    }

    // ==================== MANIPULACIÓN DE STRINGS ====================
    public static String capitalizar(String texto) {
        if (esVacio(texto)) return "";
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    public static String capitalizarPalabras(String texto) {
        if (esVacio(texto)) return "";
        String[] palabras = texto.toLowerCase().split(" ");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (palabra.length() > 0) {
                resultado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1))
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }

    public static String limpiarString(String texto) {
        if (esVacio(texto)) return "";
        return texto.trim().replaceAll("\\s+", " ");
    }

    public static String truncar(String texto, int maxLongitud) {
        if (esVacio(texto)) return "";
        if (texto.length() <= maxLongitud) return texto;
        return texto.substring(0, maxLongitud - 3) + "...";
    }

    // ==================== NÚMEROS ====================
    public static String formatearDecimal(double numero) {
        return String.format("%.2f", numero);
    }

    public static double stringToDouble(String valor) {
        if (esVacio(valor)) return 0;
        try {
            return Double.parseDouble(valor.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int stringToInt(String valor) {
        if (esVacio(valor)) return 0;
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ==================== DOCUMENTOS ====================
    public static boolean esNitValido(String nit) {
        if (esVacio(nit)) return false;
        return nit.matches("^[0-9]+-[0-9]$");
    }

    public static boolean esTelefonoValido(String telefono) {
        if (esVacio(telefono)) return false;
        return telefono.matches("^[0-9]{10}$");
    }

    // ==================== CONSTANTES DEL SISTEMA ====================
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_INACTIVO = "INACTIVO";
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_APROBADO = "APROBADO";
    public static final String ESTADO_RECHAZADO = "RECHAZADO";
    public static final String ESTADO_FINALIZADO = "FINALIZADO";
    public static final String ESTADO_REALIZADA = "REALIZADA";

    public static final String TIPO_DIRECTOR = "DIRECTOR";
    public static final String TIPO_COORDINADOR = "COORDINADOR";
    public static final String TIPO_DOCENTE = "DOCENTE";
    public static final String TIPO_ESTUDIANTE = "ESTUDIANTE";
    public static final String TIPO_INSTITUCION = "INSTITUCION";

    public static final String ACTIVIDAD_DESARROLLO = "DESARROLLO";
    public static final String ACTIVIDAD_REUNION = "REUNION";
    public static final String ACTIVIDAD_CAPACITACION = "CAPACITACION";
    public static final String ACTIVIDAD_DOCUMENTACION = "DOCUMENTACION";

    public static final String TIPO_PREGUNTA_ABIERTA = "ABIERTA";
    public static final String TIPO_PREGUNTA_OPCION_MULTIPLE = "OPCION_MULTIPLE";
    public static final String TIPO_PREGUNTA_ESCALA = "ESCALA";
    public static final String TIPO_PREGUNTA_NUMERICO = "NUMERICO";

    // ==================== UTILIDADES ADICIONALES ====================
    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) return true;
        if (obj1 == null || obj2 == null) return false;
        return obj1.equals(obj2);
    }

    public static <T> T getOrDefault(T valor, T defaultValue) {
        return valor != null ? valor : defaultValue;
    }
}