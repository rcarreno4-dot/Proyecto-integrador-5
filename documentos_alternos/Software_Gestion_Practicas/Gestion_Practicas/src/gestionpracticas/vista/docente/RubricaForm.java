package com.gestionpracticas.vista.docente;

import com.gestionpracticas.logica.PracticaLogica;
import com.gestionpracticas.logica.RubricaLogica;
import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.modelo.Rubrica;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class RubricaForm extends JFrame {

    private final RubricaLogica rubricaLogica;
    private final PracticaLogica practicaLogica;
    private final Usuario usuarioActual;

    // Componentes del formulario
    private JTextField txtNombre, txtDescripcion, txtPuntajeTotal, txtBuscar;
    private JComboBox<String> cmbPractica;
    private JButton btnGuardar, btnEliminar, btnRefrescar, btnBuscar, btnConfigPantalla;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JSplitPane split;
    private JPanel mainPanel;

    private List<Practica> listaPracticas;
    private int idSeleccionado = -1;

    public RubricaForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.rubricaLogica = new RubricaLogica();
        this.practicaLogica = new PracticaLogica();
        initComponents();
        cargarCombos();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Rúbricas - " + usuarioActual.getNombre());
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

        // Panel derecho - Tabla de rúbricas
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

        JLabel lblTitulo = new JLabel(" GESTIÓN DE RÚBRICAS");
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
        JLabel lblTituloForm = new JLabel(" CREAR NUEVA RÚBRICA");
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

        // Nombre de la rúbrica
        gbc.gridy = 0;
        JLabel lblNombre = new JLabel("NOMBRE DE LA RÚBRICA *");
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblNombre, gbc);

        gbc.gridy = 1;
        txtNombre = new JTextField();
        txtNombre.setFont(fieldFont);
        txtNombre.setPreferredSize(fieldSize);
        txtNombre.setBorder(crearBordeCampo());
        camposPanel.add(txtNombre, gbc);

        // Descripción
        gbc.gridy = 2;
        JLabel lblDescripcion = new JLabel("DESCRIPCIÓN");
        lblDescripcion.setFont(labelFont);
        lblDescripcion.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblDescripcion, gbc);

        gbc.gridy = 3;
        txtDescripcion = new JTextField();
        txtDescripcion.setFont(fieldFont);
        txtDescripcion.setPreferredSize(fieldSize);
        txtDescripcion.setBorder(crearBordeCampo());
        camposPanel.add(txtDescripcion, gbc);

        // Práctica asociada
        gbc.gridy = 4;
        JLabel lblPractica = new JLabel("PRÁCTICA ASOCIADA *");
        lblPractica.setFont(labelFont);
        lblPractica.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblPractica, gbc);

        gbc.gridy = 5;
        cmbPractica = new JComboBox<>();
        cmbPractica.setFont(fieldFont);
        cmbPractica.setPreferredSize(fieldSize);
        cmbPractica.setBackground(Color.WHITE);
        cmbPractica.setBorder(crearBordeCampo());
        camposPanel.add(cmbPractica, gbc);

        // Puntaje total
        gbc.gridy = 6;
        JLabel lblPuntaje = new JLabel("PUNTAJE TOTAL (0-100) *");
        lblPuntaje.setFont(labelFont);
        lblPuntaje.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblPuntaje, gbc);

        gbc.gridy = 7;
        txtPuntajeTotal = new JTextField();
        txtPuntajeTotal.setFont(fieldFont);
        txtPuntajeTotal.setPreferredSize(fieldSize);
        txtPuntajeTotal.setBorder(crearBordeCampo());
        camposPanel.add(txtPuntajeTotal, gbc);

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
            limpiarFormulario();
            cargarCombos();
            cargarTabla();
        });

        botonesForm.add(btnGuardar);
        botonesForm.add(btnRefrescar);

        gbc.gridy = 8;
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

        // Panel superior con título y búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);

        JLabel lblTablaTitulo = new JLabel(" LISTA DE RÚBRICAS REGISTRADAS");
        lblTablaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTablaTitulo.setForeground(new Color(33, 97, 140));
        lblTablaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelSuperior.add(lblTablaTitulo, BorderLayout.NORTH);

        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel(" Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        txtBuscar = new JTextField(15);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscar.setPreferredSize(new Dimension(150, 28));
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                buscar();
            }
        });

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

        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        // Columnas de la tabla
        String[] cols = {"ID", "Nombre", "Descripción", "Práctica", "Puntaje Total", "Estado"};
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
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(4).setMaxWidth(90);
        tabla.getColumnModel().getColumn(5).setMaxWidth(80);

        // Popup menu para editar/eliminar
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editarItem = new JMenuItem(" Editar Rúbrica");
        JMenuItem eliminarItem = new JMenuItem(" Eliminar Rúbrica");
        popupMenu.add(editarItem);
        popupMenu.add(eliminarItem);

        tabla.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tabla.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        tabla.setRowSelectionInterval(row, row);
                        popupMenu.show(tabla, e.getX(), e.getY());
                    }
                }
                if (e.getClickCount() == 2 && tabla.getSelectedRow() >= 0) {
                    cargarRubricaSeleccionada();
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tabla.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        tabla.setRowSelectionInterval(row, row);
                        popupMenu.show(tabla, e.getX(), e.getY());
                    }
                }
            }
        });

        editarItem.addActionListener(e -> {
            if (tabla.getSelectedRow() >= 0) {
                cargarRubricaSeleccionada();
            }
        });
        eliminarItem.addActionListener(e -> eliminar());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Rúbricas"));

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

        panel.add(panelSuperior, BorderLayout.NORTH);
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

        JLabel lblInfo = new JLabel("💡 Las rúbricas se utilizan para evaluar el desempeño de los estudiantes");
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

    private void cargarCombos() {
        try {
            listaPracticas = practicaLogica.obtenerTodas();
            cmbPractica.removeAllItems();
            
            if (listaPracticas != null && !listaPracticas.isEmpty()) {
                for (Practica p : listaPracticas) {
                    String titulo = (p.getTitulo() != null) ? p.getTitulo() : "Práctica #" + p.getIdPractica();
                    cmbPractica.addItem(titulo);
                }
                cmbPractica.setEnabled(true);
            } else {
                cmbPractica.addItem("No hay prácticas disponibles");
                cmbPractica.setEnabled(false);
                JOptionPane.showMessageDialog(this, 
                  "No hay prácticas registradas. Cree una práctica antes de crear rúbricas.",
                  "Sin prácticas", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
              "Error de base de datos al cargar prácticas: " + e.getMessage(),
              "Error BD", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } 
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Rubrica> rubricas = rubricaLogica.listarTodas();
            if (rubricas != null && !rubricas.isEmpty()) {
                for (Rubrica r : rubricas) {
                    String practica = (r.getPractica() != null && r.getPractica().getTitulo() != null) 
                                    ? r.getPractica().getTitulo() 
                                    : "N/A";
                    String estado = (r.getEstado() != null) ? r.getEstado() : "DESCONOCIDO";
                    
                    modeloTabla.addRow(new Object[]{
                            r.getIdRubrica(),
                            r.getNombre(),
                            r.getDescripcion(),
                            practica,
                            r.getPuntajeTotal(),
                            estado
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
              "Error de base de datos al cargar tabla: " + e.getMessage(),
              "Error BD", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } 
    }

    private void buscar() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        
        if (busqueda.isEmpty()) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        try {
            List<Rubrica> rubricas = rubricaLogica.listarTodas();
            if (rubricas != null) {
                for (Rubrica r : rubricas) {
                    String nombre = (r.getNombre() != null) ? r.getNombre().toLowerCase() : "";
                    
                    if (nombre.contains(busqueda)) {
                        String practica = (r.getPractica() != null && r.getPractica().getTitulo() != null) 
                                        ? r.getPractica().getTitulo() 
                                        : "N/A";
                        modeloTabla.addRow(new Object[]{
                                r.getIdRubrica(),
                                r.getNombre(),
                                r.getDescripcion(),
                                practica,
                                r.getPuntajeTotal(),
                                r.getEstado()
                        });
                    }
                }
            }
            
            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                  "No se encontraron rúbricas con: '" + busqueda + "'",
                  "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
              "Error de base de datos en búsqueda: " + e.getMessage(),
              "Error BD", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } 
    }

    private void cargarRubricaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            try {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
                
                Object descObj = modeloTabla.getValueAt(fila, 2);
                txtDescripcion.setText(descObj != null ? (String) descObj : "");
                
                txtPuntajeTotal.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
                btnGuardar.setText(" Actualizar");

                // Seleccionar práctica correspondiente
                String practicaNombre = (String) modeloTabla.getValueAt(fila, 3);
                for (int i = 0; i < cmbPractica.getItemCount(); i++) {
                    if (cmbPractica.getItemAt(i).equals(practicaNombre)) {
                        cmbPractica.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Scroll hasta el formulario
                SwingUtilities.invokeLater(() -> {
                    JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, split.getLeftComponent());
                    if (scrollPane != null) {
                        JViewport viewport = scrollPane.getViewport();
                        viewport.setViewPosition(new Point(0, 0));
                    }
                });
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                  "Error al cargar rúbrica: " + e.getMessage(),
                  "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardar() {
        try {
            // Validaciones
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre de la rúbrica.");
                txtNombre.requestFocus();
                return;
            }
            
            if (txtPuntajeTotal.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el puntaje total.");
                txtPuntajeTotal.requestFocus();
                return;
            }

            double puntajeTotal;
            try {
                puntajeTotal = Double.parseDouble(txtPuntajeTotal.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El puntaje debe ser un número válido (ej: 85.5)");
                txtPuntajeTotal.requestFocus();
                return;
            }
            
            if (puntajeTotal <= 0 || puntajeTotal > 100) {
                JOptionPane.showMessageDialog(this, "El puntaje total debe estar entre 1 y 100.");
                txtPuntajeTotal.requestFocus();
                return;
            }

            int idxPractica = cmbPractica.getSelectedIndex();
            if (idxPractica < 0 || listaPracticas == null || listaPracticas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una práctica válida.");
                return;
            }

            // Verificar si ya existe una rúbrica para esta práctica (solo para nuevas)
            if (idSeleccionado <= 0) {
                boolean existe = rubricaLogica.existeRubricaParaPractica(listaPracticas.get(idxPractica).getIdPractica());
                if (existe) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                      "Ya existe una rúbrica para esta práctica. ¿Desea reemplazarla?",
                      "Rúbrica existente", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            // Crear objeto rúbrica
            Rubrica r = new Rubrica();
            r.setNombre(txtNombre.getText().trim());
            r.setDescripcion(txtDescripcion.getText().trim());
            r.setPractica(listaPracticas.get(idxPractica));
            r.setDocente(usuarioActual);
            r.setPuntajeTotal(puntajeTotal);
            r.setEstado(Utilidades.ESTADO_ACTIVO);

            boolean exito;
            if (idSeleccionado > 0) {
                // Actualizar
                r.setIdRubrica(idSeleccionado);
                exito = rubricaLogica.actualizar(r);
                if (exito) {
                    JOptionPane.showMessageDialog(this, " Rúbrica actualizada correctamente.");
                }
            } else {
                // Guardar nueva
                exito = rubricaLogica.guardar(r);
                if (exito) {
                    JOptionPane.showMessageDialog(this, " Rúbrica guardada correctamente.");
                }
            }

            if (exito) {
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, " Error al guardar la rúbrica. Verifique los datos e intente nuevamente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
              "Error de base de datos al guardar: " + e.getMessage(),
              "Error BD", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una rúbrica para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        String estado = (String) modeloTabla.getValueAt(fila, 5);

        // Verificar si la rúbrica está siendo utilizada
        try {
            boolean enUso = rubricaLogica.rubricaEnUso(id);
            if (enUso) {
                JOptionPane.showMessageDialog(this, 
                  "No se puede eliminar esta rúbrica porque está siendo utilizada en evaluaciones.",
                  "Eliminación no permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
              "Error al verificar uso de rúbrica: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
              "¿Eliminar la rúbrica '" + nombre + "'?\nEsta acción no se puede deshacer.",
              "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (rubricaLogica.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, " Rúbrica eliminada correctamente.");
                    limpiarFormulario();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al eliminar la rúbrica.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                  "Error de base de datos al eliminar: " + e.getMessage(),
                  "Error BD", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } 
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPuntajeTotal.setText("");
        if (cmbPractica.getItemCount() > 0 && cmbPractica.isEnabled()) {
            cmbPractica.setSelectedIndex(0);
        }
        btnGuardar.setText(" Guardar");
        idSeleccionado = -1;
        txtNombre.requestFocus();
    }
}