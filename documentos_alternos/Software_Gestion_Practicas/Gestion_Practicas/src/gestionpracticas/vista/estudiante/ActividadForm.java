package com.gestionpracticas.vista.estudiante;

import com.gestionpracticas.logica.MatriculaPracticaLogica;
import com.gestionpracticas.logica.RegistroActividadLogica;
import com.gestionpracticas.modelo.MatriculaPractica;
import com.gestionpracticas.modelo.RegistroActividad;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class ActividadForm extends JFrame {

    private final Usuario usuarioActual;
    private final RegistroActividadLogica actividadLogica;
    private final MatriculaPracticaLogica matriculaLogica;

    private JComboBox cmbMatricula, cmbTipo;
    private JTextField txtDescripcion, txtHoras, txtBuscar;
    private JButton btnGuardar, btnEliminar, btnRefrescar, btnBuscar, btnConfigPantalla;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JSplitPane split;
    private JPanel mainPanel;

    private List listaMatriculas;  // Sin generics
    private int idSeleccionado = -1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ActividadForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.actividadLogica = new RegistroActividadLogica();
        this.matriculaLogica = new MatriculaPracticaLogica();
        initComponents();
        cargarCombos();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Registro de Actividades - " + usuarioActual.getNombre());
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
        
        cmbMatricula.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cmbMatricula.isEnabled() && cmbMatricula.getSelectedIndex() >= 0) {
                    cargarTabla();
                }
            }
        });
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 97, 140));
        header.setPreferredSize(new Dimension(1200, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("REGISTRO DE ACTIVIDADES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel("Estudiante: " + usuarioActual.getNombre());
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

        JLabel lblTituloForm = new JLabel("REGISTRAR NUEVA ACTIVIDAD");
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
        Dimension fieldSize = new Dimension(310, 32);

        gbc.gridy = 0;
        JLabel lblPractica = new JLabel("PRACTICA ASIGNADA");
        lblPractica.setFont(labelFont);
        lblPractica.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblPractica, gbc);

        gbc.gridy = 1;
        cmbMatricula = new JComboBox();
        cmbMatricula.setFont(fieldFont);
        cmbMatricula.setPreferredSize(fieldSize);
        cmbMatricula.setBackground(Color.WHITE);
        cmbMatricula.setBorder(crearBordeCampo());
        camposPanel.add(cmbMatricula, gbc);

        gbc.gridy = 2;
        JLabel lblDescripcion = new JLabel("DESCRIPCION DE LA ACTIVIDAD");
        lblDescripcion.setFont(labelFont);
        lblDescripcion.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblDescripcion, gbc);

        gbc.gridy = 3;
        txtDescripcion = new JTextField();
        txtDescripcion.setFont(fieldFont);
        txtDescripcion.setPreferredSize(fieldSize);
        txtDescripcion.setBorder(crearBordeCampo());
        camposPanel.add(txtDescripcion, gbc);

        gbc.gridy = 4;
        JLabel lblHoras = new JLabel("HORAS TRABAJADAS");
        lblHoras.setFont(labelFont);
        lblHoras.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblHoras, gbc);

        gbc.gridy = 5;
        txtHoras = new JTextField();
        txtHoras.setFont(fieldFont);
        txtHoras.setPreferredSize(fieldSize);
        txtHoras.setBorder(crearBordeCampo());
        camposPanel.add(txtHoras, gbc);

        gbc.gridy = 6;
        JLabel lblTipo = new JLabel("TIPO DE ACTIVIDAD");
        lblTipo.setFont(labelFont);
        lblTipo.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblTipo, gbc);

        gbc.gridy = 7;
        String[] tipos = {
            Utilidades.ACTIVIDAD_DESARROLLO,
            Utilidades.ACTIVIDAD_REUNION,
            Utilidades.ACTIVIDAD_CAPACITACION,
            Utilidades.ACTIVIDAD_DOCUMENTACION
        };
        cmbTipo = new JComboBox(tipos);
        cmbTipo.setFont(fieldFont);
        cmbTipo.setPreferredSize(fieldSize);
        cmbTipo.setBackground(Color.WHITE);
        cmbTipo.setBorder(crearBordeCampo());
        camposPanel.add(cmbTipo, gbc);

        JPanel botonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        botonesForm.setBackground(Color.WHITE);

        btnGuardar = new JButton("Guardar");
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

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setPreferredSize(new Dimension(110, 35));
        btnRefrescar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
                cargarCombos();
                cargarTabla();
            }
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

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        
        JLabel lblTablaTitulo = new JLabel("LISTA DE ACTIVIDADES REGISTRADAS");
        lblTablaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTablaTitulo.setForeground(new Color(33, 97, 140));
        lblTablaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelSuperior.add(lblTablaTitulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar:");
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
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        String[] cols = {"ID", "Práctica", "Descripción", "Horas", "Tipo", "Fecha", "Estado"};
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
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setMaxWidth(60);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(80);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editarItem = new JMenuItem("Editar Actividad");
        JMenuItem eliminarItem = new JMenuItem("Eliminar Actividad");
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
                    cargarActividadSeleccionada();
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

        editarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tabla.getSelectedRow() >= 0) {
                    cargarActividadSeleccionada();
                }
            }
        });
        
        eliminarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Actividades Registradas"));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelSur.setBackground(Color.WHITE);

        btnEliminar = new JButton("Eliminar Seleccionado");
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

        panel.add(panelSuperior, BorderLayout.NORTH);
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

        JLabel lblInfo = new JLabel("Las actividades deben ser validadas por el tutor institucional");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);

        JPanel panelConfig = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelConfig.setBackground(new Color(240, 242, 245));

        btnConfigPantalla = new JButton("Resolucion");
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
        int result = JOptionPane.showConfirmDialog(this, combo, "Configurar Resolucion",
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
            listaMatriculas = matriculaLogica.listarPorEstudiante(usuarioActual.getIdUsuario());
            cmbMatricula.removeAllItems();
            
            if (listaMatriculas != null && !listaMatriculas.isEmpty()) {
                for (int i = 0; i < listaMatriculas.size(); i++) {
                    MatriculaPractica m = (MatriculaPractica) listaMatriculas.get(i);
                    String practica = "";
                    if (m.getPractica() != null) {
                        practica = m.getPractica().getTitulo();
                    } else {
                        practica = "Practica #" + m.getIdMatriculaPractica();
                    }
                    cmbMatricula.addItem(practica);
                }
                cmbMatricula.setEnabled(true);
            } else {
                cmbMatricula.addItem("No hay practicas asignadas");
                cmbMatricula.setEnabled(false);
                JOptionPane.showMessageDialog(this, 
                  "No tiene practicas asignadas. Contacte al administrador.",
                  "Sin practicas", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
              "Error cargando practicas: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        
        if (listaMatriculas == null || listaMatriculas.isEmpty()) {
            return;
        }
        
        int idx = cmbMatricula.getSelectedIndex();
        if (idx < 0 || idx >= listaMatriculas.size()) {
            return;
        }

        try {
            MatriculaPractica matricula = (MatriculaPractica) listaMatriculas.get(idx);
            int idMatricula = matricula.getIdMatriculaPractica();
            List actividades = actividadLogica.listarPorMatricula(idMatricula);
            
            if (actividades != null && !actividades.isEmpty()) {
                for (int i = 0; i < actividades.size(); i++) {
                    RegistroActividad r = (RegistroActividad) actividades.get(i);
                    String practica = "";
                    if (r.getMatricula() != null && r.getMatricula().getPractica() != null) {
                        practica = r.getMatricula().getPractica().getTitulo();
                    }
                    String fecha = (r.getFechaActividad() != null) ? sdf.format(r.getFechaActividad()) : "";
                    String estado = (r.getEstado() != null) ? r.getEstado() : "DESCONOCIDO";
                    
                    modeloTabla.addRow(new Object[]{
                            Integer.valueOf(r.getIdRegistro()),
                            practica,
                            r.getDescripcion(),
                            Double.valueOf(r.getHoras()),
                            r.getTipoActividad(),
                            fecha,
                            estado
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
              "Error cargando tabla: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buscar() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        
        if (busqueda.equals("")) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        
        if (listaMatriculas == null || listaMatriculas.isEmpty()) {
            return;
        }

        int idx = cmbMatricula.getSelectedIndex();
        if (idx < 0 || idx >= listaMatriculas.size()) {
            return;
        }

        try {
            MatriculaPractica matricula = (MatriculaPractica) listaMatriculas.get(idx);
            int idMatricula = matricula.getIdMatriculaPractica();
            List actividades = actividadLogica.listarPorMatricula(idMatricula);
            
            if (actividades != null) {
                for (int i = 0; i < actividades.size(); i++) {
                    RegistroActividad r = (RegistroActividad) actividades.get(i);
                    String descripcion = (r.getDescripcion() != null) ? r.getDescripcion().toLowerCase() : "";
                    
                    if (descripcion.indexOf(busqueda) >= 0) {
                        String practica = (r.getMatricula() != null && r.getMatricula().getPractica() != null) ?
                                r.getMatricula().getPractica().getTitulo() : "";
                        String fecha = (r.getFechaActividad() != null) ? sdf.format(r.getFechaActividad()) : "";
                        
                        modeloTabla.addRow(new Object[]{
                                Integer.valueOf(r.getIdRegistro()),
                                practica,
                                r.getDescripcion(),
                                Double.valueOf(r.getHoras()),
                                r.getTipoActividad(),
                                fecha,
                                r.getEstado()
                        });
                    }
                }
            }
            
            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                  "No se encontraron actividades con: '" + busqueda + "'",
                  "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
              "Error en busqueda: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cargarActividadSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            try {
                idSeleccionado = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
                txtDescripcion.setText((String) modeloTabla.getValueAt(fila, 2));
                txtHoras.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
                
                Object tipoObj = modeloTabla.getValueAt(fila, 4);
                if (tipoObj != null) {
                    cmbTipo.setSelectedItem(tipoObj);
                }
                
                btnGuardar.setText("Actualizar");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                  "Error al cargar actividad: " + e.getMessage(),
                  "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardar() {
        try {
            if (txtDescripcion.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese la descripcion de la actividad.");
                txtDescripcion.requestFocus();
                return;
            }
            
            if (txtHoras.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese las horas trabajadas.");
                txtHoras.requestFocus();
                return;
            }

            double horas;
            try {
                horas = Double.parseDouble(txtHoras.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Las horas deben ser un numero valido (ej: 2.5)");
                txtHoras.requestFocus();
                return;
            }
            
            if (horas <= 0) {
                JOptionPane.showMessageDialog(this, "Las horas deben ser mayores a 0.");
                txtHoras.requestFocus();
                return;
            }
            
            if (horas > 8) {
                JOptionPane.showMessageDialog(this, "No se pueden registrar mas de 8 horas por dia.");
                txtHoras.requestFocus();
                return;
            }

            int idxMat = cmbMatricula.getSelectedIndex();
            if (idxMat < 0 || listaMatriculas == null || listaMatriculas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una practica valida.");
                return;
            }

            RegistroActividad r = new RegistroActividad();
            r.setMatricula((MatriculaPractica) listaMatriculas.get(idxMat));
            r.setDescripcion(txtDescripcion.getText().trim());
            r.setHoras(horas);
            r.setTipoActividad((String) cmbTipo.getSelectedItem());
            r.setFechaActividad(new Date());
            r.setEstado(Utilidades.ESTADO_PENDIENTE);

            boolean exito;
            if (idSeleccionado > 0) {
                r.setIdRegistro(idSeleccionado);
                exito = actividadLogica.actualizar(r);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Actividad actualizada correctamente.");
                }
            } else {
                exito = actividadLogica.registrar(r);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Actividad registrada correctamente.");
                }
            }

            if (exito) {
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la actividad. Verifique los datos e intente nuevamente.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad para eliminar.");
            return;
        }

        int id = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
        String descripcion = (String) modeloTabla.getValueAt(fila, 2);
        String estado = (String) modeloTabla.getValueAt(fila, 6);

        if (Utilidades.ESTADO_APROBADO.equals(estado)) {
            JOptionPane.showMessageDialog(this, 
              "No se puede eliminar una actividad que ya ha sido aprobada por el tutor.",
              "Eliminacion no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
              "Eliminar la actividad '" + descripcion + "'?\nEsta accion no se puede deshacer.",
              "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (actividadLogica.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Actividad eliminada correctamente.");
                    limpiarFormulario();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la actividad.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void limpiarFormulario(
) {
        txtDescripcion.setText("");
        txtHoras.setText("");
        if (cmbTipo.getItemCount() > 0) {
            cmbTipo.setSelectedIndex(0);
        }
        btnGuardar.setText("Guardar");
        idSeleccionado = -1;
        txtDescripcion.requestFocus();
    }
}