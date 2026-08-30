package com.gestionpracticas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton simple para conexión a Oracle.
 *
 * Lee la configuración desde resources/config.properties o config/config.properties.
 * Si no existe el archivo, usa valores seguros de ejemplo para Oracle XE local.
 */
public final class ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(ConexionBD.class.getName());
    private static Connection con;
    private static Properties propiedades;

    private ConexionBD() {
        // Clase utilitaria: no se instancia.
    }

    public static synchronized Connection getConnection() {
        try {
            if (con != null && !con.isClosed()) {
                return con;
            }

            Properties p = cargarPropiedades();
            String driver = p.getProperty("db.driver", "oracle.jdbc.driver.OracleDriver");
            String usuario = p.getProperty("db.user", "PROYECTOJD");
            String password = p.getProperty("db.password", "PROYECTOJD");
            Class.forName(driver);

            for (String url : obtenerUrls(p)) {
                try {
                    LOGGER.info("Intentando conexión Oracle: " + url);
                    con = DriverManager.getConnection(url, usuario, password);
                    con.setAutoCommit(true);
                    LOGGER.info("Conexión Oracle establecida correctamente: " + url);
                    return con;
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "No fue posible conectar con " + url + ": " + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error general de conexión a Oracle", e);
        }
        return null;
    }

    public static synchronized void cerrarConexion() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "No fue posible cerrar la conexión", e);
        } finally {
            con = null;
        }
    }

    public static synchronized void reiniciarConexion() {
        cerrarConexion();
        propiedades = null;
    }

    private static Properties cargarPropiedades() {
        if (propiedades != null) {
            return propiedades;
        }
        propiedades = new Properties();
        propiedades.setProperty("db.driver", "oracle.jdbc.driver.OracleDriver");
        propiedades.setProperty("db.url", "jdbc:oracle:thin:@localhost:1521:XE");
        propiedades.setProperty("db.user", "PROYECTOJD");
        propiedades.setProperty("db.password", "PROYECTOJD");
        propiedades.setProperty("db.fallback.urls",
                "jdbc:oracle:thin:@//localhost:1521/XE," +
                "jdbc:oracle:thin:@127.0.0.1:1521:XE," +
                "jdbc:oracle:thin:@//127.0.0.1:1521/XE");

        cargarDesdeClasspath(propiedades, "config.properties");
        cargarDesdeArchivo(propiedades, "resources/config.properties");
        cargarDesdeArchivo(propiedades, "config/config.properties");
        return propiedades;
    }

    private static void cargarDesdeClasspath(Properties p, String nombre) {
        try (InputStream in = ConexionBD.class.getClassLoader().getResourceAsStream(nombre)) {
            if (in != null) {
                p.load(in);
                LOGGER.info("Configuración cargada desde classpath: " + nombre);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No se pudo cargar " + nombre + " desde classpath", e);
        }
    }

    private static void cargarDesdeArchivo(Properties p, String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return;
        }
        try (InputStream in = new FileInputStream(archivo)) {
            p.load(in);
            LOGGER.info("Configuración cargada desde archivo: " + archivo.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No se pudo cargar " + ruta, e);
        }
    }

    private static List<String> obtenerUrls(Properties p) {
        List<String> urls = new ArrayList<>();
        agregarUrl(urls, p.getProperty("db.url"));
        String fallback = p.getProperty("db.fallback.urls", "");
        for (String url : fallback.split(",")) {
            agregarUrl(urls, url);
        }
        return urls;
    }

    private static void agregarUrl(List<String> urls, String url) {
        if (url == null) {
            return;
        }
        String limpia = url.trim();
        if (!limpia.isEmpty() && !urls.contains(limpia)) {
            urls.add(limpia);
        }
    }
}
