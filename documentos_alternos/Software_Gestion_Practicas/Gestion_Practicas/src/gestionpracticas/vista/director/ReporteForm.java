package com.gestionpracticas.vista.director;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class ReporteForm extends JFrame {

    private final Usuario usuarioActual;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JTextArea txtComentarios;
    private JComboBox cmbEstado;
    private JButton btnGuardar, btnEditar, btnEliminar, btnNuevo, btnPDF;
    private int idSeleccionado = -1;
    private String idColumn = null;

    public ReporteForm(Usuario usuario) {
        this.usuarioActual = usuario;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Reportes - " + usuarioActual.getNombre());
        setSize(1100, 650);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        principal.setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titulo = new JLabel("MÓDULO DE REPORTES");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(titulo, BorderLayout.WEST);
        principal.add(header, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Título", "Estado", "Fecha"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setBackground(new Color(41, 128, 185));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Reportes registrados"));

        JPanel detalle = new JPanel(new GridBagLayout());
        detalle.setBackground(Color.WHITE);
        detalle.setBorder(BorderFactory.createTitledBorder("Detalle del reporte"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        detalle.add(new JLabel("Título"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtTitulo = new JTextField();
        detalle.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0;
        detalle.add(new JLabel("Estado"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cmbEstado = new JComboBox(new String[]{"PENDIENTE", "APROBADO", "REVISION", "GENERADO"});
        detalle.add(cmbEstado, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTH;
        detalle.add(new JLabel("Descripción"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.45;
        txtDescripcion = new JTextArea(6, 30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        detalle.add(new JScrollPane(txtDescripcion), gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        detalle.add(new JLabel("Comentarios"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.45;
        txtComentarios = new JTextArea(6, 30);
        txtComentarios.setLineWrap(true);
        txtComentarios.setWrapStyleWord(true);
        detalle.add(new JScrollPane(txtComentarios), gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnPDF = new JButton("Exportar PDF");
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnPDF);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        detalle.add(botones, gbc);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabla, detalle);
        split.setDividerLocation(420);
        principal.add(split, BorderLayout.CENTER);
        setContentPane(principal);

        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar(false));
        btnEditar.addActionListener(e -> guardar(true));
        btnEliminar.addActionListener(e -> eliminar());
        btnPDF.addActionListener(e -> exportarPDF());
    }

    private Connection getCon() { return ConexionBD.getConnection(); }

    private boolean existeColumna(String tabla, String columna) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getCon().prepareStatement("SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?");
            ps.setString(1, tabla.toUpperCase());
            ps.setString(2, columna.toUpperCase());
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } finally {
            Utilidades.cerrar(rs); Utilidades.cerrar(ps);
        }
    }

    private String detectarIdColumn() throws SQLException {
        if (idColumn != null) return idColumn;
        String[] posibles = {"ID_REPORTE", "ID", "ID_REP"};
        for (String c : posibles) {
            if (existeColumna("REPORTE", c)) { idColumn = c; return c; }
        }
        idColumn = "ID_REPORTE";
        return idColumn;
    }

    private String colTitulo() throws SQLException {
        if (existeColumna("REPORTE", "TITULO")) return "TITULO";
        if (existeColumna("REPORTE", "NOMBRE")) return "NOMBRE";
        return null;
    }

    private String colDesc() throws SQLException {
        if (existeColumna("REPORTE", "DESCRIPCION")) return "DESCRIPCION";
        if (existeColumna("REPORTE", "DETALLE")) return "DETALLE";
        return null;
    }

    private String colComent() throws SQLException {
        if (existeColumna("REPORTE", "COMENTARIOS")) return "COMENTARIOS";
        if (existeColumna("REPORTE", "OBSERVACIONES")) return "OBSERVACIONES";
        return null;
    }

    private String colEstado() throws SQLException { return existeColumna("REPORTE", "ESTADO") ? "ESTADO" : null; }
    private String colFecha() throws SQLException {
        if (existeColumna("REPORTE", "FECHA")) return "FECHA";
        if (existeColumna("REPORTE", "FECHA_GENERACION")) return "FECHA_GENERACION";
        return null;
    }

    private int siguienteId(String col) throws SQLException {
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            ps = getCon().prepareStatement("SELECT NVL(MAX(" + col + "),0)+1 FROM REPORTE");
            rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 1;
        } finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            String id = detectarIdColumn();
            String titulo = colTitulo();
            String estado = colEstado();
            String fecha = colFecha();
            String sql = "SELECT " + id + " AS ID, " +
                    (titulo != null ? titulo : "''") + " AS TITULO, " +
                    (estado != null ? estado : "''") + " AS ESTADO, " +
                    (fecha != null ? fecha : "SYSDATE") + " AS FECHA FROM REPORTE ORDER BY " + id;
            ps = getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getInt("ID"), rs.getString("TITULO"), rs.getString("ESTADO"), rs.getString("FECHA")});
            }
        } catch (Exception ex) {
            modelo.addRow(new Object[]{1, "Reporte general de prácticas", "PENDIENTE", "Sin conexión"});
        } finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        txtTitulo.setText(String.valueOf(modelo.getValueAt(fila, 1)));
        Object estado = modelo.getValueAt(fila, 2);
        if (estado != null) cmbEstado.setSelectedItem(estado.toString());
    }

    private void guardar(boolean actualizar) {
        if (txtTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el título del reporte"); return;
        }
        PreparedStatement ps = null;
        try {
            String id = detectarIdColumn();
            String titulo = colTitulo();
            String desc = colDesc();
            String com = colComent();
            String estado = colEstado();
            String fecha = colFecha();
            if (titulo == null) throw new SQLException("La tabla REPORTE no tiene columna TITULO o NOMBRE");

            if (!actualizar || idSeleccionado <= 0) {
                ArrayList<String> cols = new ArrayList<String>();
                ArrayList<Object> vals = new ArrayList<Object>();
                cols.add(id); vals.add(Integer.valueOf(siguienteId(id)));
                cols.add(titulo); vals.add(txtTitulo.getText().trim());
                if (desc != null) { cols.add(desc); vals.add(txtDescripcion.getText().trim()); }
                if (com != null) { cols.add(com); vals.add(txtComentarios.getText().trim()); }
                if (estado != null) { cols.add(estado); vals.add(cmbEstado.getSelectedItem().toString()); }
                if (fecha != null) { cols.add(fecha); }
                StringBuilder sql = new StringBuilder("INSERT INTO REPORTE (");
                for (int i=0;i<cols.size();i++){ if(i>0) sql.append(","); sql.append(cols.get(i)); }
                sql.append(") VALUES (");
                int bindCount=0;
                for (int i=0;i<cols.size();i++){
                    if(i>0) sql.append(",");
                    if (cols.get(i).equals(fecha)) sql.append("SYSDATE"); else { sql.append("?"); bindCount++; }
                }
                sql.append(")");
                ps = getCon().prepareStatement(sql.toString());
                int idx=1;
                for (int i=0;i<cols.size();i++) if (!cols.get(i).equals(fecha)) ps.setObject(idx++, vals.get(i));
            } else {
                ArrayList<String> sets = new ArrayList<String>();
                sets.add(titulo + " = ?");
                if (desc != null) sets.add(desc + " = ?");
                if (com != null) sets.add(com + " = ?");
                if (estado != null) sets.add(estado + " = ?");
                String sql = "UPDATE REPORTE SET " + join(sets, ", ") + " WHERE " + id + " = ?";
                ps = getCon().prepareStatement(sql);
                int idx=1;
                ps.setString(idx++, txtTitulo.getText().trim());
                if (desc != null) ps.setString(idx++, txtDescripcion.getText().trim());
                if (com != null) ps.setString(idx++, txtComentarios.getText().trim());
                if (estado != null) ps.setString(idx++, cmbEstado.getSelectedItem().toString());
                ps.setInt(idx, idSeleccionado);
            }
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Reporte guardado correctamente");
                limpiar(); cargarTabla();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el reporte:\n" + ex.getMessage());
        } finally { Utilidades.cerrar(ps); }
    }

    private static String join(java.util.List<String> l, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<l.size();i++){ if(i>0) sb.append(sep); sb.append(l.get(i)); }
        return sb.toString();
    }

    private void eliminar() {
        if (idSeleccionado <= 0) { JOptionPane.showMessageDialog(this, "Seleccione un reporte"); return; }
        PreparedStatement ps = null;
        try {
            String id = detectarIdColumn();
            ps = getCon().prepareStatement("DELETE FROM REPORTE WHERE " + id + " = ?");
            ps.setInt(1, idSeleccionado);
            if (ps.executeUpdate() > 0) { JOptionPane.showMessageDialog(this, "Reporte eliminado"); limpiar(); cargarTabla(); }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar:\n" + ex.getMessage());
        } finally { Utilidades.cerrar(ps); }
    }

    private void exportarPDF() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar PDF");
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String ruta = chooser.getSelectedFile().getAbsolutePath();
                if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";
                com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
                com.itextpdf.text.pdf.PdfWriter.getInstance(documento, new java.io.FileOutputStream(ruta));
                documento.open();
                documento.add(new com.itextpdf.text.Paragraph("REPORTE - GESTIÓN DE PRÁCTICAS ACADÉMICAS"));
                documento.add(new com.itextpdf.text.Paragraph(" "));
                documento.add(new com.itextpdf.text.Paragraph("Título: " + txtTitulo.getText()));
                documento.add(new com.itextpdf.text.Paragraph("Estado: " + cmbEstado.getSelectedItem()));
                documento.add(new com.itextpdf.text.Paragraph("Descripción: " + txtDescripcion.getText()));
                documento.add(new com.itextpdf.text.Paragraph("Comentarios: " + txtComentarios.getText()));
                documento.close();
                JOptionPane.showMessageDialog(this, "PDF generado correctamente");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generando PDF:\n" + ex.getMessage());
        }
    }

    private void limpiar() {
        idSeleccionado = -1;
        tabla.clearSelection();
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtComentarios.setText("");
        cmbEstado.setSelectedIndex(0);
    }
}
