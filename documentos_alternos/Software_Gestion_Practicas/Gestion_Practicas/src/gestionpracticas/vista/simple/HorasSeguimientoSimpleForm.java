package com.gestionpracticas.vista.simple;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.util.LinkedHashMap;

/**
 * Control de horas robusto para presentación.
 * Registra actividades en REGISTRO_ACTIVIDAD y, si la tabla existe y es compatible,
 * también en HORAS_PRACTICA. El histórico se muestra aunque una de las dos tablas no exista.
 */
public class HorasSeguimientoSimpleForm extends JFrame {
    private final Usuario usuario;
    private JComboBox<SimpleData.Item> cmbMatricula;
    private JTextField txtFecha, txtHoras;
    private JTextArea txtDescripcion, txtObservacion;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblResumen;

    public HorasSeguimientoSimpleForm(Usuario usuario) {
        this.usuario = usuario;
        init();
        cargarMatriculas();
        cargarTabla();
    }

    private void init() {
        setTitle("Control de horas y seguimiento");
        setSize(1280, 740);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);

        JPanel root = new JPanel(new BorderLayout(12,12));
        root.setBackground(new Color(245,247,250));
        root.setBorder(new EmptyBorder(12,12,12,12));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33,97,140));
        header.setBorder(new EmptyBorder(16,20,16,20));
        JLabel title = new JLabel("Control de horas y actividades");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("El estudiante registra actividades; docente, coordinador o institución las aprueban o rechazan. Todo queda en histórico.");
        sub.setForeground(Color.WHITE);
        header.add(title, BorderLayout.NORTH);
        header.add(sub, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(15,15,15,15)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0;
        gbc.weightx = 1;

        cmbMatricula = new JComboBox<SimpleData.Item>();
        txtFecha = new JTextField(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        txtHoras = new JTextField("1");
        txtDescripcion = new JTextArea(5, 28);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtObservacion = new JTextArea(4, 28);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);

        addCampo(form, gbc, 0, "Matrícula / estudiante", cmbMatricula);
        addCampo(form, gbc, 2, "Fecha (yyyy-MM-dd o dd/MM/yyyy)", txtFecha);
        addCampo(form, gbc, 4, "Horas realizadas", txtHoras);
        gbc.gridy = 6; form.add(label("Actividad realizada por el estudiante"), gbc);
        gbc.gridy = 7; form.add(new JScrollPane(txtDescripcion), gbc);
        gbc.gridy = 8; form.add(label("Observación de validación"), gbc);
        gbc.gridy = 9; form.add(new JScrollPane(txtObservacion), gbc);

        JButton btnRegistrar = boton("Registrar actividad / horas", new Color(39,174,96));
        btnRegistrar.addActionListener(e -> registrarHoras());
        JButton btnAprobar = boton("Aprobar seleccionada", new Color(52,152,219));
        btnAprobar.addActionListener(e -> validarSeleccionada("APROBADO"));
        JButton btnRechazar = boton("Rechazar seleccionada", new Color(192,57,43));
        btnRechazar.addActionListener(e -> validarSeleccionada("RECHAZADO"));
        JButton btnActualizar = boton("Actualizar observación", new Color(142,68,173));
        btnActualizar.addActionListener(e -> actualizarObservacion());
        JButton btnEliminar = boton("Eliminar registro", new Color(127,140,141));
        btnEliminar.addActionListener(e -> eliminarSeleccionada());
        JButton btnRefrescar = boton("Refrescar", new Color(52,73,94));
        btnRefrescar.addActionListener(e -> { cargarMatriculas(); cargarTabla(); });
        JPanel botones = new JPanel(new GridLayout(esEstudiante() ? 2 : 6,1,8,8));
        botones.setBackground(Color.WHITE);
        botones.add(btnRegistrar);
        if (!esEstudiante()) {
            botones.add(btnAprobar); botones.add(btnRechazar); botones.add(btnActualizar); botones.add(btnEliminar);
        } else {
            txtObservacion.setText("Pendiente de validación");
            txtObservacion.setEditable(false);
        }
        botones.add(btnRefrescar);
        gbc.gridy = 10; form.add(botones, gbc);

        modelo = new DefaultTableModel(new String[]{"ID", "Fuente", "Estudiante", "Práctica", "Institución", "Fecha", "Reportadas", "Aprobadas", "Estado", "Actividad", "Observación"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setDefaultRenderer(Object.class, new EstadoColorRenderer());
        tabla.getSelectionModel().addListSelectionListener(e -> cargarObservacionSeleccionada());

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(10,10,10,10)));
        lblResumen = new JLabel("Histórico de horas y actividades");
        panelTabla.add(lblResumen, BorderLayout.NORTH);
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, panelTabla);
        split.setDividerLocation(405);
        split.setResizeWeight(0.32);
        root.add(header, BorderLayout.NORTH);
        root.add(split, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JLabel label(String s) { JLabel l = new JLabel(s); l.setFont(new Font("Segoe UI", Font.BOLD, 12)); return l; }
    private void addCampo(JPanel p, GridBagConstraints gbc, int y, String texto, JComponent comp) { gbc.gridy = y; p.add(label(texto), gbc); gbc.gridy = y+1; comp.setFont(new Font("Segoe UI", Font.PLAIN, 12)); p.add(comp, gbc); }
    private JButton boton(String t, Color c) { JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }
    private boolean esEstudiante() { String r = usuario.getRol() == null ? "" : usuario.getRol().toUpperCase(); return r.indexOf("ESTUDIANTE") >= 0; }
    private void cargarMatriculas() { cmbMatricula.removeAllItems(); java.util.List<SimpleData.Item> items = esEstudiante() ? SimpleData.matriculasDeEstudiante(usuario.getIdUsuario()) : SimpleData.matriculasTodas(); for (SimpleData.Item it : items) cmbMatricula.addItem(it); }

    private void registrarHoras() {
        SimpleData.Item m = (SimpleData.Item)cmbMatricula.getSelectedItem();
        if (m == null) { msg("No hay matrícula seleccionada. Primero vincula el estudiante desde Coordinador > Vincular Estudiantes."); return; }
        try {
            double horas = Double.parseDouble(txtHoras.getText().trim().replace(',', '.'));
            if (horas <= 0) { msg("Las horas deben ser mayores a cero."); return; }
            String desc = SimpleData.nvl(txtDescripcion.getText(), "Actividad de práctica registrada por estudiante.");
            String obs = SimpleData.nvl(txtObservacion.getText(), "Pendiente de validación.");

            boolean okRA = insertarRegistroActividad(m.id, horas, desc, obs);
            boolean okHP = false;
            if (!okRA) okHP = insertarHorasPractica(m.id, horas, desc);

            if (!okRA && !okHP) {
                msg("No se pudo registrar. Verifique que exista una matrícula válida y que REGISTRO_ACTIVIDAD u HORAS_PRACTICA estén creadas.");
                return;
            }
            insertarHistorial(m.id, "REGISTRO_HORAS", "SIN REGISTRO", "PENDIENTE", "Horas reportadas: " + horas + ". Actividad: " + desc);
            SimpleData.insertarLog(usuario.getIdUsuario(), okHP ? "HORAS_PRACTICA" : "REGISTRO_ACTIVIDAD", "INSERT", "Horas reportadas: " + horas + " | " + desc, "HORAS");
            txtDescripcion.setText("");
            lblResumen.setText("Actividad registrada. Pendiente de aprobación por docente, coordinador o institución.");
            cargarTabla();
        } catch (Exception ex) { msg("Error registrando horas: " + ex.getMessage()); }
    }

    private boolean insertarHorasPractica(int idMatricula, double horas, String desc) {
        if (!DBHelper.existeTabla("HORAS_PRACTICA")) return false;
        try {
            LinkedHashMap<String,Object> hp = new LinkedHashMap<String,Object>();
            hp.put("ID_HORAS_PRACTICA", SimpleData.nextId("HORAS_PRACTICA", SimpleData.col("HORAS_PRACTICA", "ID_HORAS_PRACTICA", "ID_HORA"), "SEQ_HORAS_PRACTICA"));
            hp.put("ID_HORA", SimpleData.nextId("HORAS_PRACTICA", SimpleData.col("HORAS_PRACTICA", "ID_HORA", "ID_HORAS_PRACTICA"), "SEQ_HORAS_PRACTICA"));
            hp.put("ID_MATRICULA_PRACTICA", idMatricula);
            hp.put("ID_MATRICULA", idMatricula);
            hp.put("HORAS_REPORTADAS", Double.valueOf(horas));
            hp.put("HORAS", Double.valueOf(horas));
            hp.put("HORAS_APROBADAS", Double.valueOf(0));
            hp.put("FECHA_REGISTRO", SimpleData.parseDate(txtFecha.getText()));
            hp.put("FECHA", SimpleData.parseDate(txtFecha.getText()));
            hp.put("CONFIRMADO_POR_INSTITUCION", Integer.valueOf(0));
            hp.put("OBSERVACION", desc);
            hp.put("OBSERVACIONES", desc);
            hp.put("ESTADO", "PENDIENTE");
            return DBHelper.insertar("HORAS_PRACTICA", hp);
        } catch (Exception ex) {
            System.err.println("HorasSeguimiento.insertarHorasPractica: " + ex.getMessage());
            return false;
        }
    }

    private boolean insertarRegistroActividad(int idMatricula, double horas, String desc, String obs) {
        if (!DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) return false;
        try {
            int[] ids = idsMatricula(idMatricula);
            String idCol = SimpleData.col("REGISTRO_ACTIVIDAD", "ID_REGISTRO", "ID_ACTIVIDAD");
            if (idCol == null) idCol = "ID_REGISTRO";
            int next = SimpleData.nextId("REGISTRO_ACTIVIDAD", idCol, "SEQ_REGISTRO_ACTIVIDAD");
            LinkedHashMap<String,Object> ra = new LinkedHashMap<String,Object>();
            ra.put("ID_REGISTRO", next);
            ra.put("ID_ACTIVIDAD", next);
            ra.put("ID_MATRICULA_PRACTICA", idMatricula);
            ra.put("ID_MATRICULA", idMatricula);
            ra.put("ID_USUARIO", ids[0] == 0 ? usuario.getIdUsuario() : ids[0]);
            ra.put("ID_ESTUDIANTE", ids[0] == 0 ? usuario.getIdUsuario() : ids[0]);
            ra.put("ID_INSTITUCION", ids[1] == 0 ? null : Integer.valueOf(ids[1]));
            ra.put("FECHA_ACTIVIDAD", SimpleData.parseDate(txtFecha.getText()));
            ra.put("FECHA_REGISTRO", SimpleData.parseDate(txtFecha.getText()));
            ra.put("DESCRIPCION", desc);
            ra.put("HORAS", Double.valueOf(horas));
            ra.put("HORAS_REPORTADAS", Double.valueOf(horas));
            ra.put("HORAS_APROBADAS", Double.valueOf(0));
            ra.put("TIPO_ACTIVIDAD", "PRACTICA");
            ra.put("OBSERVACIONES", obs);
            ra.put("OBSERVACION", obs);
            ra.put("ESTADO", "PENDIENTE");
            return DBHelper.insertar("REGISTRO_ACTIVIDAD", ra);
        } catch (Exception ex) {
            System.err.println("HorasSeguimiento.insertarRegistroActividad: " + ex.getMessage());
            return false;
        }
    }

    private int[] idsMatricula(int idMatricula) throws SQLException {
        String estCol = SimpleData.matriculaEstudianteCol();
        String instM = SimpleData.col("MATRICULA_PRACTICA", "ID_INSTITUCION");
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            String sql = "SELECT " + (estCol == null ? "0" : estCol) + ", " + (instM == null ? "0" : instM) + " FROM MATRICULA_PRACTICA WHERE ID_MATRICULA_PRACTICA=?";
            ps = ConexionBD.getConnection().prepareStatement(sql);
            ps.setInt(1, idMatricula);
            rs = ps.executeQuery();
            if (rs.next()) return new int[]{rs.getInt(1), rs.getInt(2)};
        } finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
        return new int[]{usuario.getIdUsuario(), 0};
    }

    private void validarSeleccionada(String estadoNuevo) {
        int row = tabla.getSelectedRow();
        if (row < 0) { msg("Seleccione un registro de horas o actividad."); return; }
        int id = Integer.parseInt(String.valueOf(modelo.getValueAt(row, 0)));
        String fuente = String.valueOf(modelo.getValueAt(row, 1));
        double reportadas = toDouble(modelo.getValueAt(row, 6));
        String obs = SimpleData.nvl(txtObservacion.getText(), estadoNuevo.equals("APROBADO") ? "Horas aprobadas." : "Horas rechazadas.");
        try {
            LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>();
            datos.put("ESTADO", estadoNuevo);
            datos.put("OBSERVACION", obs);
            datos.put("OBSERVACIONES", obs);
            datos.put("HORAS_APROBADAS", Double.valueOf("APROBADO".equals(estadoNuevo) ? reportadas : 0));
            datos.put("CONFIRMADO_POR_INSTITUCION", Integer.valueOf("APROBADO".equals(estadoNuevo) ? 1 : 0));
            boolean ok;
            if ("HORAS".equals(fuente)) ok = DBHelper.actualizar("HORAS_PRACTICA", SimpleData.col("HORAS_PRACTICA", "ID_HORAS_PRACTICA", "ID_HORA"), Integer.valueOf(id), datos);
            else ok = DBHelper.actualizar("REGISTRO_ACTIVIDAD", SimpleData.col("REGISTRO_ACTIVIDAD", "ID_REGISTRO", "ID_ACTIVIDAD"), Integer.valueOf(id), datos);
            if (ok) {
                int idMat = obtenerMatriculaDesdeFila(row);
                insertarHistorial(idMat, "VALIDACION_HORAS", "PENDIENTE", estadoNuevo, obs);
                SimpleData.insertarLog(usuario.getIdUsuario(), fuente.equals("HORAS") ? "HORAS_PRACTICA" : "REGISTRO_ACTIVIDAD", "UPDATE", estadoNuevo + " | " + obs, "HORAS");
                lblResumen.setText("Registro actualizado a " + estadoNuevo + ".");
                cargarTabla();
            } else msg("No se pudo actualizar el registro seleccionado.");
        } catch (Exception ex) { msg("Error validando horas: " + ex.getMessage()); }
    }

    private int obtenerMatriculaDesdeFila(int row) {
        // El ID de matrícula no se muestra para mantener la interfaz sencilla; se busca por registro.
        int id = Integer.parseInt(String.valueOf(modelo.getValueAt(row, 0)));
        String fuente = String.valueOf(modelo.getValueAt(row, 1));
        String tablaBD = "HORAS".equals(fuente) ? "HORAS_PRACTICA" : "REGISTRO_ACTIVIDAD";
        String idCol = "HORAS".equals(fuente) ? SimpleData.col(tablaBD, "ID_HORAS_PRACTICA", "ID_HORA") : SimpleData.col(tablaBD, "ID_REGISTRO", "ID_ACTIVIDAD");
        String matCol = SimpleData.col(tablaBD, "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
        if (idCol == null || matCol == null) return 0;
        PreparedStatement ps=null; ResultSet rs=null;
        try { ps=ConexionBD.getConnection().prepareStatement("SELECT " + matCol + " FROM " + tablaBD + " WHERE " + idCol + "=?"); ps.setInt(1,id); rs=ps.executeQuery(); if(rs.next()) return rs.getInt(1); } catch(Exception e) { }
        finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
        return 0;
    }

    private void actualizarObservacion() {
        int row = tabla.getSelectedRow();
        if (row < 0) { msg("Seleccione un registro."); return; }
        String obs = SimpleData.nvl(txtObservacion.getText(), "Observación actualizada.");
        int id = Integer.parseInt(String.valueOf(modelo.getValueAt(row, 0)));
        String fuente = String.valueOf(modelo.getValueAt(row, 1));
        LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>();
        datos.put("OBSERVACION", obs); datos.put("OBSERVACIONES", obs);
        boolean ok;
        if ("HORAS".equals(fuente)) ok = DBHelper.actualizar("HORAS_PRACTICA", SimpleData.col("HORAS_PRACTICA", "ID_HORAS_PRACTICA", "ID_HORA"), Integer.valueOf(id), datos);
        else ok = DBHelper.actualizar("REGISTRO_ACTIVIDAD", SimpleData.col("REGISTRO_ACTIVIDAD", "ID_REGISTRO", "ID_ACTIVIDAD"), Integer.valueOf(id), datos);
        if (ok) { lblResumen.setText("Observación actualizada."); cargarTabla(); } else msg("No se pudo actualizar.");
    }

    private void eliminarSeleccionada() {
        int row = tabla.getSelectedRow();
        if (row < 0) { msg("Seleccione un registro."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el registro seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        int id = Integer.parseInt(String.valueOf(modelo.getValueAt(row, 0)));
        String fuente = String.valueOf(modelo.getValueAt(row, 1));
        boolean ok;
        if ("HORAS".equals(fuente)) ok = DBHelper.eliminarLogico("HORAS_PRACTICA", SimpleData.col("HORAS_PRACTICA", "ID_HORAS_PRACTICA", "ID_HORA"), Integer.valueOf(id));
        else ok = DBHelper.eliminarLogico("REGISTRO_ACTIVIDAD", SimpleData.col("REGISTRO_ACTIVIDAD", "ID_REGISTRO", "ID_ACTIVIDAD"), Integer.valueOf(id));
        if (ok) { lblResumen.setText("Registro eliminado o inactivado."); cargarTabla(); } else msg("No se pudo eliminar.");
    }

    private void cargarObservacionSeleccionada() {
        int row = tabla.getSelectedRow();
        if (row >= 0) {
            txtDescripcion.setText(String.valueOf(modelo.getValueAt(row, 9) == null ? "" : modelo.getValueAt(row, 9)));
            txtObservacion.setText(String.valueOf(modelo.getValueAt(row, 10) == null ? "" : modelo.getValueAt(row, 10)));
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        String estCol = SimpleData.matriculaEstudianteCol();
        if (estCol == null || !DBHelper.existeTabla("MATRICULA_PRACTICA")) { lblResumen.setText("No hay matrículas disponibles."); return; }
        int total = 0;
        total += cargarDesdeRegistroActividad(estCol);
        total += cargarDesdeHorasPractica(estCol);
        ocultarColumnasTecnicas();
        lblResumen.setText("Histórico de horas y actividades | Registros encontrados: " + total + " | Reportadas: " + sumarCol(6) + " | Aprobadas: " + sumarCol(7));
    }

    private int cargarDesdeRegistroActividad(String estCol) {
        if (!DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) return 0;
        String idCol = SimpleData.col("REGISTRO_ACTIVIDAD", "ID_REGISTRO", "ID_ACTIVIDAD");
        String matCol = SimpleData.col("REGISTRO_ACTIVIDAD", "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
        if (idCol == null || matCol == null) return 0;
        String fecha = SimpleData.q("R", "REGISTRO_ACTIVIDAD", "FECHA_ACTIVIDAD", "FECHA_REGISTRO", "FECHA"); if (fecha == null) fecha = "SYSDATE";
        String horas = SimpleData.q("R", "REGISTRO_ACTIVIDAD", "HORAS", "HORAS_REPORTADAS"); if (horas == null) horas = "0";
        String desc = SimpleData.q("R", "REGISTRO_ACTIVIDAD", "DESCRIPCION", "ACTIVIDAD"); if (desc == null) desc = SimpleData.lit("");
        String obs = SimpleData.q("R", "REGISTRO_ACTIVIDAD", "OBSERVACIONES", "OBSERVACION"); if (obs == null) obs = SimpleData.lit("");
        String instJoinExpr = SimpleData.matriculaInstitucionExpr("M", "P");
        String joins = " JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=R." + matCol + " JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA ";
        if (instJoinExpr != null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION=" + instJoinExpr + " ";
        String inst = instJoinExpr == null ? SimpleData.lit("Sin institución") : SimpleData.institucionNombreExpr("I");
        String where = esEstudiante() ? " WHERE M." + estCol + "=" + usuario.getIdUsuario() : " WHERE 1=1";
        if (SimpleData.has("REGISTRO_ACTIVIDAD", "ESTADO")) where += " AND NVL(UPPER(R.ESTADO),'PENDIENTE') <> 'INACTIVO'";
        String sql = "SELECT R." + idCol + ", " + SimpleData.usuarioNombreExpr("U") + ", " + SimpleData.practicaTituloExpr("P") + ", " + inst + ", TO_CHAR(" + fecha + ",'YYYY-MM-DD'), " + horas + ", CASE WHEN UPPER(NVL(R.ESTADO,'PENDIENTE'))='APROBADO' THEN " + horas + " ELSE 0 END, " + SimpleData.estadoExpr("R", "REGISTRO_ACTIVIDAD", "PENDIENTE") + ", " + desc + ", " + obs + " FROM REGISTRO_ACTIVIDAD R " + joins + where + " ORDER BY " + fecha + " DESC";
        return cargarFilas(sql, "ACTIVIDAD");
    }

    private int cargarDesdeHorasPractica(String estCol) {
        if (!DBHelper.existeTabla("HORAS_PRACTICA")) return 0;
        String idCol = SimpleData.col("HORAS_PRACTICA", "ID_HORAS_PRACTICA", "ID_HORA");
        String matCol = SimpleData.col("HORAS_PRACTICA", "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
        if (idCol == null || matCol == null) return 0;
        String fecha = SimpleData.q("H", "HORAS_PRACTICA", "FECHA_REGISTRO", "FECHA", "FECHA_ACTIVIDAD"); if (fecha == null) fecha = "SYSDATE";
        String reportadas = SimpleData.q("H", "HORAS_PRACTICA", "HORAS_REPORTADAS", "HORAS"); if (reportadas == null) reportadas = "0";
        String aprobadas = SimpleData.q("H", "HORAS_PRACTICA", "HORAS_APROBADAS"); if (aprobadas == null) aprobadas = "0";
        String obs = SimpleData.q("H", "HORAS_PRACTICA", "OBSERVACION", "OBSERVACIONES"); if (obs == null) obs = SimpleData.lit("");
        String instJoinExpr = SimpleData.matriculaInstitucionExpr("M", "P");
        String joins = " JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=H." + matCol + " JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA ";
        if (instJoinExpr != null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION=" + instJoinExpr + " ";
        String inst = instJoinExpr == null ? SimpleData.lit("Sin institución") : SimpleData.institucionNombreExpr("I");
        String where = esEstudiante() ? " WHERE M." + estCol + "=" + usuario.getIdUsuario() : " WHERE 1=1";
        if (SimpleData.has("HORAS_PRACTICA", "ESTADO")) where += " AND NVL(UPPER(H.ESTADO),'PENDIENTE') <> 'INACTIVO'";
        String sql = "SELECT H." + idCol + ", " + SimpleData.usuarioNombreExpr("U") + ", " + SimpleData.practicaTituloExpr("P") + ", " + inst + ", TO_CHAR(" + fecha + ",'YYYY-MM-DD'), " + reportadas + ", " + aprobadas + ", " + SimpleData.estadoExpr("H", "HORAS_PRACTICA", "PENDIENTE") + ", " + SimpleData.lit("Registro de horas") + ", " + obs + " FROM HORAS_PRACTICA H " + joins + where + " ORDER BY " + fecha + " DESC";
        return cargarFilas(sql, "HORAS");
    }

    private int cargarFilas(String sql, String fuente) {
        Statement st=null; ResultSet rs=null; int c=0;
        try {
            st=ConexionBD.getConnection().createStatement(); rs=st.executeQuery(sql);
            while(rs.next()) {
                modelo.addRow(new Object[]{rs.getInt(1), fuente, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6), rs.getObject(7), rs.getString(8), rs.getString(9), rs.getString(10)});
                c++;
            }
        } catch(Exception e) { System.err.println("HorasSeguimiento.cargarFilas: " + e.getMessage() + " SQL=" + sql); }
        finally { Utilidades.cerrar(rs); try { if(st!=null) st.close(); } catch(Exception e){} }
        return c;
    }

    private double sumarCol(int col) { double s=0; for(int i=0;i<modelo.getRowCount();i++) s += toDouble(modelo.getValueAt(i,col)); return s; }
    private double toDouble(Object o) { try { return Double.parseDouble(String.valueOf(o==null?"0":o).replace(',', '.')); } catch(Exception e) { return 0; } }
    private void ocultarColumnasTecnicas() { try { for (int c=0;c<2;c++) { tabla.getColumnModel().getColumn(c).setMinWidth(0); tabla.getColumnModel().getColumn(c).setMaxWidth(0); tabla.getColumnModel().getColumn(c).setPreferredWidth(0); } } catch(Exception e){} }

    private void insertarHistorial(int idMatricula, String tipo, String anterior, String nuevo, String obs) {
        try {
            if (!DBHelper.existeTabla("HISTORIAL_REVISION") || idMatricula <= 0) return;
            LinkedHashMap<String,Object> h = new LinkedHashMap<String,Object>();
            String idCol = SimpleData.col("HISTORIAL_REVISION", "ID_HISTORIAL_REVISION", "ID_HISTORIAL", "ID_REVISION");
            if (idCol != null) h.put(idCol, Integer.valueOf(DBHelper.siguienteId("HISTORIAL_REVISION", idCol)));
            h.put("ID_MATRICULA_PRACTICA", idMatricula);
            h.put("ID_MATRICULA", idMatricula);
            h.put("ID_USUARIO_REVISA", usuario.getIdUsuario());
            h.put("ID_USUARIO", usuario.getIdUsuario());
            h.put("TIPO_REVISION", tipo);
            h.put("TIPO", tipo);
            h.put("ESTADO_ANTERIOR", anterior);
            h.put("ESTADO_NUEVO", nuevo);
            h.put("OBSERVACION", obs);
            h.put("OBSERVACIONES", obs);
            h.put("FECHA_REVISION", new java.sql.Date(System.currentTimeMillis()));
            h.put("FECHA", new java.sql.Date(System.currentTimeMillis()));
            DBHelper.insertar("HISTORIAL_REVISION", h);
        } catch (Exception ex) { System.err.println("No se pudo insertar historial: " + ex.getMessage()); }
    }

    private class EstadoColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                String estado = "";
                try { estado = String.valueOf(table.getModel().getValueAt(row, 8)).toUpperCase(); } catch (Exception ex) { }
                if (estado.indexOf("APROBADO") >= 0 || estado.indexOf("FINALIZ") >= 0) c.setBackground(new Color(213, 245, 227));
                else if (estado.indexOf("PEND") >= 0 || estado.indexOf("REGISTR") >= 0) c.setBackground(new Color(252, 243, 207));
                else if (estado.indexOf("RECH") >= 0) c.setBackground(new Color(250, 219, 216));
                else c.setBackground(Color.WHITE);
            }
            return c;
        }
    }

    private void msg(String m) { JOptionPane.showMessageDialog(this, m); }
}
