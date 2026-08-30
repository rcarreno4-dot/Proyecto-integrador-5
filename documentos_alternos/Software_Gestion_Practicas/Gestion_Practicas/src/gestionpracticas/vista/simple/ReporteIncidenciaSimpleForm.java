package com.gestionpracticas.vista.simple;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
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

/**
 * Reportes e incidencias por rol.
 * Cualquier usuario crea una incidencia. Director y Coordinador pueden responder y descargar PDF.
 */
public class ReporteIncidenciaSimpleForm extends JFrame {
    private final Usuario usuario;
    private JTextField txtTitulo;
    private JTextArea txtDetalle, txtRespuesta;
    private JComboBox<String> cmbTipo;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblInfo;
    private JButton btnResponder, btnPdf;

    public ReporteIncidenciaSimpleForm(Usuario usuario) {
        this.usuario = usuario;
        asegurarColumnasReporte();
        init();
        cargarTabla();
    }

    private boolean esGestor() {
        String r = usuario.getRol() == null ? "" : usuario.getRol().toUpperCase();
        return r.indexOf("DIRECTOR") >= 0 || r.indexOf("COORDINADOR") >= 0 || r.indexOf("ADMIN") >= 0;
    }

    private void init() {
        setTitle("Reportes e incidencias");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);

