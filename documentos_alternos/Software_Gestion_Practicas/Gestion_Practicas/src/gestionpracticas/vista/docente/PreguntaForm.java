package com.gestionpracticas.vista.docente;

import com.gestionpracticas.logica.PreguntaLogica;
import com.gestionpracticas.modelo.Pregunta;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.List;

public class PreguntaForm extends JFrame {

    private final PreguntaLogica preguntaLogica;
    private final Usuario usuarioActual;

    private JTextField txtTexto, txtBuscar;
    private JComboBox<String> cmbTipo;
    private JTextField txtPuntaje;
    private JButton btnGuardar, btnEliminar, btnRefrescar, btnBuscar, btnConfigPantalla;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JSplitPane split;
    private JPanel mainPanel;

    public PreguntaForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.preguntaLogica = new PreguntaLogica();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Preguntas - " + usuarioActual.getNombre());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        getContentPane().setBackground(new Color(240, 242, 245));

        // Panel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Header
        JPanel header = crearHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        // Split pane
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(420);
        split.setResizeWeight(0.35);

        // Panel izquierdo - Formulario
        JPanel formPanel = crearPanelFormulario();
        split.setLeftComponent(formPanel);

        // Panel derecho - Tabla de preguntas
        JPanel tablaPanel = crearPanelTabla();
        split.setRightComponent(tablaPanel);

        mainPanel.add(split, BorderLayout.CENTER);

