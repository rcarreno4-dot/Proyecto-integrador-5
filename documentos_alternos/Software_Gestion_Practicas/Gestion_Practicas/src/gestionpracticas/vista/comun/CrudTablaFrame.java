package com.gestionpracticas.vista.comun;

import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.PantallaUtil;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class CrudTablaFrame extends JFrame {

    private String tabla;
    private String idCol;
    private String[] columnas;
    private JTable table;
    private DefaultTableModel modelo;
    private LinkedHashMap<String, JTextField> campos = new LinkedHashMap<String, JTextField>();
    private Integer idSeleccionado = null;

    public CrudTablaFrame(String titulo, String tabla, String idCol, String[] columnasDeseadas) {
        this.tabla = tabla.toUpperCase();
        this.idCol = resolverIdCol(idCol);
        this.columnas = filtrarColumnas(columnasDeseadas);
        init(titulo);
        cargarTabla();
    }

    private String resolverIdCol(String propuesto) {
        String p = propuesto == null ? null : propuesto.toUpperCase();
        if (p != null && DBHelper.tieneColumna(tabla, p)) return p;
        String c1 = DBHelper.primeraColumna(tabla, "ID_" + tabla, "ID");
        if (c1 != null) return c1;
        java.util.Iterator<String> it = DBHelper.columnas(tabla).iterator();
        while (it.hasNext()) {
            String c = it.next();
            if (c.startsWith("ID")) return c;
        }
        return p != null ? p : "ID";
    }

    private String[] filtrarColumnas(String[] deseadas) {
        ArrayList<String> lista = new ArrayList<String>();
        if (DBHelper.tieneColumna(tabla, idCol)) lista.add(idCol);
        for (int i = 0; i < deseadas.length; i++) {
            String c = deseadas[i].toUpperCase();
            if (!c.equals(idCol) && DBHelper.tieneColumna(tabla, c)) lista.add(c);
        }
        if (lista.size() == 0) lista.add(idCol);
        return lista.toArray(new String[lista.size()]);
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }

    private void init(String titulo) {
        setTitle(titulo + " - CRUD");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.setBackground(new Color(245, 247, 250));

        JLabel header = new JLabel(titulo + "  |  Tabla: " + tabla);
        header.setOpaque(true);
        header.setBackground(new Color(33, 97, 140));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        root.add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(390);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder("Formulario de registro / edición"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        for (int i = 0; i < columnas.length; i++) {
            String c = columnas[i];
            if (c.equals(idCol)) continue;
            JLabel l = new JLabel(c.replace('_', ' '));
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            gbc.gridy++;
            form.add(l, gbc);
            JTextField txt = new JTextField();
            txt.setPreferredSize(new Dimension(320, 30));
            campos.put(c, txt);
            gbc.gridy++;
            form.add(txt, gbc);
        }

        JPanel btns = new JPanel(new GridLayout(3, 2, 8, 8));
        btns.setBackground(Color.WHITE);
        JButton guardar = boton("Guardar / Actualizar", new Color(39, 174, 96));
        JButton nuevo = boton("Nuevo", new Color(52, 152, 219));
        JButton activar = boton("Activar", new Color(22, 160, 133));
        JButton inactivar = boton("Inactivar", new Color(243, 156, 18));
        JButton eliminar = boton("Eliminar definitivo", new Color(192, 57, 43));
        JButton refrescar = boton("Refrescar", new Color(52, 73, 94));
        btns.add(guardar); btns.add(nuevo); btns.add(activar); btns.add(inactivar); btns.add(eliminar); btns.add(refrescar);
        gbc.gridy++;
        form.add(btns, gbc);

        guardar.addActionListener(e -> guardar());
        nuevo.addActionListener(e -> limpiar());
        activar.addActionListener(e -> cambiarEstado("ACTIVO"));
        inactivar.addActionListener(e -> cambiarEstado("INACTIVO"));
        eliminar.addActionListener(e -> eliminarDefinitivo());
        refrescar.addActionListener(e -> cargarTabla());

        split.setLeftComponent(new JScrollPane(form));

        modelo = new DefaultTableModel(columnas, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(modelo);
        table.setRowHeight(26);
        table.getTableHeader().setBackground(new Color(33, 97, 140));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) cargarSeleccion(); });
        split.setRightComponent(new JScrollPane(table));

        root.add(split, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        Statement st = null;
        ResultSet rs = null;
        try {
            if (!DBHelper.existeTabla(tabla)) {
                JOptionPane.showMessageDialog(this, "No existe la tabla " + tabla);
                return;
            }
            StringBuilder sql = new StringBuilder("SELECT ");
            for (int i = 0; i < columnas.length; i++) {
                if (i > 0) sql.append(", ");
                sql.append(columnas[i]);
            }
            sql.append(" FROM ").append(tabla);
            if (DBHelper.tieneColumna(tabla, "ESTADO")) sql.append(" WHERE NVL(UPPER(ESTADO),'ACTIVO') <> 'ELIMINADO'");
            sql.append(" ORDER BY ").append(DBHelper.ordenEstadoSql(tabla, columnas[0]));
            st = DBHelper.con().createStatement();
            rs = st.executeQuery(sql.toString());
            while (rs.next()) {
                Object[] fila = new Object[columnas.length];
                for (int i = 0; i < columnas.length; i++) fila[i] = rs.getObject(columnas[i]);
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando tabla: " + e.getMessage());
        } finally {
            Utilidades.cerrar(rs); try { if (st != null) st.close(); } catch (Exception ex) { }
        }
    }

    private void cargarSeleccion() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Object id = modelo.getValueAt(row, 0);
        try { idSeleccionado = Integer.valueOf(String.valueOf(id)); } catch (Exception ex) { idSeleccionado = null; }
        for (int i = 1; i < columnas.length; i++) {
            JTextField txt = campos.get(columnas[i]);
            if (txt != null) {
                Object val = modelo.getValueAt(row, i);
                txt.setText(val == null ? "" : String.valueOf(val));
            }
        }
    }

    private void guardar() {
        LinkedHashMap<String,Object> datos = new LinkedHashMap<String,Object>();
        Iterator<Map.Entry<String,JTextField>> it = campos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,JTextField> e = it.next();
            String val = e.getValue().getText().trim();
            if (val.length() == 0) continue;
            datos.put(e.getKey(), DBHelper.convertirValor(tabla, e.getKey(), val));
        }
        if (DBHelper.tieneColumna(tabla, "ESTADO") && !datos.containsKey("ESTADO")) datos.put("ESTADO", "ACTIVO");
        boolean ok;
        if (idSeleccionado == null) {
            try { datos.put(idCol, Integer.valueOf(DBHelper.siguienteId(tabla, idCol))); } catch (Exception e) { }
            ok = DBHelper.insertar(tabla, datos);
        } else {
            ok = DBHelper.actualizar(tabla, idCol, idSeleccionado, datos);
        }
        if (ok) {
            JOptionPane.showMessageDialog(this, "Registro guardado correctamente.");
            limpiar(); cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar. Revise columnas obligatorias o permisos del usuario Oracle.");
        }
    }

    private void cambiarEstado(String estado) {
        if (idSeleccionado == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro."); return; }
        if (!DBHelper.tieneColumna(tabla, "ESTADO")) { JOptionPane.showMessageDialog(this, "La tabla no tiene columna ESTADO."); return; }
        boolean ok = DBHelper.cambiarEstado(tabla, idCol, idSeleccionado, estado);
        if (ok) { JOptionPane.showMessageDialog(this, "Estado actualizado a " + estado + "."); limpiar(); cargarTabla(); }
        else JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado. Revise restricciones CHECK o permisos.");
    }

    private void eliminarDefinitivo() {
        if (idSeleccionado == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro."); return; }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar DEFINITIVAMENTE el registro seleccionado?\nSi tiene relaciones en otras tablas Oracle puede impedir la eliminación.", "Confirmar eliminación definitiva", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            boolean ok = DBHelper.borrar(tabla, idCol, idSeleccionado);
            if (ok) { JOptionPane.showMessageDialog(this, "Registro eliminado definitivamente."); limpiar(); cargarTabla(); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar definitivamente. Puede tener datos relacionados; use Inactivar.");
        }
    }

    private void limpiar() {
        idSeleccionado = null;
        Iterator<JTextField> it = campos.values().iterator();
        while (it.hasNext()) it.next().setText("");
        table.clearSelection();
    }
}
