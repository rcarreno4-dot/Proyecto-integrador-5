package com.gestionpracticas.servicio;

import com.gestionpracticas.util.DBHelper;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;

/** Servicio para registrar documentos y evidencias de estudiantes. */
public class DocumentoEstudianteService {

    private final AuditoriaService auditoriaService = new AuditoriaService();

    public String guardarArchivo(File origen, int idMatriculaPractica, String tipoDocumento) throws Exception {
        if (origen == null || !origen.exists()) {
            throw new IllegalArgumentException("El archivo de origen no existe.");
        }
        validarExtension(origen.getName());
        Path carpeta = Path.of("data", "documentos", String.valueOf(idMatriculaPractica));
        Files.createDirectories(carpeta);
        String nombreSeguro = System.currentTimeMillis() + "_" + origen.getName().replaceAll("[^A-Za-z0-9._-]", "_");
        Path destino = carpeta.resolve(nombreSeguro);
        Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return destino.toString();
    }

    public boolean registrarDocumento(int idMatriculaPractica, int idUsuarioSube, String tipoDocumento, String rutaArchivo) throws Exception {
        LinkedHashMap<String, Object> datos = new LinkedHashMap<>();
        datos.put("ID_DOCUMENTO_ESTUDIANTE", Integer.valueOf(DBHelper.siguienteId("DOCUMENTO_ESTUDIANTE", "ID_DOCUMENTO_ESTUDIANTE")));
        datos.put("ID_MATRICULA_PRACTICA", Integer.valueOf(idMatriculaPractica));
        datos.put("ID_USUARIO_SUBE", Integer.valueOf(idUsuarioSube));
        datos.put("TIPO_DOCUMENTO", tipoDocumento);
        datos.put("NOMBRE_ARCHIVO", new File(rutaArchivo).getName());
        datos.put("RUTA_ARCHIVO", rutaArchivo);
        datos.put("ESTADO", "CARGADO");
        boolean ok = DBHelper.insertar("DOCUMENTO_ESTUDIANTE", datos);
        if (ok) {
            auditoriaService.registrar(Integer.valueOf(idUsuarioSube), "DOCUMENTO_ESTUDIANTE", String.valueOf(datos.get("ID_DOCUMENTO_ESTUDIANTE")),
                    "INSERT", null, datos.toString(), "DocumentoEstudianteService");
        }
        return ok;
    }

    private static void validarExtension(String nombre) {
        String n = nombre.toLowerCase();
        if (!(n.endsWith(".pdf") || n.endsWith(".doc") || n.endsWith(".docx") || n.endsWith(".jpg") || n.endsWith(".jpeg") || n.endsWith(".png"))) {
            throw new IllegalArgumentException("Formato no permitido. Use PDF, DOC, DOCX, JPG o PNG.");
        }
    }
}
