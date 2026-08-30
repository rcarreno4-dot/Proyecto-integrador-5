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

/** Pantalla simple para publicar preguntas, revisar respuestas, comentar y calificar. */
public class EvaluacionDocenteSimpleForm extends JFrame {
    private final Usuario usuario;
    private JComboBox<SimpleData.Item> cmbMatricula;
    private JTextArea txtPreguntas, txtObservacion;
    private JTextField txtNota;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblEstado;

    public EvaluacionDocenteSimpleForm(Usuario usuario) { this.usuario = usuario; init(); cargarMatriculas(); cargarTabla(); }

    private void init() {
        setTitle("Evaluaciones y preguntas - docente"); setSize(1280, 740); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        JPanel root = new JPanel(new BorderLayout(12,12)); root.setBackground(new Color(245,247,250)); root.setBorder(new EmptyBorder(12,12,12,12));
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(new Color(33,97,140)); header.setBorder(new EmptyBorder(16,20,16,20));
        JLabel title = new JLabel("Evaluación del estudiante"); title.setFont(new Font("Segoe UI", Font.BOLD, 22)); title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Publique preguntas, abra las respuestas del estudiante en una ventana, comente y califique."); sub.setForeground(Color.WHITE);
        header.add(title, BorderLayout.NORTH); header.add(sub, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(18,18,18,18)));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(6,6,6,6); gbc.gridx = 0; gbc.weightx = 1;
        cmbMatricula = new JComboBox<SimpleData.Item>();
        txtPreguntas = new JTextArea("Describa las actividades realizadas durante la práctica.\n¿Qué aprendizaje obtuvo en la institución receptora?\n¿Qué evidencias puede presentar del proceso?", 8, 28); txtPreguntas.setLineWrap(true); txtPreguntas.setWrapStyleWord(true);
        txtNota = new JTextField("0");
        txtObservacion = new JTextArea("Evaluación pendiente por responder.", 5, 28); txtObservacion.setLineWrap(true); txtObservacion.setWrapStyleWord(true);
        addCampo(form, gbc, 0, "Estudiante matriculado", cmbMatricula);
        gbc.gridy = 2; form.add(label("Preguntas para publicar (una por línea)"), gbc); gbc.gridy = 3; form.add(new JScrollPane(txtPreguntas), gbc);
        addCampo(form, gbc, 4, "Nota / puntaje final", txtNota);
        gbc.gridy = 6; form.add(label("Comentario / retroalimentación para el estudiante"), gbc); gbc.gridy = 7; form.add(new JScrollPane(txtObservacion), gbc);
        JButton btnPublicar = boton("Publicar evaluación", new Color(39,174,96)); btnPublicar.addActionListener(e -> publicarEvaluacion());
        JButton btnVer = boton("Ver respuestas / comentar", new Color(52,152,219)); btnVer.addActionListener(e -> abrirDetalleRespuestas());
        JButton btnCalificar = boton("Calificar seleccionada", new Color(243,156,18)); btnCalificar.addActionListener(e -> calificarSeleccionada());
        JButton btnAjustes = boton("Ajustes programa / etapa", new Color(142,68,173)); btnAjustes.addActionListener(e -> abrirAjustesPrograma());
        JButton btnEliminar = boton("Eliminar evaluación", new Color(192,57,43)); btnEliminar.addActionListener(e -> eliminarEvaluacion());
        JButton btnRefrescar = boton("Refrescar", new Color(52,73,94)); btnRefrescar.addActionListener(e -> { cargarMatriculas(); cargarTabla(); });
        JPanel botones = new JPanel(new GridLayout(6,1,8,8)); botones.setBackground(Color.WHITE); botones.add(btnPublicar); botones.add(btnVer); botones.add(btnCalificar); botones.add(btnAjustes); botones.add(btnEliminar); botones.add(btnRefrescar);
        gbc.gridy = 8; form.add(botones, gbc); lblEstado = new JLabel(" "); gbc.gridy = 9; form.add(lblEstado, gbc);