        JPanel root = new JPanel(new BorderLayout(12,12));
        root.setBackground(new Color(245,247,250));
        root.setBorder(new EmptyBorder(12,12,12,12));
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33,97,140));
        header.setBorder(new EmptyBorder(16,20,16,20));
        JLabel title = new JLabel(esGestor() ? "Gestión de reportes e incidencias" : "Mis reportes e incidencias");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22)); title.setForeground(Color.WHITE);
        JLabel sub = new JLabel(esGestor() ? "Director y coordinador responden, descargan y cierran solicitudes." : "Registra tu incidencia y consulta aquí la respuesta cuando sea atendida.");
        sub.setForeground(Color.WHITE); header.add(title, BorderLayout.NORTH); header.add(sub, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(15,15,15,15)));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(6,6,6,6); gbc.gridx=0; gbc.weightx=1;
        cmbTipo = new JComboBox<String>(new String[]{"INCIDENCIA", "SOLICITUD", "REPORTE DE AVANCE", "SOPORTE", "OBSERVACION"});
        txtTitulo = new JTextField();
        txtDetalle = new JTextArea(6,28); txtDetalle.setLineWrap(true); txtDetalle.setWrapStyleWord(true);
        txtRespuesta = new JTextArea(6,28); txtRespuesta.setLineWrap(true); txtRespuesta.setWrapStyleWord(true); txtRespuesta.setEditable(esGestor());
        addCampo(form, gbc, 0, "Tipo", cmbTipo); addCampo(form, gbc, 2, "Título", txtTitulo);
        gbc.gridy=4; form.add(label("Detalle del reporte/incidencia"), gbc); gbc.gridy=5; form.add(new JScrollPane(txtDetalle), gbc);
        gbc.gridy=6; form.add(label("Respuesta del director/coordinador"), gbc); gbc.gridy=7; form.add(new JScrollPane(txtRespuesta), gbc);
        JButton btnCrear = boton("Registrar reporte/incidencia", new Color(39,174,96)); btnCrear.addActionListener(e -> crearReporte());
        btnResponder = boton("Responder seleccionada", new Color(52,152,219)); btnResponder.addActionListener(e -> responderSeleccionada());
        btnPdf = boton("Descargar PDF seleccionado", new Color(142,68,173)); btnPdf.addActionListener(e -> descargarPdfSeleccionado());
        JButton btnRefrescar = boton("Refrescar", new Color(52,73,94)); btnRefrescar.addActionListener(e -> cargarTabla());
        JPanel botones = new JPanel(new GridLayout(esGestor()?4:2,1,8,8)); botones.setBackground(Color.WHITE); botones.add(btnCrear); if(esGestor()){botones.add(btnResponder); botones.add(btnPdf);} botones.add(btnRefrescar);
        gbc.gridy=8; form.add(botones, gbc);
        lblInfo = new JLabel(" "); gbc.gridy=9; form.add(lblInfo, gbc);

        modelo = new DefaultTableModel(new String[]{"ID", "Fecha", "Usuario", "Tipo", "Título", "Estado", "Detalle", "Respuesta"}, 0) { public boolean isCellEditable(int r, int c){ return false; } };
        tabla = new JTable(modelo); tabla.setRowHeight(28); tabla.setDefaultRenderer(Object.class, new EstadoRenderer());
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
        JPanel panelTabla = new JPanel(new BorderLayout()); panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(10,10,10,10)));
        panelTabla.add(new JLabel(esGestor()?"Todas las incidencias registradas":"Mis incidencias y respuestas"), BorderLayout.NORTH);
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, panelTabla); split.setDividerLocation(395); split.setResizeWeight(0.30);
        root.add(header, BorderLayout.NORTH); root.add(split, BorderLayout.CENTER); setContentPane(root);
    }

    private JLabel label(String t){ JLabel l=new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 12)); return l; }
    private JButton boton(String t, Color c){ JButton b=new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }
    private void addCampo(JPanel p, GridBagConstraints gbc, int y, String t, JComponent c){ gbc.gridy=y; p.add(label(t),gbc); gbc.gridy=y+1; p.add(c,gbc); }

    private void crearReporte() {
        if (!DBHelper.existeTabla("REPORTE")) { msg("No existe la tabla REPORTE. Ejecute el patch SQL incluido en /sql."); return; }
        String titulo = SimpleData.nvl(txtTitulo.getText(), "Reporte sin título");
        String detalle = SimpleData.nvl(txtDetalle.getText(), "Sin detalle");
        try {
            LinkedHashMap<String,Object> r = new LinkedHashMap<String,Object>();
            String idCol = SimpleData.col("REPORTE", "ID_REPORTE", "ID_REPORTES");
            if (idCol != null) r.put(idCol, Integer.valueOf(DBHelper.siguienteId("REPORTE", idCol)));
            r.put("ID_USUARIO_GENERA", usuario.getIdUsuario()); r.put("ID_USUARIO", usuario.getIdUsuario());
            r.put("TIPO_REPORTE", cmbTipo.getSelectedItem()); r.put("TIPO", cmbTipo.getSelectedItem());
            r.put("TITULO", titulo); r.put("NOMBRE", titulo);
            r.put("DESCRIPCION", detalle); r.put("COMENTARIOS", detalle); r.put("DETALLE", detalle);
            r.put("RESPUESTA", ""); r.put("ESTADO", "PENDIENTE");
            r.put("FECHA_GENERACION", new java.sql.Date(System.currentTimeMillis())); r.put("FECHA", new java.sql.Date(System.currentTimeMillis()));
            if (DBHelper.insertar("REPORTE", r)) { lblInfo.setText("Reporte registrado. Estado PENDIENTE (naranja) hasta recibir respuesta."); txtTitulo.setText(""); txtDetalle.setText(""); cargarTabla(); }
            else msg("No se pudo registrar. Revise que el patch de reportes esté ejecutado y que no existan restricciones de ID.");
        } catch(Exception e) { msg("Error registrando reporte: " + e.getMessage()); }
    }

    private void responderSeleccionada() {
        if (!esGestor()) return;
        int row = tabla.getSelectedRow(); if (row < 0) { msg("Seleccione una incidencia."); return; }
        String respuesta = SimpleData.nvl(txtRespuesta.getText(), "Solicitud revisada y atendida.");
        int id = Integer.parseInt(String.valueOf(modelo.getValueAt(row,0)));
        String idCol = SimpleData.col("REPORTE", "ID_REPORTE", "ID_REPORTES");
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put("RESPUESTA", respuesta); d.put("COMENTARIO_RESPUESTA", respuesta); d.put("ESTADO", "RESPONDIDO");
        d.put("ID_RESPONSABLE", usuario.getIdUsuario()); d.put("ID_USUARIO_RESPONDE", usuario.getIdUsuario());
        d.put("FECHA_RESPUESTA", new java.sql.Date(System.currentTimeMillis()));
        if (idCol != null && DBHelper.actualizar("REPORTE", idCol, Integer.valueOf(id), d)) { lblInfo.setText("Incidencia respondida. El usuario la verá en verde como RESPONDIDO."); cargarTabla(); }
        else msg("No se pudo responder.");
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        if (!DBHelper.existeTabla("REPORTE")) { lblInfo.setText("No existe REPORTE. Ejecute sql/99_patch_presentacion_v14.sql"); return; }
        String idCol = SimpleData.col("REPORTE", "ID_REPORTE", "ID_REPORTES"); if (idCol == null) { lblInfo.setText("REPORTE no tiene ID reconocible."); return; }
        String fecha = SimpleData.q("R", "REPORTE", "FECHA_GENERACION", "FECHA"); if(fecha==null) fecha="SYSDATE";
        String tipo = SimpleData.q("R", "REPORTE", "TIPO_REPORTE", "TIPO"); if(tipo==null) tipo=SimpleData.lit("REPORTE");
        String titulo = SimpleData.q("R", "REPORTE", "TITULO", "NOMBRE"); if(titulo==null) titulo=SimpleData.lit("");
        String detalle = SimpleData.q("R", "REPORTE", "DESCRIPCION", "COMENTARIOS", "DETALLE"); if(detalle==null) detalle=SimpleData.lit("");
        String resp = SimpleData.q("R", "REPORTE", "RESPUESTA", "COMENTARIO_RESPUESTA"); if(resp==null) resp=SimpleData.lit("");
        String estado = SimpleData.q("R", "REPORTE", "ESTADO"); if(estado==null) estado=SimpleData.lit("PENDIENTE");
        String userCol = SimpleData.col("REPORTE", "ID_USUARIO_GENERA", "ID_USUARIO");
        String userJoin = userCol == null ? "" : " LEFT JOIN USUARIO U ON U.ID_USUARIO=R." + userCol + " ";
        String user = userCol == null ? SimpleData.lit("-") : SimpleData.usuarioNombreExpr("U");
        String where = " WHERE 1=1 "; if (!esGestor() && userCol != null) where += " AND R." + userCol + "=" + usuario.getIdUsuario();
        String sql = "SELECT R."+idCol+", TO_CHAR("+fecha+",'YYYY-MM-DD HH24:MI'), "+user+", "+tipo+", "+titulo+", "+estado+", "+detalle+", "+resp+" FROM REPORTE R "+userJoin+where+" ORDER BY "+fecha+" DESC";
        Statement st=null; ResultSet rs=null; int n=0;
        try { st=ConexionBD.getConnection().createStatement(); rs=st.executeQuery(sql); while(rs.next()){ modelo.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)}); n++; } ocultarId(); lblInfo.setText("Registros encontrados: "+n+". Pendiente=naranja, Respondido=verde."); }
        catch(Exception e){ lblInfo.setText("No se pudo cargar: "+e.getMessage()); System.err.println("Incidencias SQL: "+sql); }
        finally { Utilidades.cerrar(rs); try{ if(st!=null)st.close(); }catch(Exception ex){} }
    }

    private void cargarSeleccion() {
        int row=tabla.getSelectedRow(); if(row<0) return;
        txtTitulo.setText(String.valueOf(modelo.getValueAt(row,4)==null?"":modelo.getValueAt(row,4)));
        txtDetalle.setText(String.valueOf(modelo.getValueAt(row,6)==null?"":modelo.getValueAt(row,6)));
        txtRespuesta.setText(String.valueOf(modelo.getValueAt(row,7)==null?"":modelo.getValueAt(row,7)));
    }

    private void descargarPdfSeleccionado() {
        if (!esGestor()) { msg("Solo director o coordinador pueden descargar reportes."); return; }
        int row=tabla.getSelectedRow(); if(row<0){msg("Seleccione un reporte."); return;}
        try {
            File dir=new File("reportes"); if(!dir.exists()) dir.mkdirs();
            File out=new File(dir,"incidencia_"+modelo.getValueAt(row,0)+"_"+System.currentTimeMillis()+".pdf");
            Document doc=new Document(); PdfWriter.getInstance(doc,new FileOutputStream(out)); doc.open();
            Paragraph title=new Paragraph("Reporte / Incidencia - Sistema de Gestión de Prácticas", FontFactory.getFont(FontFactory.HELVETICA_BOLD,16)); title.setAlignment(Element.ALIGN_CENTER); doc.add(title);
            doc.add(new Paragraph("Generado por: "+usuario.getNombre()+" | Fecha: "+new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date())));
            doc.add(new Paragraph(" "));
            PdfPTable t=new PdfPTable(2); t.setWidthPercentage(100);
            for(int c=1;c<modelo.getColumnCount();c++){ t.addCell(new PdfPCell(new Phrase(modelo.getColumnName(c), FontFactory.getFont(FontFactory.HELVETICA_BOLD,9)))); t.addCell(new Phrase(String.valueOf(modelo.getValueAt(row,c)==null?"":modelo.getValueAt(row,c)), FontFactory.getFont(FontFactory.HELVETICA,9))); }
            doc.add(t); doc.close(); msg("PDF generado: "+out.getAbsolutePath());
        } catch(Exception e){ msg("No se pudo generar PDF: "+e.getMessage()); }
    }

    private void asegurarColumnasReporte() {
        if (!DBHelper.existeTabla("REPORTE")) return;
        addCol("ID_USUARIO_GENERA NUMBER"); addCol("TIPO_REPORTE VARCHAR2(80)"); addCol("TITULO VARCHAR2(200)"); addCol("DESCRIPCION VARCHAR2(1000)"); addCol("RESPUESTA VARCHAR2(1000)"); addCol("ID_RESPONSABLE NUMBER"); addCol("FECHA_RESPUESTA DATE"); addCol("ESTADO VARCHAR2(30)");
    }
    private void addCol(String def) {
        String col = def.split(" ")[0].toUpperCase(); if (SimpleData.has("REPORTE", col)) return;
        Statement st=null; try { st=ConexionBD.getConnection().createStatement(); st.executeUpdate("ALTER TABLE REPORTE ADD ("+def+")"); }
        catch(Exception e) { }
        finally { DBHelper.refrescarColumnas("REPORTE"); try{ if(st!=null)st.close(); } catch(Exception e){} }
    }

    private void ocultarId(){ try{ tabla.getColumnModel().getColumn(0).setMinWidth(0); tabla.getColumnModel().getColumn(0).setMaxWidth(0); tabla.getColumnModel().getColumn(0).setPreferredWidth(0);}catch(Exception e){} }
    private void msg(String m){ JOptionPane.showMessageDialog(this,m); }

    private class EstadoRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
            if(!isSelected){ String e=""; try{e=String.valueOf(table.getModel().getValueAt(row,5)).toUpperCase();}catch(Exception ex){} if(e.indexOf("RESP")>=0||e.indexOf("CERR")>=0) c.setBackground(new Color(213,245,227)); else if(e.indexOf("PEND")>=0) c.setBackground(new Color(252,243,207)); else c.setBackground(Color.WHITE); }
            return c;
        }
    }
}