        // Footer
        JPanel footer = crearFooter();
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 97, 140));
        header.setPreferredSize(new Dimension(1200, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel(" GESTIÓN DE PREGUNTAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel(" Docente: " + usuarioActual.getNombre());
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

        // Título del formulario
        JLabel lblTituloForm = new JLabel(" CREAR NUEVA PREGUNTA");
        lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloForm.setForeground(new Color(33, 97, 140));
        lblTituloForm.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloForm.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Panel de campos
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

        // Texto de la pregunta
        gbc.gridy = 0;
        JLabel lblTexto = new JLabel("TEXTO DE LA PREGUNTA");
        lblTexto.setFont(labelFont);
        lblTexto.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblTexto, gbc);

        gbc.gridy = 1;
        txtTexto = new JTextField();
        txtTexto.setFont(fieldFont);
        txtTexto.setPreferredSize(fieldSize);
        txtTexto.setBorder(crearBordeCampo());
        camposPanel.add(txtTexto, gbc);

        // Tipo de pregunta
        gbc.gridy = 2;
        JLabel lblTipo = new JLabel("TIPO DE PREGUNTA");
        lblTipo.setFont(labelFont);
        lblTipo.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblTipo, gbc);

        gbc.gridy = 3;
        cmbTipo = new JComboBox<>(new String[]{
            Utilidades.TIPO_PREGUNTA_ABIERTA,
            Utilidades.TIPO_PREGUNTA_OPCION_MULTIPLE,
            Utilidades.TIPO_PREGUNTA_ESCALA,
            Utilidades.TIPO_PREGUNTA_NUMERICO
        });
        cmbTipo.setFont(fieldFont);
        cmbTipo.setPreferredSize(fieldSize);
        cmbTipo.setBackground(Color.WHITE);
        cmbTipo.setBorder(crearBordeCampo());
        camposPanel.add(cmbTipo, gbc);

        // Puntaje máximo
        gbc.gridy = 4;
        JLabel lblPuntaje = new JLabel("PUNTAJE MÁXIMO (1-100)");
        lblPuntaje.setFont(labelFont);
        lblPuntaje.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblPuntaje, gbc);

        gbc.gridy = 5;
        txtPuntaje = new JTextField("5");
        txtPuntaje.setFont(fieldFont);
        txtPuntaje.setPreferredSize(fieldSize);
        txtPuntaje.setBorder(crearBordeCampo());
        camposPanel.add(txtPuntaje, gbc);

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
        btnGuardar.addActionListener(e -> guardar());

        btnRefrescar = new JButton(" Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setPreferredSize(new Dimension(110, 35));
        btnRefrescar.addActionListener(e -> {
            limpiar();
            cargarTabla();
        });

        botonesForm.add(btnGuardar);
        botonesForm.add(btnRefrescar);

        gbc.gridy = 6;
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

        // Título de la tabla
        JLabel lblTablaTitulo = new JLabel(" LISTA DE PREGUNTAS REGISTRADAS");
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
        btnBuscar.addActionListener(e -> buscar());

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        // Columnas de la tabla
        String[] cols = {"ID", "Pregunta", "Tipo", "Puntaje Máx.", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override
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
        tabla.getColumnModel().getColumn(1).setPreferredWidth(350);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setMaxWidth(90);
        tabla.getColumnModel().getColumn(4).setMaxWidth(80);

        // Popup menu para eliminar
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem eliminarItem = new JMenuItem(" Eliminar Pregunta");
        popupMenu.add(eliminarItem);

        tabla.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tabla.rowAtPoint(e.getPoint());
                    tabla.setRowSelectionInterval(row, row);
                    popupMenu.show(tabla, e.getX(), e.getY());
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

        eliminarItem.addActionListener(e -> eliminar());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Preguntas"));

        // Panel sur con botón eliminar
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
        btnEliminar.addActionListener(e -> eliminar());

        panelSur.add(btnEliminar);

        panel.add(lblTablaTitulo, BorderLayout.NORTH);
        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelSur, BorderLayout.SOUTH);

        // Habilitar botón eliminar según selección
        tabla.getSelectionModel().addListSelectionListener(e -> {
            btnEliminar.setEnabled(tabla.getSelectedRow() >= 0);
        });

        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(240, 242, 245));
        footer.setPreferredSize(new Dimension(1200, 40));
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblInfo = new JLabel("💡 Las preguntas se asignan automáticamente a las evaluaciones");
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
        btnConfigPantalla.addActionListener(e -> configurarResolucion());

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

        JComboBox<String> combo = new JComboBox<>(resoluciones);
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
            List<Pregunta> preguntas = preguntaLogica.listarTodas();
            if (preguntas != null) {
                for (Pregunta p : preguntas) {
                    modeloTabla.addRow(new Object[]{
                        p.getIdPregunta(),
                        p.getTextoPregunta(),
                        p.getTipoPregunta(),
                        p.getPuntajeMaximo(),
                        p.getEstado()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        modeloTabla.setRowCount(0);
        try {
            List<Pregunta> preguntas = preguntaLogica.listarTodas();
            if (preguntas != null) {
                for (Pregunta p : preguntas) {
                    if (p.getTextoPregunta().toLowerCase().contains(busqueda)) {
                        modeloTabla.addRow(new Object[]{
                            p.getIdPregunta(),
                            p.getTextoPregunta(),
                            p.getTipoPregunta(),
                            p.getPuntajeMaximo(),
                            p.getEstado()
                        });
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en búsqueda: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        try {
            if (txtTexto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el texto de la pregunta.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtTexto.requestFocus();
                return;
            }
            
            if (txtPuntaje.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el puntaje máximo.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtPuntaje.requestFocus();
                return;
            }
            
            double puntaje = Double.parseDouble(txtPuntaje.getText().trim());
            if (puntaje <= 0) {
                JOptionPane.showMessageDialog(this, "El puntaje máximo debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtPuntaje.requestFocus();
                return;
            }
            
            if (puntaje > 100) {
                JOptionPane.showMessageDialog(this, "El puntaje máximo no puede superar 100.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtPuntaje.requestFocus();
                return;
            }
            
            Pregunta p = new Pregunta();
            p.setTextoPregunta(txtTexto.getText().trim());
            p.setTipoPregunta((String) cmbTipo.getSelectedItem());
            p.setPuntajeMaximo(puntaje);
            p.setEstado(Utilidades.ESTADO_ACTIVO);
            
            int confirm = JOptionPane.showConfirmDialog(this,
              "¿Guardar la pregunta?\n" + p.getTextoPregunta(),
              "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            if (preguntaLogica.guardar(p)) {
                JOptionPane.showMessageDialog(this, " Pregunta guardada correctamente.");
                limpiar();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, " Error al guardar la pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El puntaje debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String texto = (String) modeloTabla.getValueAt(fila, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
          "¿Eliminar la pregunta?\n" + texto + "\n\nEsta acción no se puede deshacer.", 
          "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (preguntaLogica.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, " Pregunta eliminada correctamente.");
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al eliminar la pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiar() {
        txtTexto.setText("");
        txtPuntaje.setText("5");
        cmbTipo.setSelectedIndex(0);
        txtBuscar.setText("");
        txtTexto.requestFocus();
    }
}