        modelo = new DefaultTableModel(new String[]{"ID", "Estudiante", "Práctica", "Estado", "Preguntas", "Respuestas", "Nota", "Comentario docente"}, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modelo); tabla.setRowHeight(28); tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
        JPanel panelTabla = new JPanel(new BorderLayout()); panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(10,10,10,10)));
        panelTabla.add(new JLabel("Histórico de evaluaciones, respuestas y comentarios"), BorderLayout.NORTH); panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, panelTabla); split.setDividerLocation(410); split.setResizeWeight(0.33);
        root.add(header, BorderLayout.NORTH); root.add(split, BorderLayout.CENTER); setContentPane(root);
    }

    private JLabel label(String s) { JLabel l = new JLabel(s); l.setFont(new Font("Segoe UI", Font.BOLD, 12)); return l; }
    private void addCampo(JPanel p, GridBagConstraints gbc, int y, String texto, JComponent comp) { gbc.gridy = y; p.add(label(texto), gbc); gbc.gridy = y+1; comp.setFont(new Font("Segoe UI", Font.PLAIN, 12)); p.add(comp, gbc); }
    private JButton boton(String t, Color c) { JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }
    private void cargarMatriculas() { cmbMatricula.removeAllItems(); for (SimpleData.Item it : SimpleData.matriculasTodas()) cmbMatricula.addItem(it); }

    private void publicarEvaluacion() {
        SimpleData.Item m = (SimpleData.Item)cmbMatricula.getSelectedItem(); if (m == null) { msg("No hay matrículas registradas. Primero el Coordinador debe vincular el estudiante."); return; }
        String preguntas = txtPreguntas.getText() == null ? "" : txtPreguntas.getText().trim(); if (preguntas.length() == 0) { msg("Escriba al menos una pregunta."); return; }
        try {
            int idPractica = idPracticaMatricula(m.id); int idEval = SimpleData.nextId("EVALUACION", "ID_EVALUACION", "SEQ_EVALUACION"); int idRubrica = SimpleData.asegurarRubrica(idPractica, usuario.getIdUsuario());
            LinkedHashMap<String,Object> eval = new LinkedHashMap<String,Object>();
            eval.put("ID_EVALUACION", idEval); eval.put("ID_MATRICULA_PRACTICA", m.id); eval.put("ID_PRACTICA", idPractica); eval.put("ID_RUBRICA", idRubrica == 0 ? null : Integer.valueOf(idRubrica)); eval.put("ID_EVALUADOR", usuario.getIdUsuario()); eval.put("PUNTAJE_OBTENIDO", Double.valueOf(0)); eval.put("NOTA_EVALUACION", Double.valueOf(0)); eval.put("NOTA", Double.valueOf(0)); eval.put("OBSERVACIONES", SimpleData.nvl(txtObservacion.getText(), "Evaluación pendiente por resolver.")); eval.put("FECHA_EVALUACION", new java.sql.Date(System.currentTimeMillis())); eval.put("ESTADO", "PENDIENTE");
            if (!DBHelper.insertar("EVALUACION", eval)) { msg("No se pudo crear la evaluación. Revise que la matrícula y la rúbrica existan."); return; }
            String[] lineas = preguntas.split("\\r?\\n"); int orden = 1; int creadas = 0;
            for (int i = 0; i < lineas.length; i++) {
                String q = lineas[i].trim(); if (q.length() == 0) continue;
                LinkedHashMap<String,Object> preg = new LinkedHashMap<String,Object>();
                preg.put("ID_PREGUNTA", SimpleData.nextId("PREGUNTA", "ID_PREGUNTA", "SEQ_PREGUNTA")); preg.put("ID_EVALUACION", idEval); preg.put("ID_PRACTICA", idPractica); preg.put("ENUNCIADO", q); preg.put("TEXTO_PREGUNTA", q); preg.put("TIPO_PREGUNTA", "ABIERTA"); preg.put("PUNTAJE_MAXIMO", Double.valueOf(5)); preg.put("ORDEN", Integer.valueOf(orden++)); preg.put("COMPARTIDA", Integer.valueOf(0)); preg.put("ESTADO", "ACTIVO");
                if (DBHelper.insertar("PREGUNTA", preg)) creadas++;
            }
            SimpleData.insertarLog(usuario.getIdUsuario(), "EVALUACION", "INSERT", "Evaluación publicada con " + creadas + " pregunta(s)", "DOCENTE");
            lblEstado.setText("Evaluación publicada. El estudiante ya la puede ver en su cuestionario."); cargarTabla(); seleccionarEvaluacion(idEval);
        } catch (Exception ex) { msg("Error publicando evaluación: " + ex.getMessage()); }
    }

    private int idPracticaMatricula(int idMatricula) throws SQLException {
        PreparedStatement ps = null; ResultSet rs = null;
        try { ps = ConexionBD.getConnection().prepareStatement("SELECT ID_PRACTICA FROM MATRICULA_PRACTICA WHERE ID_MATRICULA_PRACTICA=?"); ps.setInt(1, idMatricula); rs = ps.executeQuery(); if (rs.next()) return rs.getInt(1); throw new SQLException("La matrícula seleccionada no existe."); }
        finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }

    private void abrirDetalleRespuestas() {
        int idEval = evalSeleccionada(); if (idEval <= 0) { msg("Seleccione una evaluación."); return; }
        JDialog dlg = new JDialog(this, "Respuestas y retroalimentación", true);
        dlg.setSize(900, 620); dlg.setLocationRelativeTo(this);
        JPanel root = new JPanel(new BorderLayout(10,10)); root.setBorder(new EmptyBorder(12,12,12,12));
        JTextArea detalle = new JTextArea(cargarDetalleEvaluacion(idEval)); detalle.setEditable(false); detalle.setFont(new Font("Segoe UI", Font.PLAIN, 13)); detalle.setLineWrap(true); detalle.setWrapStyleWord(true);
        JTextArea comentario = new JTextArea(txtObservacion.getText(), 5, 60); comentario.setLineWrap(true); comentario.setWrapStyleWord(true);
        JButton guardar = boton("Guardar comentario para el estudiante", new Color(39,174,96));
        guardar.addActionListener(e -> { guardarComentario(idEval, comentario.getText()); dlg.dispose(); cargarTabla(); });
        JPanel abajo = new JPanel(new BorderLayout(6,6)); abajo.add(new JLabel("Comentario visible para el estudiante:"), BorderLayout.NORTH); abajo.add(new JScrollPane(comentario), BorderLayout.CENTER); abajo.add(guardar, BorderLayout.SOUTH);
        root.add(new JScrollPane(detalle), BorderLayout.CENTER); root.add(abajo, BorderLayout.SOUTH);
        dlg.setContentPane(root); dlg.setVisible(true);
    }

    private String cargarDetalleEvaluacion(int idEval) {
        StringBuilder sb = new StringBuilder();
        sb.append("EVALUACIÓN #").append(idEval).append("\n\n");
        String respJoin = DBHelper.existeTabla("RESPUESTA") ? " LEFT JOIN RESPUESTA R ON R.ID_PREGUNTA=Q.ID_PREGUNTA " : "";
        String respuesta = DBHelper.existeTabla("RESPUESTA") ? SimpleData.respuestaTextoExpr("R") : SimpleData.lit("");
        String retro = SimpleData.q("R", "RESPUESTA", "RETROALIMENTACION") == null ? SimpleData.lit("") : SimpleData.q("R", "RESPUESTA", "RETROALIMENTACION");
        String sql = "SELECT " + SimpleData.preguntaTextoExpr("Q") + ", " + respuesta + ", " + retro + " FROM PREGUNTA Q " + respJoin + " WHERE Q.ID_EVALUACION=? ORDER BY " + (SimpleData.has("PREGUNTA", "ORDEN") ? "Q.ORDEN" : "Q.ID_PREGUNTA");
        PreparedStatement ps=null; ResultSet rs=null; int n=0;
        try { ps=ConexionBD.getConnection().prepareStatement(sql); ps.setInt(1,idEval); rs=ps.executeQuery(); while(rs.next()) { n++; sb.append(n).append(". Pregunta: ").append(rs.getString(1)).append("\n"); sb.append("   Respuesta estudiante: ").append(rs.getString(2)==null||rs.getString(2).trim().length()==0?"(sin responder)":rs.getString(2)).append("\n"); sb.append("   Retroalimentación previa: ").append(rs.getString(3)==null?"":rs.getString(3)).append("\n\n"); } }
        catch(Exception e) { sb.append("No se pudieron cargar respuestas: ").append(e.getMessage()); }
        finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
        return sb.toString();
    }

    private void guardarComentario(int idEval, String comentario) {
        String texto = SimpleData.nvl(comentario, "Evaluación revisada por docente.");
        LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>();
        datos.put("OBSERVACIONES", texto); datos.put("OBSERVACION", texto);
        datos.put("FECHA_EVALUACION", new java.sql.Date(System.currentTimeMillis()));
        if (DBHelper.actualizar("EVALUACION", "ID_EVALUACION", Integer.valueOf(idEval), datos)) {
            // El mismo comentario queda también en cada respuesta para que el estudiante lo vea como retroalimentación.
            if (DBHelper.existeTabla("RESPUESTA") && SimpleData.has("RESPUESTA", "RETROALIMENTACION")) {
                try { Statement st=ConexionBD.getConnection().createStatement(); st.executeUpdate("UPDATE RESPUESTA SET RETROALIMENTACION='" + texto.replace("'", "''") + "' WHERE ID_PREGUNTA IN (SELECT ID_PREGUNTA FROM PREGUNTA WHERE ID_EVALUACION=" + idEval + ")"); st.close(); } catch(Exception e) { }
            }
            SimpleData.insertarLog(usuario.getIdUsuario(), "EVALUACION", "UPDATE", "Comentario docente: " + texto, "DOCENTE");
            lblEstado.setText("Comentario guardado. El estudiante ya puede verlo.");
        } else msg("No se pudo guardar el comentario.");
    }

    private void calificarSeleccionada() {
        int idEval = evalSeleccionada(); if (idEval <= 0) { msg("Seleccione una evaluación en la tabla."); return; }
        try {
            double nota = Double.parseDouble(txtNota.getText().trim().replace(',', '.'));
            LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>();
            datos.put("PUNTAJE_OBTENIDO", Double.valueOf(nota)); datos.put("NOTA_EVALUACION", Double.valueOf(nota)); datos.put("NOTA", Double.valueOf(nota)); datos.put("CALIFICACION", Double.valueOf(nota)); datos.put("OBSERVACIONES", SimpleData.nvl(txtObservacion.getText(), "Evaluación revisada por docente.")); datos.put("FECHA_EVALUACION", new java.sql.Date(System.currentTimeMillis())); datos.put("ESTADO", "FINALIZADA");
            if (DBHelper.actualizar("EVALUACION", "ID_EVALUACION", Integer.valueOf(idEval), datos)) { guardarComentario(idEval, txtObservacion.getText()); SimpleData.insertarLog(usuario.getIdUsuario(), "EVALUACION", "UPDATE", "Calificación registrada: " + nota, "DOCENTE"); lblEstado.setText("Calificación registrada. El estudiante ya puede verla."); cargarTabla(); }
            else msg("No se pudo actualizar la evaluación.");
        } catch (Exception ex) { msg("Nota no válida o error actualizando: " + ex.getMessage()); }
    }

    private void abrirAjustesPrograma() {
        int idEval = evalSeleccionada(); if (idEval <= 0) { msg("Seleccione una evaluación."); return; }
        try {
            DatosEval de = datosEval(idEval);
            JTextField txtEtapa = new JTextField(de.etapa == null ? "PRODUCTIVA" : de.etapa);
            JTextField txtEstadoMat = new JTextField(de.estadoMatricula == null ? "ACTIVO" : de.estadoMatricula);
            JTextField txtHorasReq = new JTextField(String.valueOf(de.horasRequeridas));
            JPanel p = new JPanel(new GridLayout(6,1,6,6));
            p.add(new JLabel("Etapa académica del estudiante:")); p.add(txtEtapa);
            p.add(new JLabel("Estado de matrícula:")); p.add(txtEstadoMat);
            p.add(new JLabel("Horas requeridas de la práctica:")); p.add(txtHorasReq);
            if (JOptionPane.showConfirmDialog(this, p, "Ajustes del programa del estudiante", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                LinkedHashMap<String,Object> mat = new LinkedHashMap<String,Object>(); mat.put("ETAPA_ACADEMICA", txtEtapa.getText()); mat.put("ETAPA", txtEtapa.getText()); mat.put("ESTADO", txtEstadoMat.getText());
                DBHelper.actualizar("MATRICULA_PRACTICA", "ID_MATRICULA_PRACTICA", Integer.valueOf(de.idMatricula), mat);
                LinkedHashMap<String,Object> pra = new LinkedHashMap<String,Object>(); pra.put("HORAS_REQUERIDAS", Double.valueOf(txtHorasReq.getText().replace(',', '.'))); pra.put("HORAS_REGLAMENTARIAS", Double.valueOf(txtHorasReq.getText().replace(',', '.')));
                DBHelper.actualizar("PRACTICA", "ID_PRACTICA", Integer.valueOf(de.idPractica), pra);
                SimpleData.insertarLog(usuario.getIdUsuario(), "MATRICULA_PRACTICA", "UPDATE", "Etapa/estado/horas ajustadas desde evaluación", "DOCENTE");
                lblEstado.setText("Ajustes actualizados."); cargarTabla();
            }
        } catch(Exception e) { msg("No se pudieron abrir ajustes: " + e.getMessage()); }
    }

    private static class DatosEval { int idMatricula; int idPractica; String etapa; String estadoMatricula; double horasRequeridas; }
    private DatosEval datosEval(int idEval) throws SQLException {
        DatosEval d = new DatosEval();
        String etapa = SimpleData.q("M", "MATRICULA_PRACTICA", "ETAPA_ACADEMICA", "ETAPA"); if (etapa == null) etapa = SimpleData.lit("PRODUCTIVA");
        String horas = SimpleData.q("P", "PRACTICA", "HORAS_REQUERIDAS", "HORAS_REGLAMENTARIAS"); if (horas == null) horas = "0";
        PreparedStatement ps=null; ResultSet rs=null;
        try { ps=ConexionBD.getConnection().prepareStatement("SELECT M.ID_MATRICULA_PRACTICA, M.ID_PRACTICA, " + etapa + ", " + SimpleData.estadoExpr("M", "MATRICULA_PRACTICA", "ACTIVO") + ", " + horas + " FROM EVALUACION E JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=E.ID_MATRICULA_PRACTICA LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA WHERE E.ID_EVALUACION=?"); ps.setInt(1,idEval); rs=ps.executeQuery(); if(rs.next()) { d.idMatricula=rs.getInt(1); d.idPractica=rs.getInt(2); d.etapa=rs.getString(3); d.estadoMatricula=rs.getString(4); d.horasRequeridas=rs.getDouble(5); } return d; }
        finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }

    private void eliminarEvaluacion() {
        int idEval = evalSeleccionada(); if (idEval <= 0) { msg("Seleccione una evaluación."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar o inactivar la evaluación seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        boolean ok = DBHelper.eliminarLogico("EVALUACION", "ID_EVALUACION", Integer.valueOf(idEval));
        if (ok) { SimpleData.insertarLog(usuario.getIdUsuario(), "EVALUACION", "DELETE", "Evaluación eliminada/inactivada", "DOCENTE"); cargarTabla(); }
        else msg("No se pudo eliminar. Puede tener preguntas/respuestas asociadas; se intentará dejar como INACTIVA si existe ESTADO.");
    }

    private int evalSeleccionada() { int row = tabla.getSelectedRow(); return row < 0 ? -1 : Integer.parseInt(String.valueOf(modelo.getValueAt(row, 0))); }
    private void seleccionarEvaluacion(int idEval) { for(int i=0;i<modelo.getRowCount();i++) if(Integer.parseInt(String.valueOf(modelo.getValueAt(i,0)))==idEval) { tabla.setRowSelectionInterval(i,i); break; } }
    private void cargarSeleccion() { int row = tabla.getSelectedRow(); if (row >= 0) { Object nota = modelo.getValueAt(row, 6); Object obs = modelo.getValueAt(row, 7); txtNota.setText(nota == null ? "0" : String.valueOf(nota)); txtObservacion.setText(obs == null ? "" : String.valueOf(obs)); } }

    private void cargarTabla() {
        modelo.setRowCount(0);
        if (!DBHelper.existeTabla("EVALUACION") || !SimpleData.has("EVALUACION", "ID_MATRICULA_PRACTICA")) { lblEstado.setText("La tabla EVALUACION no está lista para matrículas."); return; }
        String estCol = SimpleData.matriculaEstudianteCol(); if (estCol == null) { lblEstado.setText("No se encuentra columna de estudiante en MATRÍCULA."); return; }
        String preguntaCount = DBHelper.existeTabla("PREGUNTA") && SimpleData.has("PREGUNTA", "ID_EVALUACION") ? "(SELECT COUNT(*) FROM PREGUNTA Q WHERE Q.ID_EVALUACION=E.ID_EVALUACION" + (SimpleData.has("PREGUNTA", "ESTADO") ? " AND NVL(UPPER(Q.ESTADO),'ACTIVO')='ACTIVO'" : "") + ")" : "0";
        String respCount = DBHelper.existeTabla("PREGUNTA") && DBHelper.existeTabla("RESPUESTA") ? "(SELECT COUNT(*) FROM PREGUNTA Q JOIN RESPUESTA R ON R.ID_PREGUNTA=Q.ID_PREGUNTA WHERE Q.ID_EVALUACION=E.ID_EVALUACION)" : "0";
        String fecha = SimpleData.has("EVALUACION", "FECHA_EVALUACION") ? "E.FECHA_EVALUACION" : "E.ID_EVALUACION";
        String obs = SimpleData.q("E", "EVALUACION", "OBSERVACIONES", "OBSERVACION"); if (obs == null) obs = SimpleData.lit("");
        String sql = "SELECT E.ID_EVALUACION, " + SimpleData.usuarioNombreExpr("U") + " ESTUDIANTE, " + SimpleData.practicaTituloExpr("P") + " PRACTICA, " + SimpleData.estadoExpr("E", "EVALUACION", "PENDIENTE") + " ESTADO, " + preguntaCount + " PREGUNTAS, " + respCount + " RESPUESTAS, " + SimpleData.notaExpr("E") + " NOTA, " + obs + " OBSERVACIONES " +
                "FROM EVALUACION E JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=E.ID_MATRICULA_PRACTICA JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA " +
                (SimpleData.has("EVALUACION", "ESTADO") ? "WHERE NVL(UPPER(E.ESTADO),'ACTIVO') <> 'INACTIVO' " : "") +
                "ORDER BY " + fecha + " DESC, E.ID_EVALUACION DESC";
        Statement st = null; ResultSet rs = null; int total = 0;
        try { st = ConexionBD.getConnection().createStatement(); rs = st.executeQuery(sql); while (rs.next()) { total++; modelo.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getObject(7), rs.getString(8)}); } ocultarId(); lblEstado.setText("Evaluaciones cargadas: " + total); }
        catch (Exception e) { msg("No se pudo cargar evaluaciones: " + e.getMessage()); }
        finally { Utilidades.cerrar(rs); try { if (st != null) st.close(); } catch (Exception e) { } }
    }
    private void ocultarId() { if (tabla.getColumnModel().getColumnCount() > 0) { tabla.getColumnModel().getColumn(0).setMinWidth(0); tabla.getColumnModel().getColumn(0).setMaxWidth(0); tabla.getColumnModel().getColumn(0).setPreferredWidth(0); } }
    private void msg(String m) { JOptionPane.showMessageDialog(this, m); }
}
