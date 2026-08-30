package com.gestionpracticas.vista.simple;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.LinkedHashMap;

/** Pantalla simple para que el estudiante vea estado, responda y consulte comentarios del docente. */
public class CuestionarioEstudianteSimpleForm extends JFrame {
    private final Usuario usuario;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextArea txtRespuesta;
    private JLabel lblPregunta, lblEstado;

    public CuestionarioEstudianteSimpleForm(Usuario usuario) { this.usuario = usuario; init(); cargarPreguntas(); }

    private void init() {
        setTitle("Mis cuestionarios y evaluaciones"); setSize(1250, 720); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        JPanel root = new JPanel(new BorderLayout(12,12)); root.setBackground(new Color(245,247,250)); root.setBorder(new EmptyBorder(12,12,12,12));
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(new Color(33,97,140)); header.setBorder(new EmptyBorder(16,20,16,20));
        JLabel title = new JLabel("Cuestionario del estudiante"); title.setFont(new Font("Segoe UI", Font.BOLD, 22)); title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Revisa si está pendiente o finalizada, responde las preguntas y consulta el comentario del docente."); sub.setForeground(Color.WHITE);
        header.add(title, BorderLayout.NORTH); header.add(sub, BorderLayout.SOUTH);
        modelo = new DefaultTableModel(new String[]{"ID", "Práctica", "Pregunta", "Evaluación", "Respuesta", "Tiempo", "Nota", "Comentario docente"}, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modelo); tabla.setRowHeight(30); tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
        JPanel panelResp = new JPanel(new BorderLayout(8,8)); panelResp.setBackground(Color.WHITE);
        panelResp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(15,15,15,15)));
        lblPregunta = new JLabel("Seleccione una pregunta"); lblPregunta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtRespuesta = new JTextArea(10, 35); txtRespuesta.setLineWrap(true); txtRespuesta.setWrapStyleWord(true);
        JButton btnEnviar = boton("Enviar / actualizar respuesta", new Color(39,174,96)); btnEnviar.addActionListener(e -> responder());
        JButton btnDetalle = boton("Ver comentario docente", new Color(52,152,219)); btnDetalle.addActionListener(e -> verComentario());
        JButton btnRefrescar = boton("Refrescar", new Color(52,73,94)); btnRefrescar.addActionListener(e -> cargarPreguntas());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT)); botones.setBackground(Color.WHITE); botones.add(btnRefrescar); botones.add(btnDetalle); botones.add(btnEnviar);
        lblEstado = new JLabel(" ");
        panelResp.add(lblPregunta, BorderLayout.NORTH); panelResp.add(new JScrollPane(txtRespuesta), BorderLayout.CENTER); panelResp.add(botones, BorderLayout.SOUTH);
        JPanel panelTabla = new JPanel(new BorderLayout()); panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(10,10,10,10)));
        panelTabla.add(new JLabel("Preguntas, estado, nota y comentario"), BorderLayout.NORTH); panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER); panelTabla.add(lblEstado, BorderLayout.SOUTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelTabla, panelResp); split.setDividerLocation(760); split.setResizeWeight(0.60);
        root.add(header, BorderLayout.NORTH); root.add(split, BorderLayout.CENTER); setContentPane(root);
    }

    private JButton boton(String t, Color c) { JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }

    private void cargarPreguntas() {
        modelo.setRowCount(0);
        String estCol = SimpleData.matriculaEstudianteCol(); if (estCol == null) { lblEstado.setText("No hay columna de estudiante en matrícula."); return; }
        if (!DBHelper.existeTabla("EVALUACION") || !DBHelper.existeTabla("PREGUNTA")) { lblEstado.setText("El docente aún no ha publicado preguntas."); return; }
        String respJoin = "";
        if (SimpleData.has("RESPUESTA", "ID_USUARIO") && SimpleData.has("RESPUESTA", "ID_ESTUDIANTE")) respJoin = " AND (R.ID_USUARIO=? OR R.ID_ESTUDIANTE=?)";
        else if (SimpleData.has("RESPUESTA", "ID_USUARIO")) respJoin = " AND R.ID_USUARIO=?";
        else if (SimpleData.has("RESPUESTA", "ID_ESTUDIANTE")) respJoin = " AND R.ID_ESTUDIANTE=?";
        String estadoResp = SimpleData.has("RESPUESTA", "ESTADO") ? "NVL(R.ESTADO,'PENDIENTE')" : "CASE WHEN R.ID_RESPUESTA IS NULL THEN 'PENDIENTE' ELSE 'ENVIADA' END";
        String obs = SimpleData.q("E", "EVALUACION", "OBSERVACIONES", "OBSERVACION"); if (obs == null) obs = SimpleData.lit("");
        String tiempo = "CASE WHEN UPPER(NVL(E.ESTADO,'PENDIENTE')) IN ('FINALIZADA','FINALIZADO','REALIZADA','REALIZADO') THEN 'FINALIZÓ' WHEN R.ID_RESPUESTA IS NULL THEN 'PENDIENTE DE REALIZAR' ELSE 'RESPONDIDA - EN REVISIÓN' END";
        String sql = "SELECT Q.ID_PREGUNTA, " + SimpleData.practicaTituloExpr("P") + " PRACTICA, " + SimpleData.preguntaTextoExpr("Q") + " PREGUNTA, " + SimpleData.estadoExpr("E", "EVALUACION", "PENDIENTE") + " EST_EVAL, " + estadoResp + " EST_RESP, " + tiempo + " TIEMPO, " + SimpleData.notaExpr("E") + " NOTA, " + obs + " OBS " +
                "FROM MATRICULA_PRACTICA M JOIN EVALUACION E ON E.ID_MATRICULA_PRACTICA=M.ID_MATRICULA_PRACTICA JOIN PREGUNTA Q ON Q.ID_EVALUACION=E.ID_EVALUACION LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA LEFT JOIN RESPUESTA R ON R.ID_PREGUNTA=Q.ID_PREGUNTA" + respJoin +
                " WHERE M." + estCol + "=?" + (SimpleData.has("PREGUNTA", "ESTADO") ? " AND NVL(UPPER(Q.ESTADO),'ACTIVO')='ACTIVO'" : "") + (SimpleData.has("EVALUACION", "ESTADO") ? " AND NVL(UPPER(E.ESTADO),'PENDIENTE') <> 'INACTIVO'" : "") + " ORDER BY CASE WHEN R.ID_RESPUESTA IS NULL THEN 0 ELSE 1 END, E.ID_EVALUACION DESC" + (SimpleData.has("PREGUNTA", "ORDEN") ? ", Q.ORDEN" : "");
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            ps = ConexionBD.getConnection().prepareStatement(sql); int idx = 1;
            if (SimpleData.has("RESPUESTA", "ID_USUARIO") && SimpleData.has("RESPUESTA", "ID_ESTUDIANTE")) { ps.setInt(idx++, usuario.getIdUsuario()); ps.setInt(idx++, usuario.getIdUsuario()); }
            else if (SimpleData.has("RESPUESTA", "ID_USUARIO") || SimpleData.has("RESPUESTA", "ID_ESTUDIANTE")) ps.setInt(idx++, usuario.getIdUsuario());
            ps.setInt(idx++, usuario.getIdUsuario());
            rs = ps.executeQuery(); while (rs.next()) modelo.addRow(new Object[]{rs.getInt("ID_PREGUNTA"), rs.getString("PRACTICA"), rs.getString("PREGUNTA"), rs.getString("EST_EVAL"), rs.getString("EST_RESP"), rs.getString("TIEMPO"), rs.getObject("NOTA"), rs.getString("OBS")});
            ocultarId(); lblEstado.setText(modelo.getRowCount() == 0 ? "No hay preguntas pendientes. Verifica matrícula y publicación docente." : "Preguntas cargadas: " + modelo.getRowCount());
        } catch (Exception e) { msg("No se pudieron cargar preguntas: " + e.getMessage()); }
        finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }

    private void cargarSeleccion() { int row = tabla.getSelectedRow(); if (row < 0) return; lblPregunta.setText(String.valueOf(modelo.getValueAt(row, 2))); txtRespuesta.setText(cargarRespuesta(((Number)modelo.getValueAt(row, 0)).intValue())); }

    private String cargarRespuesta(int idPregunta) {
        if (!DBHelper.existeTabla("RESPUESTA")) return "";
        String filtro = filtroRespuestaEstudiante("WHERE ID_PREGUNTA=?");
        PreparedStatement ps = null; ResultSet rs = null;
        try { ps = ConexionBD.getConnection().prepareStatement("SELECT " + SimpleData.respuestaTextoExpr("R") + " FROM RESPUESTA R " + filtro + ordenFechaRespuesta()); int i=1; ps.setInt(i++, idPregunta); i=setParamsEstudiante(ps, i); rs = ps.executeQuery(); if (rs.next()) return rs.getString(1); }
        catch (Exception e) { }
        finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
        return "";
    }

    private void responder() {
        int row = tabla.getSelectedRow(); if (row < 0) { msg("Seleccione una pregunta."); return; }
        String evalEstado = String.valueOf(modelo.getValueAt(row, 3)).toUpperCase();
        if (evalEstado.indexOf("FINAL") >= 0) { msg("Esta evaluación ya fue finalizada. Puedes ver la nota y el comentario, pero no modificar la respuesta."); return; }
        int idPregunta = ((Number)modelo.getValueAt(row, 0)).intValue(); String resp = txtRespuesta.getText() == null ? "" : txtRespuesta.getText().trim(); if (resp.length() == 0) { msg("Escriba la respuesta antes de enviar."); return; }
        try { Integer idResp = buscarRespuesta(idPregunta); if (idResp == null) insertarRespuesta(idPregunta, resp); else actualizarRespuesta(idResp.intValue(), resp); marcarEvaluacionRealizada(idPregunta); SimpleData.insertarLog(usuario.getIdUsuario(), "RESPUESTA", "INSERT/UPDATE", "Respuesta enviada por estudiante", "ESTUDIANTE"); lblEstado.setText("Respuesta guardada correctamente. Queda pendiente de revisión docente."); cargarPreguntas(); }
        catch (Exception ex) { msg("No se pudo guardar la respuesta: " + ex.getMessage()); }
    }

    private void marcarEvaluacionRealizada(int idPregunta) {
        PreparedStatement ps=null; ResultSet rs=null;
        try { ps=ConexionBD.getConnection().prepareStatement("SELECT ID_EVALUACION FROM PREGUNTA WHERE ID_PREGUNTA=?"); ps.setInt(1,idPregunta); rs=ps.executeQuery(); if(rs.next()) { LinkedHashMap<String,Object> d=new LinkedHashMap<String,Object>(); d.put("ESTADO", "REALIZADA"); DBHelper.actualizar("EVALUACION", "ID_EVALUACION", Integer.valueOf(rs.getInt(1)), d); } } catch(Exception e) { }
        finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }

    private void verComentario() {
        int row = tabla.getSelectedRow(); if (row < 0) { msg("Seleccione una pregunta."); return; }
        String texto = "Estado evaluación: " + modelo.getValueAt(row, 3) + "\nEstado respuesta: " + modelo.getValueAt(row, 4) + "\nTiempo/avance: " + modelo.getValueAt(row, 5) + "\nNota: " + modelo.getValueAt(row, 6) + "\n\nComentario docente:\n" + (modelo.getValueAt(row, 7) == null ? "Sin comentario docente aún." : modelo.getValueAt(row, 7));
        JTextArea a = new JTextArea(texto, 14, 60); a.setLineWrap(true); a.setWrapStyleWord(true); a.setEditable(false); JOptionPane.showMessageDialog(this, new JScrollPane(a), "Comentario y estado de evaluación", JOptionPane.INFORMATION_MESSAGE);
    }

    private String filtroRespuestaEstudiante(String prefix) {
        String f = prefix;
        if (SimpleData.has("RESPUESTA", "ID_USUARIO") && SimpleData.has("RESPUESTA", "ID_ESTUDIANTE")) f += " AND (ID_USUARIO=? OR ID_ESTUDIANTE=?)";
        else if (SimpleData.has("RESPUESTA", "ID_USUARIO")) f += " AND ID_USUARIO=?";
        else if (SimpleData.has("RESPUESTA", "ID_ESTUDIANTE")) f += " AND ID_ESTUDIANTE=?";
        return f;
    }
    private int setParamsEstudiante(PreparedStatement ps, int i) throws SQLException { if (SimpleData.has("RESPUESTA", "ID_USUARIO") && SimpleData.has("RESPUESTA", "ID_ESTUDIANTE")) { ps.setInt(i++, usuario.getIdUsuario()); ps.setInt(i++, usuario.getIdUsuario()); } else if (SimpleData.has("RESPUESTA", "ID_USUARIO") || SimpleData.has("RESPUESTA", "ID_ESTUDIANTE")) ps.setInt(i++, usuario.getIdUsuario()); return i; }
    private String ordenFechaRespuesta() { return SimpleData.has("RESPUESTA", "FECHA_RESPUESTA") ? " ORDER BY FECHA_RESPUESTA DESC" : " ORDER BY ID_RESPUESTA DESC"; }

    private Integer buscarRespuesta(int idPregunta) throws SQLException { PreparedStatement ps = null; ResultSet rs = null; try { ps = ConexionBD.getConnection().prepareStatement("SELECT ID_RESPUESTA FROM RESPUESTA " + filtroRespuestaEstudiante("WHERE ID_PREGUNTA=?")); int i=1; ps.setInt(i++, idPregunta); setParamsEstudiante(ps, i); rs = ps.executeQuery(); return rs.next() ? Integer.valueOf(rs.getInt(1)) : null; } finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); } }

    private void insertarRespuesta(int idPregunta, String resp) throws SQLException {
        LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>(); datos.put("ID_RESPUESTA", SimpleData.nextId("RESPUESTA", "ID_RESPUESTA", "SEQ_RESPUESTA")); datos.put("ID_PREGUNTA", idPregunta); datos.put("ID_USUARIO", usuario.getIdUsuario()); datos.put("ID_ESTUDIANTE", usuario.getIdUsuario()); datos.put("CONTENIDO_RESPUESTA", resp); datos.put("RESPUESTA", resp); datos.put("FECHA_RESPUESTA", new java.sql.Date(System.currentTimeMillis())); datos.put("ESTADO", "ENVIADA"); if (!DBHelper.insertar("RESPUESTA", datos)) throw new SQLException("Insert de respuesta rechazado por Oracle.");
    }
    private void actualizarRespuesta(int idResp, String resp) throws SQLException { LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>(); datos.put("CONTENIDO_RESPUESTA", resp); datos.put("RESPUESTA", resp); datos.put("FECHA_RESPUESTA", new java.sql.Date(System.currentTimeMillis())); datos.put("ESTADO", "ENVIADA"); if (!DBHelper.actualizar("RESPUESTA", "ID_RESPUESTA", Integer.valueOf(idResp), datos)) throw new SQLException("Update de respuesta rechazado por Oracle."); }
    private void ocultarId() { if (tabla.getColumnModel().getColumnCount() > 0) { tabla.getColumnModel().getColumn(0).setMinWidth(0); tabla.getColumnModel().getColumn(0).setMaxWidth(0); tabla.getColumnModel().getColumn(0).setPreferredWidth(0); } }
    private void msg(String m) { JOptionPane.showMessageDialog(this, m); }
}
