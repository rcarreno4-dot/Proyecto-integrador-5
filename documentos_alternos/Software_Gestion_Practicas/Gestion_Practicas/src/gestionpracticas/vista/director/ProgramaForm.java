package com.gestionpracticas.vista.director;

import com.gestionpracticas.vista.director.ProgramaForm;
import com.gestionpracticas.logica.ProgramaLogica;
import com.gestionpracticas.modelo.Programa;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProgramaForm extends JFrame {

    private final ProgramaLogica programaLogica;
    private final Usuario usuarioActual;

    // Componentes del formulario
    private JTextField txtNombre, txtCodigo, txtDescripcion, txtFacultad, txtDuracion, txtBuscar;
    private JComboBox cmbEstado;
    private JButton btnGuardar, btnEliminar, btnRefrescar, btnBuscar, btnConfigPantalla;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JSplitPane split;
    private JPanel mainPanel;

    private int idSeleccionado = -1;

    public ProgramaForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.programaLogica = new ProgramaLogica();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Programas - " + usuarioActual.getNombre());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        getContentPane().setBackground(new Color(240, 242, 245));

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));

        JPanel header = crearHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(420);
        split.setResizeWeight(0.35);

        JPanel formPanel = crearPanelFormulario();
        split.setLeftComponent(formPanel);

        JPanel tablaPanel = crearPanelTabla();
        split.setRightComponent(tablaPanel);

        mainPanel.add(split, BorderLayout.CENTER);

        JPanel footer = crearFooter();
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 97, 140));
        header.setPreferredSize(new Dimension(1200, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel(" GESTIÓN DE PROGRAMAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel(" Director: " + usuarioActual.getNombre());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(new Color(220, 220, 220));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblUsuario, BorderLayout.EAST);

        return header;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTituloForm = new JLabel(" REGISTRAR NUEVO PROGRAMA");
        lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloForm.setForeground(new Color(33, 97, 140));
        lblTituloForm.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloForm.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 11);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);
        Dimension fieldSize = new Dimension(300, 32);

        // Nombre
        gbc.gridy = 0;
        JLabel lblNombre = new JLabel("NOMBRE DEL PROGRAMA");
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblNombre, gbc);

        gbc.gridy = 1;
        txtNombre = new JTextField();
        txtNombre.setFont(fieldFont);
        txtNombre.setPreferredSize(fieldSize);
        txtNombre.setBorder(crearBordeCampo());
        camposPanel.add(txtNombre, gbc);

        // Código
        gbc.gridy = 2;
        JLabel lblCodigo = new JLabel("CÓDIGO");
        lblCodigo.setFont(labelFont);
        lblCodigo.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblCodigo, gbc);

        gbc.gridy = 3;
        txtCodigo = new JTextField();
        txtCodigo.setFont(fieldFont);
        txtCodigo.setPreferredSize(fieldSize);
        txtCodigo.setBorder(crearBordeCampo());
        camposPanel.add(txtCodigo, gbc);

        // Descripción
        gbc.gridy = 4;
        JLabel lblDescripcion = new JLabel("DESCRIPCIÓN");
        lblDescripcion.setFont(labelFont);
        lblDescripcion.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblDescripcion, gbc);

        gbc.gridy = 5;
        txtDescripcion = new JTextField();
        txtDescripcion.setFont(fieldFont);
        txtDescripcion.setPreferredSize(fieldSize);
        txtDescripcion.setBorder(crearBordeCampo());
        camposPanel.add(txtDescripcion, gbc);

        // Facultad
        gbc.gridy = 6;
        JLabel lblFacultad = new JLabel("FACULTAD");
        lblFacultad.setFont(labelFont);
        lblFacultad.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblFacultad, gbc);

        gbc.gridy = 7;
        txtFacultad = new JTextField();
        txtFacultad.setFont(fieldFont);
        txtFacultad.setPreferredSize(fieldSize);
        txtFacultad.setBorder(crearBordeCampo());
        camposPanel.add(txtFacultad, gbc);

        // Duración
        gbc.gridy = 8;
        JLabel lblDuracion = new JLabel("DURACIÓN (SEMESTRES)");
        lblDuracion.setFont(labelFont);
        lblDuracion.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblDuracion, gbc);

        gbc.gridy = 9;
        txtDuracion = new JTextField();
        txtDuracion.setFont(fieldFont);
        txtDuracion.setPreferredSize(fieldSize);
        txtDuracion.setBorder(crearBordeCampo());
        camposPanel.add(txtDuracion, gbc);

        // Estado
        gbc.gridy = 10;
        JLabel lblEstado = new JLabel("ESTADO");
        lblEstado.setFont(labelFont);
        lblEstado.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblEstado, gbc);

        gbc.gridy = 11;
        cmbEstado = new JComboBox(new String[]{Utilidades.ESTADO_ACTIVO, Utilidades.ESTADO_INACTIVO});
        cmbEstado.setFont(fieldFont);
        cmbEstado.setPreferredSize(fieldSize);
        cmbEstado.setBackground(Color.WHITE);
        cmbEstado.setBorder(crearBordeCampo());
        camposPanel.add(cmbEstado, gbc);

        // Botones del formulario
        JPanel botonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        botonesForm.setBackground(Color.WHITE);

        btnGuardar = new JButton(" Guardar");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(110, 35));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });

        btnRefrescar = new JButton(" Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setPreferredSize(new Dimension(110, 35));
        btnRefrescar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
                cargarTabla();
            }
        });

        botonesForm.add(btnGuardar);
        botonesForm.add(btnRefrescar);

        gbc.gridy = 12;
        gbc.insets = new Insets(15, 5, 5, 5);
        camposPanel.add(botonesForm, gbc);

        panel.add(lblTituloForm, BorderLayout.NORTH);
        panel.add(camposPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTablaTitulo = new JLabel(" LISTA DE PROGRAMAS REGISTRADOS");
        lblTablaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTablaTitulo.setForeground(new Color(33, 97, 140));
        lblTablaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel(" Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        txtBuscar = new JTextField(15);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscar.setPreferredSize(new Dimension(150, 28));

        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setPreferredSize(new Dimension(80, 28));
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        // Columnas de la tabla
        String[] cols = {"ID", "Nombre", "Código", "Facultad", "Duración", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(33, 97, 140));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(52, 152, 219, 50));
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(4).setMaxWidth(80);
        tabla.getColumnModel().getColumn(5).setMaxWidth(80);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editarItem = new JMenuItem(" Editar Programa");
        JMenuItem eliminarItem = new JMenuItem(" Eliminar Programa");
        popupMenu.add(editarItem);
        popupMenu.add(eliminarItem);

        tabla.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tabla.rowAtPoint(e.getPoint());
                    tabla.setRowSelectionInterval(row, row);
                    popupMenu.show(tabla, e.getX(), e.getY());
                }
                if (e.getClickCount() == 2) {
                    cargarProgramaSeleccionado();
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tabla.rowAtPoint(e.getPoint());
                    tabla.setRowSelectionInterval(row, row);
                    popupMenu.show(tabla, e.getX(), e.getY());
                }
            }
        });

        editarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarProgramaSeleccionado();
            }
        });

        eliminarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Programas"));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelSur.setBackground(Color.WHITE);

        btnEliminar = new JButton(" Eliminar Seleccionado");
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(140, 30));
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        panelSur.add(btnEliminar);

        panel.add(lblTablaTitulo, BorderLayout.NORTH);
        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelSur, BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                btnEliminar.setEnabled(tabla.getSelectedRow() >= 0);
            }
        });

        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(240, 242, 245));
        footer.setPreferredSize(new Dimension(1200, 40));
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblInfo = new JLabel("💡 Los programas académicos pueden tener una duración de hasta 12 semestres");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);

        JPanel panelConfig = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelConfig.setBackground(new Color(240, 242, 245));

        btnConfigPantalla = new JButton("🖥️ Resolución");
        btnConfigPantalla.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnConfigPantalla.setBackground(new Color(52, 73, 94));
        btnConfigPantalla.setForeground(Color.WHITE);
        btnConfigPantalla.setFocusPainted(false);
        btnConfigPantalla.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfigPantalla.setPreferredSize(new Dimension(100, 28));
        btnConfigPantalla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configurarResolucion();
            }
        });

        panelConfig.add(btnConfigPantalla);

        footer.add(lblInfo, BorderLayout.WEST);
        footer.add(panelConfig, BorderLayout.EAST);

        return footer;
    }

    private Border crearBordeCampo() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );
    }

    private void configurarResolucion() {
        String[] resoluciones = {"16:9 (1920x1080)", "16:10 (1920x1200)", "4:3 (1024x768)", "16:9 (1366x768)"};
        String[] valores = {"1920x1080", "1920x1200", "1024x768", "1366x768"};

        JComboBox combo = new JComboBox(resoluciones);
        int result = JOptionPane.showConfirmDialog(this, combo, "🖥️ Configurar Resolución",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int idx = combo.getSelectedIndex();
            String[] dimensiones = valores[idx].split("x");
            int ancho = Integer.parseInt(dimensiones[0]);
            int alto = Integer.parseInt(dimensiones[1]);

            setSize(ancho, alto);
            setLocationRelativeTo(null);

            double proporcion = (double) ancho / 1200;
            int nuevoDivider = (int) (420 * proporcion);
            split.setDividerLocation(nuevoDivider);

            JOptionPane.showMessageDialog(this, "Pantalla configurada a " + resoluciones[idx]);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List programas = programaLogica.obtenerTodos();
            if (programas != null) {
                for (int i = 0; i < programas.size(); i++) {
                    Programa p = (Programa) programas.get(i);
                    modeloTabla.addRow(new Object[]{
                            Integer.valueOf(p.getIdPrograma()),
                            p.getNombre(),
                            p.getCodigo(),
                            p.getFacultad(),
                            Integer.valueOf(p.getDuracionSemestres()),
                            p.getEstado()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando tabla: " + e.getMessage());
        }
    }

    private void buscar() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        modeloTabla.setRowCount(0);
        try {
            List programas = programaLogica.obtenerTodos();
            if (programas != null) {
                for (int i = 0; i < programas.size(); i++) {
                    Programa p = (Programa) programas.get(i);
                    if (p.getNombre().toLowerCase().indexOf(busqueda) >= 0 ||
                        p.getCodigo().toLowerCase().indexOf(busqueda) >= 0) {
                        modeloTabla.addRow(new Object[]{
                                Integer.valueOf(p.getIdPrograma()),
                                p.getNombre(),
                                p.getCodigo(),
                                p.getFacultad(),
                                Integer.valueOf(p.getDuracionSemestres()),
                                p.getEstado()
                        });
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en búsqueda: " + e.getMessage());
        }
    }

    private void cargarProgramaSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
            txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
            txtCodigo.setText((String) modeloTabla.getValueAt(fila, 2));
            txtFacultad.setText((String) modeloTabla.getValueAt(fila, 3));
            txtDuracion.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
            cmbEstado.setSelectedItem(modeloTabla.getValueAt(fila, 5));
            btnGuardar.setText(" Actualizar");
        }
    }

    private void guardar() {
        try {
            if (txtNombre.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre del programa.");
                txtNombre.requestFocus();
                return;
            }
            if (txtCodigo.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese el código del programa.");
                txtCodigo.requestFocus();
                return;
            }
            if (txtDuracion.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese la duración en semestres.");
                txtDuracion.requestFocus();
                return;
            }

            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            if (duracion <= 0 || duracion > 12) {
                JOptionPane.showMessageDialog(this, "La duración debe estar entre 1 y 12 semestres.");
                txtDuracion.requestFocus();
                return;
            }

            Programa p = new Programa();
            p.setNombre(txtNombre.getText().trim());
            p.setCodigo(txtCodigo.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setFacultad(txtFacultad.getText().trim());
            p.setDuracionSemestres(duracion);
            p.setEstado((String) cmbEstado.getSelectedItem());

            boolean exito;
            if (idSeleccionado > 0) {
                p.setIdPrograma(idSeleccionado);
                exito = programaLogica.actualizar(p);
                if (exito) JOptionPane.showMessageDialog(this, " Programa actualizado correctamente.");
            } else {
                exito = programaLogica.crearPrograma(p);
                if (exito) JOptionPane.showMessageDialog(this, " Programa guardado correctamente.");
            }

            if (exito) {
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, " Error al guardar el programa.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La duración debe ser un número válido.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un programa para eliminar.");
            return;
        }

        int id = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
        String nombre = (String) modeloTabla.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
              "¿Eliminar el programa '" + nombre + "'?\nEsta acción no se puede deshacer.",
              "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (programaLogica.desactivar(id)) {
                    JOptionPane.showMessageDialog(this, " Programa eliminado correctamente.");
                    limpiarFormulario();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al eliminar el programa.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtFacultad.setText("");
        txtDuracion.setText("");
        cmbEstado.setSelectedIndex(0);
        btnGuardar.setText(" Guardar");
        idSeleccionado = -1;
        txtNombre.requestFocus();
    }
}