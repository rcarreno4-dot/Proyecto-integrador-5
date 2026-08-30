package com.gestionpracticas.servicio;

import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Generador de PDF con iText 5.5.13.3. Incluye trazabilidad mediante hash
 * y texto verificable que puede convertirse en QR por un servicio externo.
 */
public class ReportePdfService {

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10);

    public void generarReporteHoras(int idMatriculaPractica, String rutaPdf) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(rutaPdf));
        doc.open();
        agregarEncabezado(doc, "Reporte de avance de horas");
        doc.add(new Paragraph("Matrícula de práctica: " + idMatriculaPractica, NORMAL));
        doc.add(new Paragraph("Fecha de emisión: " + fechaActual(), NORMAL));
        doc.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        agregarCabecera(tabla, "Fecha", "Actividad", "Horas", "Estado");

        PreparedStatement ps = null;
        ResultSet rs = null;
        double total = 0;
        try {
            ps = ConexionBD.getConnection().prepareStatement(
                    "SELECT FECHA_ACTIVIDAD, DESCRIPCION, HORAS, ESTADO FROM REGISTRO_ACTIVIDAD " +
                    "WHERE ID_MATRICULA_PRACTICA = ? ORDER BY FECHA_ACTIVIDAD");
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            while (rs.next()) {
                tabla.addCell(celda(rs.getString(1)));
                tabla.addCell(celda(rs.getString(2)));
                tabla.addCell(celda(String.valueOf(rs.getDouble(3))));
                tabla.addCell(celda(rs.getString(4)));
                total += rs.getDouble(3);
            }
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        doc.add(tabla);
        doc.add(new Paragraph("Total de horas registradas: " + total, SUBTITULO));
        agregarTrazabilidad(doc, "HORAS", String.valueOf(idMatriculaPractica));
        doc.close();
    }

    public void generarReporteEvaluacionFinal(int idMatriculaPractica, String rutaPdf) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(rutaPdf));
        doc.open();
        agregarEncabezado(doc, "Reporte de evaluación final");
        doc.add(new Paragraph("Matrícula de práctica: " + idMatriculaPractica, NORMAL));
        doc.add(new Paragraph("Fecha de emisión: " + fechaActual(), NORMAL));
        doc.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        agregarCabecera(tabla, "Evaluación", "Rúbrica", "Nota", "Observaciones");

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ConexionBD.getConnection().prepareStatement(
                    "SELECT E.ID_EVALUACION, NVL(R.NOMBRE,'Sin rúbrica') RUBRICA, " +
                    "NVL(E.NOTA_EVALUACION, NVL(E.PUNTAJE_OBTENIDO,0)) NOTA, E.OBSERVACIONES " +
                    "FROM EVALUACION E LEFT JOIN RUBRICA R ON E.ID_RUBRICA = R.ID_RUBRICA " +
                    "WHERE E.ID_MATRICULA_PRACTICA = ? ORDER BY E.FECHA_EVALUACION");
            ps.setInt(1, idMatriculaPractica);
            rs = ps.executeQuery();
            while (rs.next()) {
                tabla.addCell(celda(rs.getString(1)));
                tabla.addCell(celda(rs.getString(2)));
                tabla.addCell(celda(rs.getString(3)));
                tabla.addCell(celda(rs.getString(4)));
            }
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        doc.add(tabla);
        agregarTrazabilidad(doc, "EVALUACION", String.valueOf(idMatriculaPractica));
        doc.close();
    }

    public void generarCertificadoPractica(int idCertificadoPractica, String rutaPdf) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(rutaPdf));
        doc.open();
        agregarEncabezado(doc, "Certificado de práctica académica");

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ConexionBD.getConnection().prepareStatement(
                    "SELECT C.ID_CERTIFICADO_PRACTICA, U.NOMBRE || ' ' || U.APELLIDO AS ESTUDIANTE, " +
                    "P.NOMBRE AS PRACTICA, I.NOMBRE AS INSTITUCION, C.FECHA_EMISION, C.HASH_VERIFICACION " +
                    "FROM CERTIFICADO_PRACTICA C " +
                    "INNER JOIN MATRICULA_PRACTICA M ON C.ID_MATRICULA_PRACTICA = M.ID_MATRICULA_PRACTICA " +
                    "INNER JOIN USUARIO U ON M.ID_ESTUDIANTE = U.ID_USUARIO " +
                    "INNER JOIN PRACTICA P ON M.ID_PRACTICA = P.ID_PRACTICA " +
                    "LEFT JOIN INSTITUCION I ON M.ID_INSTITUCION = I.ID_INSTITUCION " +
                    "WHERE C.ID_CERTIFICADO_PRACTICA = ?");
            ps.setInt(1, idCertificadoPractica);
            rs = ps.executeQuery();
            if (rs.next()) {
                doc.add(new Paragraph("Se certifica que:", NORMAL));
                Paragraph estudiante = new Paragraph(rs.getString("ESTUDIANTE"), TITULO);
                estudiante.setAlignment(Element.ALIGN_CENTER);
                doc.add(estudiante);
                doc.add(new Paragraph("Completó satisfactoriamente la práctica: " + rs.getString("PRACTICA"), NORMAL));
                doc.add(new Paragraph("Institución receptora: " + rs.getString("INSTITUCION"), NORMAL));
                doc.add(new Paragraph("Fecha de emisión: " + rs.getString("FECHA_EMISION"), NORMAL));
                doc.add(new Paragraph("Firma digital simulada: DIRECCIÓN DE PROGRAMA / COORDINACIÓN DE PRÁCTICAS", SUBTITULO));
                doc.add(new Paragraph("Hash de verificación: " + rs.getString("HASH_VERIFICACION"), NORMAL));
            } else {
                doc.add(new Paragraph("No se encontró el certificado solicitado.", NORMAL));
            }
        } finally {
            Utilidades.cerrar(rs);
            Utilidades.cerrar(ps);
        }
        agregarTrazabilidad(doc, "CERTIFICADO", String.valueOf(idCertificadoPractica));
        doc.close();
    }

    private static void agregarEncabezado(Document doc, String titulo) throws Exception {
        Paragraph p = new Paragraph("Sistema de Gestión de Prácticas Académicas", TITULO);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
        Paragraph s = new Paragraph(titulo, SUBTITULO);
        s.setAlignment(Element.ALIGN_CENTER);
        doc.add(s);
        doc.add(new Paragraph(" "));
    }

    private static void agregarCabecera(PdfPTable tabla, String... textos) {
        for (String texto : textos) {
            PdfPCell cell = new PdfPCell(new Phrase(texto, SUBTITULO));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }
    }

    private static PdfPCell celda(String texto) {
        return new PdfPCell(new Phrase(texto == null ? "" : texto, NORMAL));
    }

    private static void agregarTrazabilidad(Document doc, String tipo, String id) throws Exception {
        String base = tipo + "|" + id + "|" + fechaActual();
        String hash = sha256(base);
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Trazabilidad", SUBTITULO));
        doc.add(new Paragraph("Código verificable / QR textual: " + base + "|" + hash, NORMAL));
    }

    public static String sha256(String texto) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    private static String fechaActual() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
