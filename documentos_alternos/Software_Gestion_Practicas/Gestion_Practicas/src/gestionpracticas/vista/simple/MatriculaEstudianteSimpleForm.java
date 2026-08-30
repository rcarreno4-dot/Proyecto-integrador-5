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
import java.util.List;

/** Pantalla simple para vincular estudiantes sin escribir ID manualmente. */
public class MatriculaEstudianteSimpleForm extends JFrame {
    private final Usuario usuario;
    private JComboBox<SimpleData.Item> cmbEstudiante, cmbPractica, cmbInstitucion, cmbGrupo;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblEstado;

    public MatriculaEstudianteSimpleForm(Usuario usuario) { this.usuario = usuario; init(); cargarCombos(); cargarTabla(); }

    private void init() {
        setTitle("Vincular estudiantes a práctica - modo sencillo");
        setSize(1280, 720); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);

        JPanel root = new JPanel(new BorderLayout(12,12)); root.setBackground(new Color(245,247,250)); root.setBorder(new EmptyBorder(12,12,12,12));
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(new Color(33,97,140)); header.setBorder(new EmptyBorder(16,20,16,20));
        JLabel title = new JLabel("Vinculación de estudiantes"); title.setFont(new Font("Segoe UI", Font.BOLD, 22)); title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Seleccione estudiante, práctica, institución y grupo. El sistema usa los ID internamente y muestra el avance productivo."); sub.setForeground(Color.WHITE);
        header.add(title, BorderLayout.NORTH); header.add(sub, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(18,18,18,18)));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(7,7,7,7); gbc.gridx = 0; gbc.weightx = 1;
        cmbEstudiante = new JComboBox<SimpleData.Item>(); cmbPractica = new JComboBox<SimpleData.Item>(); cmbInstitucion = new JComboBox<SimpleData.Item>(); cmbGrupo = new JComboBox<SimpleData.Item>();
        cmbPractica.addActionListener(e -> cargarGrupos()); cmbInstitucion.addActionListener(e -> cargarGrupos());
        addCampo(form, gbc, 0, "Estudiante", cmbEstudiante); addCampo(form, gbc, 2, "Práctica", cmbPractica); addCampo(form, gbc, 4, "Institución receptora", cmbInstitucion); addCampo(form, gbc, 6, "Grupo", cmbGrupo);
        JButton btnGuardar = boton("Matricular estudiante", new Color(39,174,96)); btnGuardar.addActionListener(e -> matricular());
        JButton btnCrearGrupo = boton("Crear grupo para esta práctica", new Color(52,152,219)); btnCrearGrupo.addActionListener(e -> crearGrupo());
        JButton btnRefrescar = boton("Refrescar", new Color(52,73,94)); btnRefrescar.addActionListener(e -> { cargarCombos(); cargarTabla(); });
        JPanel botones = new JPanel(new GridLayout(3,1,8,8)); botones.setBackground(Color.WHITE); botones.add(btnGuardar); botones.add(btnCrearGrupo); botones.add(btnRefrescar);
        gbc.gridy = 8; form.add(botones, gbc); lblEstado = new JLabel(" "); lblEstado.setForeground(new Color(80,80,80)); gbc.gridy = 9; form.add(lblEstado, gbc);

        modelo = new DefaultTableModel(new String[]{"ID", "Estudiante", "Práctica", "Grupo", "Institución", "Etapa", "Estado", "Horas reportadas", "Horas aprobadas", "Pendientes", "Promedio"}, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modelo); tabla.setRowHeight(26);
        JPanel panelTabla = new JPanel(new BorderLayout()); panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(10,10,10,10)));
        panelTabla.add(new JLabel("Matrículas actuales, programa productivo y avance"), BorderLayout.NORTH); panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, panelTabla); split.setDividerLocation(360); split.setResizeWeight(0.28);
        root.add(header, BorderLayout.NORTH); root.add(split, BorderLayout.CENTER); setContentPane(root);
    }

    private void addCampo(JPanel p, GridBagConstraints gbc, int y, String texto, JComponent comp) { gbc.gridy = y; JLabel l = new JLabel(texto); l.setFont(new Font("Segoe UI", Font.BOLD, 12)); p.add(l, gbc); gbc.gridy = y + 1; comp.setFont(new Font("Segoe UI", Font.PLAIN, 12)); p.add(comp, gbc); }
    private JButton boton(String txt, Color color) { JButton b = new JButton(txt); b.setBackground(color); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }
    private boolean esEstudiante() { String r = usuario.getRol() == null ? "" : usuario.getRol().toUpperCase(); return r.indexOf("ESTUDIANTE") >= 0; }
    private void cargarCombos() {
        if (esEstudiante()) {
            cmbEstudiante.removeAllItems();
            cmbEstudiante.addItem(new SimpleData.Item(usuario.getIdUsuario(), usuario.getNombre() + " " + usuario.getApellido()));
            cmbEstudiante.setEnabled(false);
            lblEstado.setText("Auto matrícula: selecciona la práctica, institución y grupo disponible para solicitar/vincularte.");
        } else cargar(cmbEstudiante, SimpleData.estudiantes());
        cargar(cmbPractica, SimpleData.practicas()); cargar(cmbInstitucion, SimpleData.instituciones()); cargarGrupos();
    }
    private void cargarGrupos() { SimpleData.Item p = (SimpleData.Item)cmbPractica.getSelectedItem(); SimpleData.Item i = (SimpleData.Item)cmbInstitucion.getSelectedItem(); cargar(cmbGrupo, SimpleData.grupos(p == null ? null : Integer.valueOf(p.id), i == null ? null : Integer.valueOf(i.id))); }
    private void cargar(JComboBox<SimpleData.Item> combo, List<SimpleData.Item> datos) { combo.removeAllItems(); for (SimpleData.Item it : datos) combo.addItem(it); }

    private void crearGrupo() {
        SimpleData.Item p = (SimpleData.Item)cmbPractica.getSelectedItem(); SimpleData.Item i = (SimpleData.Item)cmbInstitucion.getSelectedItem();
        if (p == null || i == null) { msg("Debe seleccionar práctica e institución."); return; }
        try {
            int id = SimpleData.nextId("GRUPO", "ID_GRUPO", "SEQ_GRUPO");
            LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>();
            datos.put("ID_GRUPO", id); datos.put("NOMBRE", "Grupo " + p.texto.split(" - ")[0]); datos.put("NOMBRE_GRUPO", "Grupo " + p.texto.split(" - ")[0]); datos.put("ESTADO", "ACTIVO"); datos.put("ID_PRACTICA", p.id); datos.put("ID_INSTITUCION", i.id); datos.put("CUPO_MAXIMO", 30);
            if (DBHelper.insertar("GRUPO", datos)) { SimpleData.insertarLog(usuario.getIdUsuario(), "GRUPO", "INSERT", "Grupo creado para práctica " + p.texto, "COORDINADOR"); lblEstado.setText("Grupo creado correctamente."); cargarGrupos(); cargarTabla(); }
            else msg("No se pudo crear el grupo. Revise restricciones de Oracle.");
        } catch (Exception ex) { msg("Error creando grupo: " + ex.getMessage()); }
    }

    private void matricular() {
        SimpleData.Item est = (SimpleData.Item)cmbEstudiante.getSelectedItem(); SimpleData.Item prac = (SimpleData.Item)cmbPractica.getSelectedItem(); SimpleData.Item inst = (SimpleData.Item)cmbInstitucion.getSelectedItem(); SimpleData.Item grupo = (SimpleData.Item)cmbGrupo.getSelectedItem();
        if (est == null || prac == null || inst == null || grupo == null) { msg("Debe seleccionar estudiante, práctica, institución y grupo. Si no hay grupo, cree uno primero."); return; }
        try {
            if (existeMatricula(est.id, prac.id)) { msg("El estudiante ya está matriculado en esa práctica. Se refresca la tabla para que puedas verlo."); cargarTabla(); return; }
            LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>();
            datos.put("ID_MATRICULA_PRACTICA", SimpleData.nextId("MATRICULA_PRACTICA", "ID_MATRICULA_PRACTICA", "SEQ_MATRICULA_PRACTICA"));
            datos.put("ID_ESTUDIANTE", est.id); datos.put("ID_USUARIO", est.id); datos.put("ID_PRACTICA", prac.id); datos.put("ID_GRUPO", grupo.id); datos.put("ID_INSTITUCION", inst.id); datos.put("FECHA_MATRICULA", new java.sql.Date(System.currentTimeMillis())); datos.put("ESTADO", "ACTIVO"); datos.put("ETAPA_ACADEMICA", "PRODUCTIVA"); datos.put("ETAPA", "PRODUCTIVA"); datos.put("HORAS_COMPLETAS", Integer.valueOf(0));
            if (DBHelper.insertar("MATRICULA_PRACTICA", datos)) { SimpleData.insertarLog(usuario.getIdUsuario(), "MATRICULA_PRACTICA", "INSERT", "Matrícula de " + est.texto + " en " + prac.texto, esEstudiante() ? "ESTUDIANTE" : "COORDINADOR"); lblEstado.setText(esEstudiante() ? "Solicitud/matrícula registrada. El coordinador puede validarla desde seguimiento." : "Matrícula registrada correctamente."); cargarTabla(); }
            else msg("No se pudo matricular. Verifique que práctica, grupo e institución existan y estén activos.");
        } catch (Exception ex) { msg("Error al matricular: " + ex.getMessage()); }
    }

    private boolean existeMatricula(int idEst, int idPrac) throws SQLException {
        String estCol = SimpleData.matriculaEstudianteCol(); if (estCol == null) return false;
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) FROM MATRICULA_PRACTICA WHERE " + estCol + "=? AND ID_PRACTICA=?" + (SimpleData.has("MATRICULA_PRACTICA","ESTADO") ? " AND NVL(UPPER(ESTADO),'ACTIVO') <> 'INACTIVO'" : "");
            ps = ConexionBD.getConnection().prepareStatement(sql); ps.setInt(1, idEst); ps.setInt(2, idPrac); rs = ps.executeQuery(); return rs.next() && rs.getInt(1) > 0;
        } finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }


    private String horasReportadasSub(String idM) {
        String s = "0";
        if (DBHelper.existeTabla("HORAS_PRACTICA")) {
            String c = SimpleData.col("HORAS_PRACTICA", "HORAS_REPORTADAS", "HORAS");
            String m = SimpleData.col("HORAS_PRACTICA", "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
            if (c != null && m != null) s += "+(SELECT NVL(SUM(H." + c + "),0) FROM HORAS_PRACTICA H WHERE H." + m + "=" + idM + ")";
        }
        if (DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) {
            String c = SimpleData.col("REGISTRO_ACTIVIDAD", "HORAS", "HORAS_REPORTADAS");
            String m = SimpleData.col("REGISTRO_ACTIVIDAD", "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
            if (c != null && m != null) s += "+(SELECT NVL(SUM(R." + c + "),0) FROM REGISTRO_ACTIVIDAD R WHERE R." + m + "=" + idM + ")";
        }
        return "(" + s + ")";
    }
    private String horasAprobadasSub(String idM) {
        String s = "0";
        if (DBHelper.existeTabla("HORAS_PRACTICA")) {
            String c = SimpleData.col("HORAS_PRACTICA", "HORAS_APROBADAS");
            String m = SimpleData.col("HORAS_PRACTICA", "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
            if (c != null && m != null) s += "+(SELECT NVL(SUM(H." + c + "),0) FROM HORAS_PRACTICA H WHERE H." + m + "=" + idM + ")";
        }
        if (DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) {
            String c = SimpleData.col("REGISTRO_ACTIVIDAD", "HORAS", "HORAS_REPORTADAS");
            String m = SimpleData.col("REGISTRO_ACTIVIDAD", "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
            if (c != null && m != null) s += "+(SELECT NVL(SUM(R." + c + "),0) FROM REGISTRO_ACTIVIDAD R WHERE R." + m + "=" + idM + " AND UPPER(NVL(R.ESTADO,'PENDIENTE'))='APROBADO')";
        }
        return "(" + s + ")";
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        String estCol = SimpleData.matriculaEstudianteCol();
        if (estCol == null) { lblEstado.setText("La tabla MATRICULA_PRACTICA no tiene columna de estudiante."); return; }
        String joins = " JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA ";
        if (SimpleData.has("MATRICULA_PRACTICA", "ID_GRUPO")) joins += " LEFT JOIN GRUPO G ON G.ID_GRUPO=M.ID_GRUPO "; else joins += " LEFT JOIN GRUPO G ON G.ID_PRACTICA=M.ID_PRACTICA ";
        String instJoinExpr = SimpleData.matriculaInstitucionExpr("M", "P"); if (instJoinExpr != null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION=" + instJoinExpr + " ";
        String instTxt = instJoinExpr == null ? SimpleData.lit("Sin institución") : SimpleData.institucionNombreExpr("I");
        String idM = "M.ID_MATRICULA_PRACTICA";
        String hr = horasReportadasSub(idM);
        String ha = horasAprobadasSub(idM);
        String prom = DBHelper.existeTabla("EVALUACION") ? "(SELECT NVL(ROUND(AVG(" + SimpleData.notaExpr("E") + "),2),0) FROM EVALUACION E WHERE E.ID_MATRICULA_PRACTICA=" + idM + ")" : "0";
        String sql = "SELECT " + idM + " ID_MATRICULA, " + SimpleData.usuarioNombreExpr("U") + " ESTUDIANTE, " + SimpleData.practicaTituloExpr("P") + " PRACTICA, " + SimpleData.grupoNombreExpr("G") + " GRUPO, " + instTxt + " INSTITUCION, " + SimpleData.etapaMatriculaExpr("M") + " ETAPA, " + SimpleData.estadoExpr("M", "MATRICULA_PRACTICA", "ACTIVO") + " ESTADO, " + hr + " HR, " + ha + " HA, (" + hr + "-" + ha + ") PEND, " + prom + " PROM FROM MATRICULA_PRACTICA M " + joins + " ORDER BY " + SimpleData.usuarioNombreExpr("U") + ", " + SimpleData.practicaTituloExpr("P");
        Statement st = null; ResultSet rs = null; int total = 0;
        try {
            st = ConexionBD.getConnection().createStatement(); rs = st.executeQuery(sql);
            while (rs.next()) { total++; modelo.addRow(new Object[]{rs.getInt("ID_MATRICULA"), rs.getString("ESTUDIANTE"), rs.getString("PRACTICA"), rs.getString("GRUPO"), rs.getString("INSTITUCION"), rs.getString("ETAPA"), rs.getString("ESTADO"), rs.getDouble("HR"), rs.getDouble("HA"), rs.getDouble("PEND"), rs.getDouble("PROM")}); }
            ocultarId(); lblEstado.setText("Matrículas visibles: " + total + ". Selecciona Control de horas para ver el histórico detallado.");
        } catch (Exception e) { msg("No se pudo cargar matrículas: " + e.getMessage()); }
        finally { Utilidades.cerrar(rs); try { if (st != null) st.close(); } catch (Exception e) {} }
    }

    private void ocultarId() { if (tabla.getColumnModel().getColumnCount() > 0) { tabla.getColumnModel().getColumn(0).setMinWidth(0); tabla.getColumnModel().getColumn(0).setMaxWidth(0); tabla.getColumnModel().getColumn(0).setPreferredWidth(0); } }
    private void msg(String m) { JOptionPane.showMessageDialog(this, m); }
}
