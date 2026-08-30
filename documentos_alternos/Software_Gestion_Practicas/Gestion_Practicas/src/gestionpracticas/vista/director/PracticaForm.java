package com.gestionpracticas.vista.director;

import com.gestionpracticas.logica.PracticaLogica;
import com.gestionpracticas.logica.ProgramaLogica;
import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.modelo.Programa;
import com.gestionpracticas.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PracticaForm extends JFrame {

    private Usuario usuarioActual;
    private PracticaLogica practicaLogica;
    private ProgramaLogica programaLogica;

    private JTextField txtTitulo;
    private JTextField txtDescripcion;
    private JTextField txtHoras;
    private JTextField txtBuscar;

    private JComboBox cmbTipo;
    private JComboBox cmbPrograma;

    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnGuardar;
    private JButton btnActivar;
    private JButton btnInactivar;
    private JButton btnEliminar;
    private JButton btnNuevo;

    private int idSeleccionado = -1;

    public PracticaForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.practicaLogica = new PracticaLogica();
        this.programaLogica = new ProgramaLogica();

        initComponents();
        cargarProgramas();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("GESTIÓN DE PRÁCTICAS");
        setSize(1150, 680);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(240, 240, 240));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 97, 140));
        header.setPreferredSize(new Dimension(100, 65));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblTitulo = new JLabel("GESTIÓN DE PRÁCTICAS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblRol = new JLabel("Director: " + (usuarioActual != null ? usuarioActual.getNombre() : "Administrador"));
        lblRol.setForeground(Color.WHITE);
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        header.add(lblTitulo, BorderLayout.CENTER);
        header.add(lblRol, BorderLayout.EAST);
        principal.add(header, BorderLayout.NORTH);

        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setPreferredSize(new Dimension(360, 565));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder("Registrar / actualizar práctica"));

        JLabel lPrograma = new JLabel("Programa académico");
        lPrograma.setBounds(20, 25, 160, 25);
        panelForm.add(lPrograma);

        cmbPrograma = new JComboBox();
        cmbPrograma.setBounds(20, 50, 300, 30);
        panelForm.add(cmbPrograma);

        JLabel l1 = new JLabel("Título");
        l1.setBounds(20, 90, 120, 25);
        panelForm.add(l1);

        txtTitulo = new JTextField();
        txtTitulo.setBounds(20, 115, 300, 30);
        panelForm.add(txtTitulo);

        JLabel l2 = new JLabel("Descripción");
        l2.setBounds(20, 155, 120, 25);
        panelForm.add(l2);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(20, 180, 300, 30);
        panelForm.add(txtDescripcion);

        JLabel l3 = new JLabel("Horas requeridas");
        l3.setBounds(20, 220, 150, 25);
        panelForm.add(l3);

        txtHoras = new JTextField();
        txtHoras.setBounds(20, 245, 300, 30);
        panelForm.add(txtHoras);

        JLabel l4 = new JLabel("Tipo de práctica");
        l4.setBounds(20, 285, 150, 25);
        panelForm.add(l4);

        cmbTipo = new JComboBox();
        cmbTipo.addItem("EMPRESARIAL");
        cmbTipo.addItem("SOCIAL");
        cmbTipo.addItem("INVESTIGACION");
        cmbTipo.addItem("COMUNITARIA");
        cmbTipo.setBounds(20, 310, 300, 30);
        panelForm.add(cmbTipo);

        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setBounds(20, 365, 135, 40);
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        panelForm.add(btnGuardar);

        btnNuevo = new JButton("NUEVO");
        btnNuevo.setBounds(185, 365, 135, 40);
        btnNuevo.setBackground(new Color(52, 152, 219));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFocusPainted(false);
        panelForm.add(btnNuevo);

        btnActivar = new JButton("ACTIVAR");
        btnActivar.setBounds(20, 420, 95, 38);
        btnActivar.setBackground(new Color(39, 174, 96));
        btnActivar.setForeground(Color.WHITE);
        btnActivar.setFocusPainted(false);
        panelForm.add(btnActivar);

        btnInactivar = new JButton("INACTIVAR");
        btnInactivar.setBounds(125, 420, 100, 38);
        btnInactivar.setBackground(new Color(230, 126, 34));
        btnInactivar.setForeground(Color.WHITE);
        btnInactivar.setFocusPainted(false);
        panelForm.add(btnInactivar);

        btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setBounds(235, 420, 85, 38);
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        panelForm.add(btnEliminar);

        JTextArea ayuda = new JTextArea("Nota: ACTIVAR cambia el estado a ACTIVO. INACTIVAR conserva el registro con estado INACTIVO. ELIMINAR borra definitivamente si no tiene datos relacionados.");
        ayuda.setLineWrap(true);
        ayuda.setWrapStyleWord(true);
        ayuda.setEditable(false);
        ayuda.setOpaque(false);
        ayuda.setForeground(Color.GRAY);
        ayuda.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        ayuda.setBounds(20, 475, 305, 65);
        panelForm.add(ayuda);

        principal.add(panelForm, BorderLayout.WEST);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField(25);
        JButton btnBuscar = new JButton("Buscar");
        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelTabla.add(panelBuscar, BorderLayout.NORTH);

        modelo = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.addColumn("ID");
        modelo.addColumn("TÍTULO");
        modelo.addColumn("DESCRIPCIÓN");
        modelo.addColumn("HORAS");
        modelo.addColumn("TIPO");
        modelo.addColumn("ESTADO");
        modelo.addColumn("PROGRAMA");
        modelo.addColumn("ID_PROGRAMA");

        tabla = new JTable(modelo);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setBackground(new Color(33, 97, 140));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Prácticas registradas en la base de datos"));
        panelTabla.add(scroll, BorderLayout.CENTER);
        principal.add(panelTabla, BorderLayout.CENTER);

        add(principal);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { guardar(); }
        });

        btnNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { limpiar(); }
        });

        btnActivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { activar(); }
        });

        btnInactivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { inactivar(); }
        });

        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { eliminarDefinitivo(); }
        });

        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { buscar(); }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                    txtTitulo.setText(valorCelda(fila, 1));
                    txtDescripcion.setText(valorCelda(fila, 2));
                    txtHoras.setText(valorCelda(fila, 3));
                    cmbTipo.setSelectedItem(valorCelda(fila, 4));
                    Object idProg = modelo.getValueAt(fila, 7);
                    if (idProg != null && !idProg.toString().trim().equals("")) {
                        seleccionarProgramaPorId(Integer.parseInt(idProg.toString()));
                    }
                    btnGuardar.setText("ACTUALIZAR");
                }
            }
        });
    }

    private String valorCelda(int fila, int columna) {
        Object v = modelo.getValueAt(fila, columna);
        return v == null ? "" : v.toString();
    }

    private void ocultarColumnaIdPrograma() {
        try {
            tabla.getColumnModel().getColumn(7).setMinWidth(0);
            tabla.getColumnModel().getColumn(7).setMaxWidth(0);
            tabla.getColumnModel().getColumn(7).setPreferredWidth(0);
        } catch (Exception e) {
            // no hacer nada
        }
    }

    private void cargarProgramas() {
        cmbPrograma.removeAllItems();
        try {
            List programas = programaLogica.obtenerActivos();
            for (int i = 0; i < programas.size(); i++) {
                cmbPrograma.addItem(programas.get(i));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando programas: " + e.getMessage());
        }
    }

    private void seleccionarProgramaPorId(int idPrograma) {
        for (int i = 0; i < cmbPrograma.getItemCount(); i++) {
            Object item = cmbPrograma.getItemAt(i);
            if (item instanceof Programa) {
                Programa p = (Programa) item;
                if (p.getIdPrograma() == idPrograma) {
                    cmbPrograma.setSelectedIndex(i);
                    return;
                }
            }
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try {
            List lista = practicaLogica.obtenerTodas();
            for (int i = 0; i < lista.size(); i++) {
                Practica p = (Practica) lista.get(i);
                Programa prog = p.getPrograma();
                modelo.addRow(new Object[]{
                    Integer.valueOf(p.getIdPractica()),
                    p.getTitulo(),
                    p.getDescripcion(),
                    Integer.valueOf(p.getHorasRequeridas()),
                    p.getTipoPractica(),
                    p.getEstado(),
                    prog != null ? prog.getNombre() : "Sin programa",
                    prog != null ? Integer.valueOf(prog.getIdPrograma()) : null
                });
            }
            ocultarColumnaIdPrograma();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando tabla: " + e.getMessage());
        }
    }

    private void guardar() {
        try {
            if (cmbPrograma.getItemCount() == 0 || cmbPrograma.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Primero registre un programa académico en el módulo Programas.");
                return;
            }
            if (txtTitulo.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese el título de la práctica.");
                txtTitulo.requestFocus();
                return;
            }
            if (txtDescripcion.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese la descripción de la práctica.");
                txtDescripcion.requestFocus();
                return;
            }
            int horas = Integer.parseInt(txtHoras.getText().trim());
            if (horas <= 0) {
                JOptionPane.showMessageDialog(this, "Las horas deben ser mayores a cero.");
                txtHoras.requestFocus();
                return;
            }

            Practica p = new Practica();
            p.setPrograma((Programa) cmbPrograma.getSelectedItem());
            p.setTitulo(txtTitulo.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setHorasRequeridas(horas);
            p.setTipoPractica(cmbTipo.getSelectedItem().toString());
            p.setEstado("ACTIVO");

            boolean ok;
            if (idSeleccionado == -1) {
                ok = practicaLogica.guardar(p);
            } else {
                p.setIdPractica(idSeleccionado);
                ok = practicaLogica.actualizar(p);
            }

            if (ok) {
                JOptionPane.showMessageDialog(this, "Práctica guardada correctamente en la base de datos.");
                limpiar();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar. Revisa la consola de NetBeans para ver el error SQL.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Las horas deben ser un número válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void activar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una práctica de la tabla.");
            return;
        }

        int op = JOptionPane.showConfirmDialog(
            this,
            "¿Desea activar esta práctica?\nEl estado cambiará a ACTIVO en la base de datos.",
            "Confirmar activación",
            JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {
            boolean ok = practicaLogica.activar(idSeleccionado);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Práctica activada correctamente.");
                limpiar();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo activar la práctica.");
            }
        }
    }

    private void inactivar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una práctica de la tabla.");
            return;
        }

        int op = JOptionPane.showConfirmDialog(
            this,
            "¿Desea inactivar esta práctica?\nLa práctica seguirá en la base de datos con estado INACTIVO.",
            "Confirmar inactivación",
            JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {
            boolean ok = practicaLogica.inactivar(idSeleccionado);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Práctica inactivada correctamente.");
                limpiar();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo inactivar la práctica.");
            }
        }
    }

    private void eliminarDefinitivo() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una práctica de la tabla.");
            return;
        }

        int op = JOptionPane.showConfirmDialog(
            this,
            "¿Desea eliminar definitivamente esta práctica?\nEsta acción borra el registro de la base de datos y no se puede deshacer.",
            "Confirmar eliminación definitiva",
            JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {
            boolean ok = practicaLogica.eliminarDefinitivo(idSeleccionado);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Práctica eliminada definitivamente.");
                limpiar();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar. Puede que la práctica tenga matrículas, evaluaciones, grupos, horas o registros asociados.\nUse INACTIVAR para conservar la trazabilidad."
                );
            }
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        modelo.setRowCount(0);
        List lista = practicaLogica.obtenerTodas();
        for (int i = 0; i < lista.size(); i++) {
            Practica p = (Practica) lista.get(i);
            Programa prog = p.getPrograma();
            String titulo = p.getTitulo() == null ? "" : p.getTitulo().toLowerCase();
            String nombrePrograma = prog == null || prog.getNombre() == null ? "" : prog.getNombre().toLowerCase();
            String estado = p.getEstado() == null ? "" : p.getEstado().toLowerCase();
            if (titulo.indexOf(texto) >= 0 || nombrePrograma.indexOf(texto) >= 0 || estado.indexOf(texto) >= 0) {
                modelo.addRow(new Object[]{
                    Integer.valueOf(p.getIdPractica()),
                    p.getTitulo(),
                    p.getDescripcion(),
                    Integer.valueOf(p.getHorasRequeridas()),
                    p.getTipoPractica(),
                    p.getEstado(),
                    prog != null ? prog.getNombre() : "Sin programa",
                    prog != null ? Integer.valueOf(prog.getIdPrograma()) : null
                });
            }
        }
        ocultarColumnaIdPrograma();
    }

    private void limpiar() {
        idSeleccionado = -1;
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtHoras.setText("");
        cmbTipo.setSelectedIndex(0);
        if (cmbPrograma.getItemCount() > 0) {
            cmbPrograma.setSelectedIndex(0);
        }
        btnGuardar.setText("GUARDAR");
        cargarProgramas();
    }                           
}
