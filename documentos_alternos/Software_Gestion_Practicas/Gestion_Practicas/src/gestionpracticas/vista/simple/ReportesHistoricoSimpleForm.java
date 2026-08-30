package com.gestionpracticas.vista.simple;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.LinkedHashMap;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/** Panel administrativo de histórico: logs, reportes, revisiones, horas, comentarios y evaluaciones. */
public class ReportesHistoricoSimpleForm extends JFrame {
    private final Usuario usuario;
    private JComboBox<String> cmbVista;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblInfo;

    public ReportesHistoricoSimpleForm(Usuario usuario) { this.usuario = usuario; init(); cargarVista(); }

    private void init() {
        setTitle("Reportes e histórico del sistema"); setSize(1280, 720); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        JPanel root = new JPanel(new BorderLayout(12,12)); root.setBackground(new Color(245,247,250)); root.setBorder(new EmptyBorder(12,12,12,12));
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(new Color(33,97,140)); header.setBorder(new EmptyBorder(16,20,16,20));
        JLabel title = new JLabel("Reportes e histórico"); title.setFont(new Font("Segoe UI", Font.BOLD, 22)); title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Consulta y descarga PDF de matrículas, horas, actividades, evaluaciones, respuestas, comentarios y bitácora."); sub.setForeground(Color.WHITE);
        header.add(title, BorderLayout.NORTH); header.add(sub, BorderLayout.SOUTH);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); top.setBackground(Color.WHITE);
        cmbVista = new JComboBox<String>(new String[]{"Resumen general", "Matrículas y avance productivo", "Histórico de horas", "Actividades del estudiante", "Evaluaciones, respuestas y comentarios", "Bitácora LOG_ACTIVIDAD", "Historial de revisiones", "Reportes generados"}); cmbVista.setPreferredSize(new Dimension(315, 32));
        JButton btnCargar = boton("Cargar", new Color(52,152,219)); btnCargar.addActionListener(e -> cargarVista());
        JButton btnPdf = boton("Descargar PDF", new Color(39,174,96)); btnPdf.addActionListener(e -> descargarPdf());
        JButton btnPdfCom = boton("PDF comentarios", new Color(142,68,173)); btnPdfCom.addActionListener(e -> { cmbVista.setSelectedItem("Evaluaciones, respuestas y comentarios"); cargarVista(); descargarPdf(); });
        JButton btnEditar = boton("Modificar reporte", new Color(243,156,18)); btnEditar.addActionListener(e -> modificarReporteSeleccionado());
        JButton btnEliminar = boton("Eliminar reporte", new Color(192,57,43)); btnEliminar.addActionListener(e -> eliminarReporteSeleccionado());
        top.add(new JLabel("Vista:")); top.add(cmbVista); top.add(btnCargar); top.add(btnPdf); top.add(btnPdfCom); top.add(btnEditar); top.add(btnEliminar);
        modelo = new DefaultTableModel(); tabla = new JTable(modelo); tabla.setRowHeight(28); lblInfo = new JLabel(" "); lblInfo.setBorder(new EmptyBorder(8,8,8,8));
        JPanel center = new JPanel(new BorderLayout()); center.setBackground(Color.WHITE); center.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(8,8,8,8)));
        center.add(top, BorderLayout.NORTH); center.add(new JScrollPane(tabla), BorderLayout.CENTER); center.add(lblInfo, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH); root.add(center, BorderLayout.CENTER); setContentPane(root);
    }

    private JButton boton(String t, Color c) { JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }

    private void cargarVista() {
        String v = String.valueOf(cmbVista.getSelectedItem());
        if (v.indexOf("Resumen") >= 0) cargarResumen();
        else if (v.indexOf("Matrículas") >= 0) cargarMatriculasAvance();
        else if (v.indexOf("horas") >= 0) cargarHistoricoHoras();
        else if (v.indexOf("Actividades") >= 0) cargarActividades();
        else if (v.indexOf("Evaluaciones") >= 0) cargarEvaluaciones();
        else if (v.indexOf("LOG") >= 0) cargarLog();
        else if (v.indexOf("revisiones") >= 0) cargarRevisiones();
        else cargarReportes();
    }

    private void cargarResumen() {
        modelo.setDataVector(new Object[0][2], new String[]{"Indicador", "Valor"});
        modelo.addRow(new Object[]{"Estudiantes matriculados", DBHelper.contar("MATRICULA_PRACTICA")});
        modelo.addRow(new Object[]{"Horas reportadas", sumaHorasReportadas()});
        modelo.addRow(new Object[]{"Horas aprobadas", sumaHorasAprobadas()});
        modelo.addRow(new Object[]{"Actividades registradas", DBHelper.contar("REGISTRO_ACTIVIDAD")});
        modelo.addRow(new Object[]{"Evaluaciones publicadas", DBHelper.contar("EVALUACION")});
        modelo.addRow(new Object[]{"Preguntas publicadas", DBHelper.contar("PREGUNTA")});
        modelo.addRow(new Object[]{"Respuestas enviadas", DBHelper.contar("RESPUESTA")});
        modelo.addRow(new Object[]{"Logs de auditoría", DBHelper.contar("LOG_ACTIVIDAD")});
        lblInfo.setText("Resumen general | Datos leídos directamente de Oracle");
    }
    private double sumaHorasReportadas() { double a=0; if(DBHelper.existeTabla("HORAS_PRACTICA")) { String c=SimpleData.col("HORAS_PRACTICA","HORAS_REPORTADAS","HORAS"); if(c!=null) a += DBHelper.sumarWhere("HORAS_PRACTICA", c, "1=1"); } if(DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) { String c=SimpleData.col("REGISTRO_ACTIVIDAD","HORAS","HORAS_REPORTADAS"); if(c!=null) a += DBHelper.sumarWhere("REGISTRO_ACTIVIDAD", c, "1=1"); } return a; }
    private double sumaHorasAprobadas() { double a=0; if(DBHelper.existeTabla("HORAS_PRACTICA") && SimpleData.col("HORAS_PRACTICA","HORAS_APROBADAS")!=null) a += DBHelper.sumarWhere("HORAS_PRACTICA", "HORAS_APROBADAS", "1=1"); if(DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) { String h=SimpleData.col("REGISTRO_ACTIVIDAD","HORAS","HORAS_REPORTADAS"); if(h!=null) a += DBHelper.sumarWhere("REGISTRO_ACTIVIDAD", h, "UPPER(NVL(ESTADO,'PENDIENTE'))='APROBADO'"); } return a; }

    private void cargarMatriculasAvance() {
        String estCol = SimpleData.matriculaEstudianteCol(); if (estCol == null) { error("Matrículas", "No se encuentra columna de estudiante en MATRICULA_PRACTICA"); return; }
        String joins = " JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA ";
        if (SimpleData.has("MATRICULA_PRACTICA", "ID_GRUPO")) joins += " LEFT JOIN GRUPO G ON G.ID_GRUPO=M.ID_GRUPO "; else joins += " LEFT JOIN GRUPO G ON G.ID_PRACTICA=M.ID_PRACTICA ";
        String instJoinExpr = SimpleData.matriculaInstitucionExpr("M", "P"); if (instJoinExpr != null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION=" + instJoinExpr + " ";
        String instTxt = instJoinExpr == null ? SimpleData.lit("Sin institución") : SimpleData.institucionNombreExpr("I");
        String req = SimpleData.q("P", "PRACTICA", "HORAS_REQUERIDAS", "HORAS_REGLAMENTARIAS"); if(req==null) req="0";
        String hr = horasReportadasSub("M"); String ha = horasAprobadasSub("M");
        String prom = DBHelper.existeTabla("EVALUACION") ? "(SELECT NVL(ROUND(AVG(" + SimpleData.notaExpr("E") + "),2),0) FROM EVALUACION E WHERE E.ID_MATRICULA_PRACTICA=M.ID_MATRICULA_PRACTICA)" : "0";
        String sql = "SELECT " + SimpleData.usuarioNombreExpr("U") + ", " + SimpleData.practicaTituloExpr("P") + ", " + SimpleData.grupoNombreExpr("G") + ", " + instTxt + ", " + SimpleData.etapaMatriculaExpr("M") + ", " + SimpleData.estadoExpr("M", "MATRICULA_PRACTICA", "ACTIVO") + ", " + req + ", " + hr + ", " + ha + ", (" + req + "-" + ha + "), " + prom + " FROM MATRICULA_PRACTICA M " + joins + " ORDER BY " + SimpleData.usuarioNombreExpr("U");
        cargar("Matrículas y avance productivo", new String[]{"Estudiante", "Práctica", "Grupo", "Institución", "Etapa", "Estado", "Horas requeridas", "Reportadas", "Aprobadas", "Pendientes", "Promedio"}, sql);
    }
    private String horasReportadasSub(String aliasM) { String s="0"; if(DBHelper.existeTabla("HORAS_PRACTICA")) { String c=SimpleData.col("HORAS_PRACTICA","HORAS_REPORTADAS","HORAS"); String m=SimpleData.col("HORAS_PRACTICA","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(c!=null&&m!=null) s += "+(SELECT NVL(SUM(H."+c+"),0) FROM HORAS_PRACTICA H WHERE H."+m+"="+aliasM+".ID_MATRICULA_PRACTICA)"; } if(DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) { String c=SimpleData.col("REGISTRO_ACTIVIDAD","HORAS","HORAS_REPORTADAS"); String m=SimpleData.col("REGISTRO_ACTIVIDAD","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(c!=null&&m!=null) s += "+(SELECT NVL(SUM(R."+c+"),0) FROM REGISTRO_ACTIVIDAD R WHERE R."+m+"="+aliasM+".ID_MATRICULA_PRACTICA)"; } return "("+s+")"; }
    private String horasAprobadasSub(String aliasM) { String s="0"; if(DBHelper.existeTabla("HORAS_PRACTICA")) { String c=SimpleData.col("HORAS_PRACTICA","HORAS_APROBADAS"); String m=SimpleData.col("HORAS_PRACTICA","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(c!=null&&m!=null) s += "+(SELECT NVL(SUM(H."+c+"),0) FROM HORAS_PRACTICA H WHERE H."+m+"="+aliasM+".ID_MATRICULA_PRACTICA)"; } if(DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) { String c=SimpleData.col("REGISTRO_ACTIVIDAD","HORAS","HORAS_REPORTADAS"); String m=SimpleData.col("REGISTRO_ACTIVIDAD","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(c!=null&&m!=null) s += "+(SELECT NVL(SUM(R."+c+"),0) FROM REGISTRO_ACTIVIDAD R WHERE R."+m+"="+aliasM+".ID_MATRICULA_PRACTICA AND UPPER(NVL(R.ESTADO,'PENDIENTE'))='APROBADO')"; } return "("+s+")"; }

    private void cargarHistoricoHoras() { cargarActividades(); }

    private void cargarActividades() {
        String estCol = SimpleData.matriculaEstudianteCol(); if (estCol == null) { error("Actividades", "No se encuentra columna de estudiante en MATRICULA_PRACTICA"); return; }
        if (!DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) { error("Actividades", "La tabla REGISTRO_ACTIVIDAD no existe."); return; }
        String matCol = SimpleData.col("REGISTRO_ACTIVIDAD", "ID_MATRICULA_PRACTICA", "ID_MATRICULA"); if(matCol==null) { error("Actividades", "REGISTRO_ACTIVIDAD no tiene columna de matrícula."); return; }
        String fecha = SimpleData.q("R", "REGISTRO_ACTIVIDAD", "FECHA_ACTIVIDAD", "FECHA_REGISTRO", "FECHA"); if(fecha==null) fecha="SYSDATE";
        String horas = SimpleData.q("R", "REGISTRO_ACTIVIDAD", "HORAS", "HORAS_REPORTADAS"); if(horas==null) horas="0";
        String desc = SimpleData.q("R", "REGISTRO_ACTIVIDAD", "DESCRIPCION", "ACTIVIDAD"); if(desc==null) desc=SimpleData.lit("");
        String obs = SimpleData.q("R", "REGISTRO_ACTIVIDAD", "OBSERVACIONES", "OBSERVACION"); if(obs==null) obs=SimpleData.lit("");
        String instJoinExpr = SimpleData.matriculaInstitucionExpr("M", "P");
        String joins = " JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=R." + matCol + " JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA "; if(instJoinExpr!=null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION=" + instJoinExpr + " ";
        String inst = instJoinExpr==null ? SimpleData.lit("Sin institución") : SimpleData.institucionNombreExpr("I");
        String sql = "SELECT " + SimpleData.usuarioNombreExpr("U") + ", " + SimpleData.practicaTituloExpr("P") + ", " + inst + ", TO_CHAR(" + fecha + ",'YYYY-MM-DD'), " + horas + ", CASE WHEN UPPER(NVL(R.ESTADO,'PENDIENTE'))='APROBADO' THEN " + horas + " ELSE 0 END, " + SimpleData.estadoExpr("R", "REGISTRO_ACTIVIDAD", "PENDIENTE") + ", " + desc + ", " + obs + " FROM REGISTRO_ACTIVIDAD R " + joins + " ORDER BY " + fecha + " DESC";
        cargar("Actividades y horas del estudiante", new String[]{"Estudiante", "Práctica", "Institución", "Fecha", "Reportadas", "Aprobadas", "Estado", "Actividad", "Observación"}, sql);
    }

    private void cargarEvaluaciones() {
        String estCol = SimpleData.matriculaEstudianteCol(); if (estCol == null) { error("Evaluaciones", "No se encuentra columna de estudiante en MATRICULA_PRACTICA"); return; }
        String respJoin = DBHelper.existeTabla("RESPUESTA") ? " LEFT JOIN RESPUESTA R ON R.ID_PREGUNTA=Q.ID_PREGUNTA " : "";
        String pregunta = DBHelper.existeTabla("PREGUNTA") ? SimpleData.preguntaTextoExpr("Q") : SimpleData.lit("Sin pregunta");
        String respuesta = DBHelper.existeTabla("RESPUESTA") ? SimpleData.respuestaTextoExpr("R") : SimpleData.lit("");
        String retro = SimpleData.q("R", "RESPUESTA", "RETROALIMENTACION"); if(retro==null) retro=SimpleData.lit("");
        String fecha = SimpleData.has("EVALUACION", "FECHA_EVALUACION") ? "E.FECHA_EVALUACION" : "E.ID_EVALUACION";
        String obs = SimpleData.q("E", "EVALUACION", "OBSERVACIONES", "OBSERVACION"); if(obs==null) obs=SimpleData.lit("");
        String sql = "SELECT " + SimpleData.usuarioNombreExpr("U") + ", " + SimpleData.practicaTituloExpr("P") + ", " + pregunta + ", " + respuesta + ", " + SimpleData.estadoExpr("E", "EVALUACION", "PENDIENTE") + ", " + SimpleData.notaExpr("E") + ", " + obs + ", " + retro + " FROM EVALUACION E JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=E.ID_MATRICULA_PRACTICA JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA LEFT JOIN PREGUNTA Q ON Q.ID_EVALUACION=E.ID_EVALUACION " + respJoin + " ORDER BY " + fecha + " DESC" + (SimpleData.has("PREGUNTA", "ORDEN") ? ", Q.ORDEN" : "");
        cargar("Evaluaciones, respuestas y comentarios", new String[]{"Estudiante", "Práctica", "Pregunta", "Respuesta", "Estado evaluación", "Nota", "Comentario docente", "Retroalimentación respuesta"}, sql);
    }

    private void cargarLog() {
        if (!DBHelper.existeTabla("LOG_ACTIVIDAD")) { error("Bitácora", "La tabla LOG_ACTIVIDAD no existe."); return; }
        String fecha = SimpleData.q("L", "LOG_ACTIVIDAD", "FECHA_HORA", "FECHA_LOG", "FECHA"); if(fecha==null) fecha="SYSDATE";
        String tabla = SimpleData.q("L", "LOG_ACTIVIDAD", "TABLA_AFECTADA", "TABLA"); if(tabla==null) tabla=SimpleData.lit("");
        String accion = SimpleData.q("L", "LOG_ACTIVIDAD", "TIPO_OPERACION", "ACCION"); if(accion==null) accion=SimpleData.lit("");
        String modulo = SimpleData.q("L", "LOG_ACTIVIDAD", "MODULO"); if(modulo==null) modulo=SimpleData.lit("");
        String detalle = SimpleData.q("L", "LOG_ACTIVIDAD", "VALOR_NUEVO", "DETALLE", "DESCRIPCION"); if(detalle==null) detalle=SimpleData.lit("");
        String userJoin = SimpleData.has("LOG_ACTIVIDAD", "ID_USUARIO") ? " LEFT JOIN USUARIO U ON U.ID_USUARIO=L.ID_USUARIO " : "";
        String user = SimpleData.has("LOG_ACTIVIDAD", "ID_USUARIO") ? SimpleData.usuarioNombreExpr("U") : (SimpleData.q("L", "LOG_ACTIVIDAD", "USUARIO_BD")==null ? SimpleData.lit("-") : SimpleData.q("L", "LOG_ACTIVIDAD", "USUARIO_BD"));
        cargar("Bitácora de acciones", new String[]{"Fecha", "Usuario", "Tabla", "Operación", "Módulo", "Detalle"}, "SELECT TO_CHAR(" + fecha + ",'YYYY-MM-DD HH24:MI'), " + user + ", " + tabla + ", " + accion + ", " + modulo + ", " + detalle + " FROM LOG_ACTIVIDAD L " + userJoin + " ORDER BY " + fecha + " DESC");
    }

    private void cargarRevisiones() {
        if (!DBHelper.existeTabla("HISTORIAL_REVISION")) { error("Historial", "La tabla HISTORIAL_REVISION no existe."); return; }
        String estCol = SimpleData.matriculaEstudianteCol();
        String joins = estCol == null || !SimpleData.has("HISTORIAL_REVISION", "ID_MATRICULA_PRACTICA") ? "" : " LEFT JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=H.ID_MATRICULA_PRACTICA LEFT JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA ";
        String fecha = SimpleData.q("H", "HISTORIAL_REVISION", "FECHA_REVISION", "FECHA"); if(fecha==null) fecha="SYSDATE";
        String estudiante = joins.length() == 0 ? SimpleData.lit("-") : SimpleData.usuarioNombreExpr("U"); String practica = joins.length() == 0 ? SimpleData.lit("-") : SimpleData.practicaTituloExpr("P");
        String tipo = SimpleData.q("H", "HISTORIAL_REVISION", "TIPO_REVISION", "TIPO"); if(tipo==null) tipo=SimpleData.lit(""); String ant=SimpleData.q("H", "HISTORIAL_REVISION", "ESTADO_ANTERIOR"); if(ant==null) ant=SimpleData.lit(""); String nuevo=SimpleData.q("H", "HISTORIAL_REVISION", "ESTADO_NUEVO"); if(nuevo==null) nuevo=SimpleData.lit(""); String obs=SimpleData.q("H", "HISTORIAL_REVISION", "OBSERVACION", "OBSERVACIONES"); if(obs==null) obs=SimpleData.lit("");
        cargar("Historial de revisiones", new String[]{"Fecha", "Estudiante", "Práctica", "Tipo", "Antes", "Ahora", "Observación"}, "SELECT TO_CHAR(" + fecha + ",'YYYY-MM-DD HH24:MI'), " + estudiante + ", " + practica + ", " + tipo + ", " + ant + ", " + nuevo + ", " + obs + " FROM HISTORIAL_REVISION H " + joins + " ORDER BY " + fecha + " DESC");
    }

    private void cargarReportes() {
        if (!DBHelper.existeTabla("REPORTE")) { error("Reportes", "La tabla REPORTE no existe."); return; }
        String fecha = SimpleData.q("R", "REPORTE", "FECHA_GENERACION", "FECHA"); if(fecha==null) fecha="SYSDATE";
        String titulo = SimpleData.q("R", "REPORTE", "TITULO", "NOMBRE"); if(titulo==null) titulo=SimpleData.lit(""); String tipo=SimpleData.q("R", "REPORTE", "TIPO_REPORTE", "TIPO"); if(tipo==null) tipo=SimpleData.lit(""); String desc=SimpleData.q("R", "REPORTE", "DESCRIPCION", "COMENTARIOS"); if(desc==null) desc=SimpleData.lit(""); String estado=SimpleData.q("R", "REPORTE", "ESTADO"); if(estado==null) estado=SimpleData.lit(""); String ruta=SimpleData.q("R", "REPORTE", "RUTA_ARCHIVO", "ARCHIVO"); if(ruta==null) ruta=SimpleData.lit("");
        String userJoin = SimpleData.has("REPORTE", "ID_USUARIO_GENERA") ? " LEFT JOIN USUARIO U ON U.ID_USUARIO=R.ID_USUARIO_GENERA " : ""; String user = SimpleData.has("REPORTE", "ID_USUARIO_GENERA") ? SimpleData.usuarioNombreExpr("U") : SimpleData.lit("-");
        cargar("Reportes generados", new String[]{"ID", "Fecha", "Usuario", "Título", "Tipo", "Descripción/Comentario", "Estado", "Ruta"}, "SELECT R.ID_REPORTE, TO_CHAR(" + fecha + ",'YYYY-MM-DD HH24:MI'), " + user + ", " + titulo + ", " + tipo + ", " + desc + ", " + estado + ", " + ruta + " FROM REPORTE R " + userJoin + " ORDER BY " + fecha + " DESC"); ocultarIdSiReportes();
    }

    private void descargarPdf() {
        if (modelo.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Primero cargue una vista con datos."); return; }
        try {
            File dir = new File("reportes"); if(!dir.exists()) dir.mkdirs();
            String nombre = "reporte_" + String.valueOf(cmbVista.getSelectedItem()).replace(' ', '_').replace('/', '_') + "_" + System.currentTimeMillis() + ".pdf";
            File out = new File(dir, nombre);
            Document doc = new Document(); PdfWriter.getInstance(doc, new FileOutputStream(out)); doc.open();
            Paragraph titulo = new Paragraph("Sistema de Gestión de Prácticas - " + cmbVista.getSelectedItem(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)); titulo.setAlignment(Element.ALIGN_CENTER); doc.add(titulo);
            doc.add(new Paragraph("Generado por: " + usuario.getNombre() + " | Fecha: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date())));
            doc.add(new Paragraph("Trazabilidad: reporte generado desde Oracle con registros visibles en la pantalla.\n "));
            int visibles = 0; for(int c=0;c<modelo.getColumnCount();c++) { try { if(tabla.getColumnModel().getColumn(c).getMaxWidth()!=0) visibles++; } catch(Exception ex) { visibles++; } }
            if (visibles <= 0) visibles = modelo.getColumnCount();
            PdfPTable t = new PdfPTable(visibles); t.setWidthPercentage(100);
            for(int c=0;c<modelo.getColumnCount();c++) { if(tabla.getColumnModel().getColumn(c).getMaxWidth()==0) continue; PdfPCell cell=new PdfPCell(new Phrase(modelo.getColumnName(c), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8))); t.addCell(cell); }
            for(int r=0;r<modelo.getRowCount();r++) for(int c=0;c<modelo.getColumnCount();c++) { if(tabla.getColumnModel().getColumn(c).getMaxWidth()==0) continue; t.addCell(new Phrase(String.valueOf(modelo.getValueAt(r,c)==null?"":modelo.getValueAt(r,c)), FontFactory.getFont(FontFactory.HELVETICA, 7))); }
            doc.add(t); doc.close(); insertarReporte(nombre, out.getPath()); JOptionPane.showMessageDialog(this, "PDF generado: " + out.getAbsolutePath());
        } catch(Exception e) { JOptionPane.showMessageDialog(this, "No se pudo generar PDF: " + e.getMessage()); }
    }

    private void insertarReporte(String titulo, String ruta) {
        try { if(!DBHelper.existeTabla("REPORTE")) return; LinkedHashMap<String,Object> r=new LinkedHashMap<String,Object>(); String idCol = SimpleData.col("REPORTE", "ID_REPORTE", "ID_REPORTES"); if(idCol!=null) r.put(idCol, Integer.valueOf(DBHelper.siguienteId("REPORTE", idCol))); r.put("ID_USUARIO_GENERA", usuario.getIdUsuario()); r.put("TITULO", titulo); r.put("NOMBRE", titulo); r.put("DESCRIPCION", "Reporte PDF generado desde la vista: " + cmbVista.getSelectedItem()); r.put("COMENTARIOS", "Incluye trazabilidad de comentarios, respuestas y registros visibles."); r.put("TIPO_REPORTE", String.valueOf(cmbVista.getSelectedItem())); r.put("FECHA", new java.sql.Date(System.currentTimeMillis())); r.put("FECHA_GENERACION", new java.sql.Date(System.currentTimeMillis())); r.put("RUTA_ARCHIVO", ruta); r.put("ESTADO", "GENERADO"); DBHelper.insertar("REPORTE", r); } catch(Exception e) { }
    }

    private void modificarReporteSeleccionado() {
        if (String.valueOf(cmbVista.getSelectedItem()).indexOf("Reportes") < 0) { JOptionPane.showMessageDialog(this, "Esta acción modifica solo registros de la tabla REPORTE. Seleccione 'Reportes generados'."); return; }
        int row=tabla.getSelectedRow(); if(row<0) { JOptionPane.showMessageDialog(this,"Seleccione un reporte."); return; }
        int id=Integer.parseInt(String.valueOf(modelo.getValueAt(row,0))); JTextField titulo=new JTextField(String.valueOf(modelo.getValueAt(row,3))); JTextArea desc=new JTextArea(String.valueOf(modelo.getValueAt(row,5)),5,40); JPanel p=new JPanel(new BorderLayout(5,5)); p.add(titulo,BorderLayout.NORTH); p.add(new JScrollPane(desc),BorderLayout.CENTER);
        if(JOptionPane.showConfirmDialog(this,p,"Modificar título y comentario",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) { LinkedHashMap<String,Object>d=new LinkedHashMap<String,Object>(); d.put("TITULO", titulo.getText()); d.put("NOMBRE", titulo.getText()); d.put("DESCRIPCION", desc.getText()); d.put("COMENTARIOS", desc.getText()); if(DBHelper.actualizar("REPORTE","ID_REPORTE",Integer.valueOf(id),d)) cargarReportes(); else JOptionPane.showMessageDialog(this,"No se pudo modificar."); }
    }
    private void eliminarReporteSeleccionado() { if(String.valueOf(cmbVista.getSelectedItem()).indexOf("Reportes")<0){JOptionPane.showMessageDialog(this,"Seleccione 'Reportes generados'.");return;} int row=tabla.getSelectedRow(); if(row<0){JOptionPane.showMessageDialog(this,"Seleccione un reporte.");return;} int id=Integer.parseInt(String.valueOf(modelo.getValueAt(row,0))); if(JOptionPane.showConfirmDialog(this,"¿Eliminar/inactivar reporte?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){ if(DBHelper.eliminarLogico("REPORTE","ID_REPORTE",Integer.valueOf(id))) cargarReportes(); else JOptionPane.showMessageDialog(this,"No se pudo eliminar."); } }

    private void error(String nombre, String msg) { modelo.setDataVector(new Object[0][1], new String[]{nombre}); lblInfo.setText(msg); }
    private void cargar(String nombre, String[] cols, String sql) { modelo.setDataVector(new Object[0][cols.length], cols); Statement st = null; ResultSet rs = null; int count = 0; try { st = ConexionBD.getConnection().createStatement(); rs = st.executeQuery(sql); int n = cols.length; while (rs.next()) { Object[] row = new Object[n]; for (int i=0; i<n; i++) row[i] = rs.getObject(i+1); modelo.addRow(row); count++; } lblInfo.setText(nombre + " | Registros encontrados: " + count); } catch (Exception e) { lblInfo.setText("No se pudo cargar " + nombre + ": " + e.getMessage()); System.err.println("Reporte SQL: " + sql); } finally { Utilidades.cerrar(rs); try { if (st != null) st.close(); } catch (Exception e) { } } }
    private void ocultarIdSiReportes() { try { if(String.valueOf(cmbVista.getSelectedItem()).indexOf("Reportes")>=0) { tabla.getColumnModel().getColumn(0).setMinWidth(0); tabla.getColumnModel().getColumn(0).setMaxWidth(0); tabla.getColumnModel().getColumn(0).setPreferredWidth(0); } } catch(Exception e){} }
}
