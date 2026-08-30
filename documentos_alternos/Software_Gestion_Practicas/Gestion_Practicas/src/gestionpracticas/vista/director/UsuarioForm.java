package com.gestionpracticas.vista.director;

import com.gestionpracticas.logica.UsuarioLogica;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import com.gestionpracticas.util.DBHelper;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class UsuarioForm extends JFrame {
    
    private final Usuario usuarioActual;
    private final Usuario usuarioEditar;
    private final UsuarioLogica usuarioLogica;
    private boolean editando = false;
    private String tipoFiltro;
    
    // Componentes de la interfaz
    private JTextField txtNombre, txtApellido, txtEmail, txtTelefono, txtDocumento, txtBuscar;
    private JPasswordField txtContrasena, txtConfirmarContrasena;
    private JComboBox cmbTipoUsuario;
    private JTextArea txtObservaciones;
    private JButton btnGuardar, btnLimpiar, btnEliminar, btnActivar, btnEliminarDefinitivo, btnRefrescar, btnBuscar;
    private JButton btnCambiarPass, btnRegistrarError;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTabbedPane tabbedPane;
    private int idSeleccionado = -1;
    
    public UsuarioForm(Usuario usuario) {
        this(usuario, null, null);
    }
    
    public UsuarioForm(Usuario usuario, String tipoFiltro) {
        this(usuario, null, tipoFiltro);
    }
    
    public UsuarioForm(Usuario usuario, Usuario usuarioEditar) {
        this(usuario, usuarioEditar, null);
    }
    
    public UsuarioForm(Usuario usuario, Usuario usuarioEditar, String tipoFiltro) {
        this.usuarioActual = usuario;
        this.usuarioEditar = usuarioEditar;
        this.usuarioLogica = new UsuarioLogica();
        this.editando = (usuarioEditar != null);
        this.tipoFiltro = tipoFiltro;
        initComponents();
        if (editando) {
            cargarDatosUsuario();
        } else {
            cargarTabla();
        }
    }
    
    private void initComponents() {
        String titulo = "Gestión de Usuarios";
        if (tipoFiltro != null) {
            titulo = titulo + " - " + tipoFiltro;
        }
        if (editando) {
            titulo = " Editar Usuario";
        }
        setTitle(titulo + " - " + usuarioActual.getNombre());
        setSize(1200, 750);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 242, 245));
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 242, 245));
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel lblBuscar = new JLabel(" Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.setPreferredSize(new Dimension(250, 35));
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setPreferredSize(new Dimension(100, 35));
        
        btnRefrescar = new JButton(" Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setPreferredSize(new Dimension(110, 35));
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(Box.createHorizontalStrut(20));
        panelBusqueda.add(btnRefrescar);
        JLabel lblAyudaCrud = new JLabel("  Crear: diligencia y Guarda | Editar: doble clic | Activar/Inactivar: selecciona una fila | Contraseña: clic derecho o pestaña Seguridad  ");
        lblAyudaCrud.setOpaque(true);
        lblAyudaCrud.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblAyudaCrud.setForeground(new Color(27, 38, 59));
        lblAyudaCrud.setBackground(new Color(232, 244, 253));
        lblAyudaCrud.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185)));
        panelBusqueda.add(lblAyudaCrud);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.4);
        
        // Panel izquierdo - Tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        
        String[] columnas = {"ID", "Nombre", "Apellido", "Email", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(33, 97, 140));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editarItem = new JMenuItem(" Editar Usuario");
        JMenuItem activarItem = new JMenuItem(" Activar Usuario");
        JMenuItem eliminarItem = new JMenuItem(" Inactivar Usuario");
        JMenuItem eliminarDefItem = new JMenuItem(" Eliminar Definitivo");
        JMenuItem cambiarPassItem = new JMenuItem(" Cambiar Contraseña");
        popupMenu.add(editarItem);
        popupMenu.add(activarItem);
        popupMenu.add(eliminarItem);
        popupMenu.add(eliminarDefItem);
        popupMenu.addSeparator();
        popupMenu.add(cambiarPassItem);
        
        tabla.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tabla.rowAtPoint(e.getPoint());
                    tabla.setRowSelectionInterval(row, row);
                    popupMenu.show(tabla, e.getX(), e.getY());
                }
                if (e.getClickCount() == 2) {
                    cargarUsuarioSeleccionado();
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
                cargarUsuarioSeleccionado();
            }
        });
        
        activarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activarUsuario();
            }
        });
        
        eliminarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
            }
        });
        
        eliminarDefItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarDefinitivoUsuario();
            }
        });
        
        cambiarPassItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoCambiarContrasena();
            }
        });
        
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));
        
        JPanel panelBotonesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesTabla.setBackground(Color.WHITE);
        
        btnActivar = new JButton(" Activar");
        btnActivar.setBackground(new Color(22, 160, 133));
        btnActivar.setForeground(Color.WHITE);
        btnActivar.setFocusPainted(false);
        btnActivar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActivar.setPreferredSize(new Dimension(100, 35));
        btnActivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activarUsuario();
            }
        });

        btnEliminar = new JButton(" Inactivar");
        btnEliminar.setBackground(new Color(243, 156, 18));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(110, 35));
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
            }
        });

        btnEliminarDefinitivo = new JButton(" Eliminar definitivo");
        btnEliminarDefinitivo.setBackground(new Color(192, 57, 43));
        btnEliminarDefinitivo.setForeground(Color.WHITE);
        btnEliminarDefinitivo.setFocusPainted(false);
        btnEliminarDefinitivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminarDefinitivo.setPreferredSize(new Dimension(165, 35));
        btnEliminarDefinitivo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarDefinitivoUsuario();
            }
        });
        panelBotonesTabla.add(btnActivar);
        panelBotonesTabla.add(btnEliminar);
        panelBotonesTabla.add(btnEliminarDefinitivo);
        
        panelTabla.add(scrollTabla, BorderLayout.CENTER);
        panelTabla.add(panelBotonesTabla, BorderLayout.SOUTH);
        
        // Panel derecho - Pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tabbedPane.addTab(" Información", crearPanelInformacion());
        tabbedPane.addTab(" Seguridad", crearPanelSeguridad());
        tabbedPane.addTab(" Observaciones", crearPanelObservaciones());
        
        splitPane.setLeftComponent(panelTabla);
        splitPane.setRightComponent(tabbedPane);
        
        // Panel de botones principal
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 242, 245));
        
        btnGuardar = new JButton(editando ? " Actualizar usuario" : " Crear usuario");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(140, 40));
        
        btnLimpiar = new JButton(" Limpiar");
        btnLimpiar.setBackground(new Color(52, 152, 219));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.setPreferredSize(new Dimension(140, 40));
        
        JButton btnCerrar = new JButton(" Cerrar");
        btnCerrar.setBackground(new Color(192, 57, 43));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(140, 40));
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCerrar);
        
        mainPanel.add(panelBusqueda, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });
        
        btnRefrescar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
            }
        });
        
        if (!editando) {
            cargarTabla();
        }
    }
    
    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        JLabel lblTitulo = new JLabel(editando ? "EDITAR USUARIO" : "REGISTRAR NUEVO USUARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(33, 97, 140));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        Dimension fieldSize = new Dimension(280, 35);
        
        // Nombre
        gbc.gridy = 1;
        JLabel lblNombre = new JLabel("NOMBRES");
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(new Color(52, 73, 94));
        panel.add(lblNombre, gbc);
        
        gbc.gridy = 2;
        txtNombre = new JTextField();
        txtNombre.setFont(fieldFont);
        txtNombre.setPreferredSize(fieldSize);
        txtNombre.setBorder(crearBordeCampo());
        panel.add(txtNombre, gbc);
        
        // Apellido
        gbc.gridy = 3;
        JLabel lblApellido = new JLabel("APELLIDOS");
        lblApellido.setFont(labelFont);
        lblApellido.setForeground(new Color(52, 73, 94));
        panel.add(lblApellido, gbc);
        
        gbc.gridy = 4;
        txtApellido = new JTextField();
        txtApellido.setFont(fieldFont);
        txtApellido.setPreferredSize(fieldSize);
        txtApellido.setBorder(crearBordeCampo());
        panel.add(txtApellido, gbc);
        
        // Email
        gbc.gridy = 5;
        JLabel lblEmail = new JLabel("CORREO ELECTRÓNICO");
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(new Color(52, 73, 94));
        panel.add(lblEmail, gbc);
        
        gbc.gridy = 6;
        txtEmail = new JTextField();
        txtEmail.setFont(fieldFont);
        txtEmail.setPreferredSize(fieldSize);
        txtEmail.setBorder(crearBordeCampo());
        panel.add(txtEmail, gbc);
        
        // Teléfono
        gbc.gridy = 7;
        JLabel lblTelefono = new JLabel("TELÉFONO");
        lblTelefono.setFont(labelFont);
        lblTelefono.setForeground(new Color(52, 73, 94));
        panel.add(lblTelefono, gbc);
        
        gbc.gridy = 8;
        txtTelefono = new JTextField();
        txtTelefono.setFont(fieldFont);
        txtTelefono.setPreferredSize(fieldSize);
        txtTelefono.setBorder(crearBordeCampo());
        panel.add(txtTelefono, gbc);
        
        // Documento
        gbc.gridy = 9;
        JLabel lblDocumento = new JLabel("DOCUMENTO");
        lblDocumento.setFont(labelFont);
        lblDocumento.setForeground(new Color(52, 73, 94));
        panel.add(lblDocumento, gbc);
        
        gbc.gridy = 10;
        txtDocumento = new JTextField();
        txtDocumento.setFont(fieldFont);
        txtDocumento.setPreferredSize(fieldSize);
        txtDocumento.setBorder(crearBordeCampo());
        panel.add(txtDocumento, gbc);
        
        // Tipo Usuario
        gbc.gridy = 11;
        JLabel lblTipo = new JLabel("TIPO DE USUARIO");
        lblTipo.setFont(labelFont);
        lblTipo.setForeground(new Color(52, 73, 94));
        panel.add(lblTipo, gbc);
        
        gbc.gridy = 12;
        cmbTipoUsuario = new JComboBox(new String[]{"DIRECTOR", "COORDINADOR", "DOCENTE", "ESTUDIANTE", "INSTITUCION"});
        cmbTipoUsuario.setFont(fieldFont);
        cmbTipoUsuario.setPreferredSize(fieldSize);
        cmbTipoUsuario.setBackground(Color.WHITE);
        cmbTipoUsuario.setBorder(crearBordeCampo());
        if (tipoFiltro != null) {
            cmbTipoUsuario.setSelectedItem(tipoFiltro);
            cmbTipoUsuario.setEnabled(false);
        }
        panel.add(cmbTipoUsuario, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelSeguridad() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        Dimension fieldSize = new Dimension(280, 35);
        
        if (!editando) {
            gbc.gridy = 0;
            JLabel lblContrasena = new JLabel("CONTRASEÑA");
            lblContrasena.setFont(labelFont);
            lblContrasena.setForeground(new Color(52, 73, 94));
            panel.add(lblContrasena, gbc);
            
            gbc.gridy = 1;
            txtContrasena = new JPasswordField();
            txtContrasena.setFont(fieldFont);
            txtContrasena.setPreferredSize(fieldSize);
            txtContrasena.setBorder(crearBordeCampo());
            panel.add(txtContrasena, gbc);
            
            gbc.gridy = 2;
            JLabel lblConfirmar = new JLabel("CONFIRMAR CONTRASEÑA");
            lblConfirmar.setFont(labelFont);
            lblConfirmar.setForeground(new Color(52, 73, 94));
            panel.add(lblConfirmar, gbc);
            
            gbc.gridy = 3;
            txtConfirmarContrasena = new JPasswordField();
            txtConfirmarContrasena.setFont(fieldFont);
            txtConfirmarContrasena.setPreferredSize(fieldSize);
            txtConfirmarContrasena.setBorder(crearBordeCampo());
            panel.add(txtConfirmarContrasena, gbc);
        } else {
            gbc.gridy = 0;
            JLabel lblInfo = new JLabel("🔐 GESTIÓN DE CONTRASEÑA");
            lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblInfo.setForeground(new Color(33, 97, 140));
            panel.add(lblInfo, gbc);
            
            gbc.gridy = 1;
            btnCambiarPass = new JButton(" Cambiar Contraseña");
            btnCambiarPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnCambiarPass.setBackground(new Color(52, 152, 219));
            btnCambiarPass.setForeground(Color.WHITE);
            btnCambiarPass.setFocusPainted(false);
            btnCambiarPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCambiarPass.setPreferredSize(new Dimension(280, 45));
            btnCambiarPass.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mostrarDialogoCambiarContrasena();
                }
            });
            panel.add(btnCambiarPass, gbc);
        }
        
        gbc.gridy = 4;
        JPanel infoSeguridad = new JPanel(new BorderLayout());
        infoSeguridad.setBackground(new Color(255, 248, 225));
        infoSeguridad.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(241, 196, 15), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        JLabel lblInfoSeguridad = new JLabel("<html> <b>Recomendaciones de seguridad:</b><br>" +
          "• La contraseña debe tener al menos 6 caracteres<br>" +
          "• Combina letras mayúsculas, minúsculas y números<br>" +
          "• No compartas tu contraseña con nadie</html>");
        lblInfoSeguridad.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoSeguridad.add(lblInfoSeguridad, BorderLayout.CENTER);
        
        gbc.gridy = 5;
        panel.add(infoSeguridad, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelObservaciones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(Color.WHITE);
        
        btnRegistrarError = new JButton(" Registrar Incidencia");
        btnRegistrarError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrarError.setBackground(new Color(241, 196, 15));
        btnRegistrarError.setForeground(new Color(33, 33, 33));
        btnRegistrarError.setFocusPainted(false);
        btnRegistrarError.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrarError.setPreferredSize(new Dimension(170, 38));
        btnRegistrarError.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarError();
            }
        });
        topPanel.add(btnRegistrarError);
        
        JLabel lblObservaciones = new JLabel(" REGISTRO DE OBSERVACIONES");
        lblObservaciones.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblObservaciones.setForeground(new Color(52, 73, 94));
        lblObservaciones.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        
        txtObservaciones = new JTextArea(10, 40);
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservaciones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        
        JPanel obsBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        obsBotones.setBackground(Color.WHITE);
        
        JButton btnGuardarObs = new JButton(" Guardar Nota");
        btnGuardarObs.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnGuardarObs.setBackground(new Color(39, 174, 96));
        btnGuardarObs.setForeground(Color.WHITE);
        btnGuardarObs.setFocusPainted(false);
        btnGuardarObs.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardarObs.setPreferredSize(new Dimension(120, 30));
        btnGuardarObs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarObservacion();
            }
        });
        
        JButton btnLimpiarObs = new JButton(" Limpiar");
        btnLimpiarObs.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLimpiarObs.setBackground(new Color(192, 57, 43));
        btnLimpiarObs.setForeground(Color.WHITE);
        btnLimpiarObs.setFocusPainted(false);
        btnLimpiarObs.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiarObs.setPreferredSize(new Dimension(100, 30));
        btnLimpiarObs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtObservaciones.setText("");
            }
        });
        
        obsBotones.add(btnGuardarObs);
        obsBotones.add(btnLimpiarObs);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollObservaciones, BorderLayout.CENTER);
        panel.add(obsBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Border crearBordeCampo() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List usuarios;
            if (tipoFiltro != null) {
                usuarios = usuarioLogica.listarPorTipo(tipoFiltro);
            } else {
                usuarios = usuarioLogica.obtenerTodos();
            }
            ordenarUsuarios(usuarios);
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = (Usuario) usuarios.get(i);
                modeloTabla.addRow(new Object[]{
                    Integer.valueOf(u.getIdUsuario()), u.getNombre(), u.getApellido(),
                    u.getEmail(), u.getTipoUsuario(), u.getEstado()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + e.getMessage());
        }
    }
    
    private void buscar() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        modeloTabla.setRowCount(0);
        try {
            List usuarios;
            if (tipoFiltro != null) {
                usuarios = usuarioLogica.listarPorTipo(tipoFiltro);
            } else {
                usuarios = usuarioLogica.obtenerTodos();
            }
            ordenarUsuarios(usuarios);
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = (Usuario) usuarios.get(i);
                if (u.getNombre().toLowerCase().indexOf(busqueda) >= 0 || 
                    u.getApellido().toLowerCase().indexOf(busqueda) >= 0 ||
                    u.getEmail().toLowerCase().indexOf(busqueda) >= 0) {
                    modeloTabla.addRow(new Object[]{
                        Integer.valueOf(u.getIdUsuario()), u.getNombre(), u.getApellido(),
                        u.getEmail(), u.getTipoUsuario(), u.getEstado()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en búsqueda: " + e.getMessage());
        }
    }
    
    private void cargarUsuarioSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
            try {
                Usuario u = usuarioLogica.obtenerPorId(idSeleccionado);
                if (u != null) {
                    txtNombre.setText(u.getNombre());
                    txtApellido.setText(u.getApellido());
                    txtEmail.setText(u.getEmail());
                    txtTelefono.setText(u.getTelefono());
                    txtDocumento.setText(u.getDocumento());
                    cmbTipoUsuario.setSelectedItem(u.getTipoUsuario());
                    btnGuardar.setText(" Actualizar usuario");
                    editando = true;
                    cargarObservaciones(idSeleccionado);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    private void cargarDatosUsuario() {
        if (usuarioEditar != null) {
            idSeleccionado = usuarioEditar.getIdUsuario();
            txtNombre.setText(usuarioEditar.getNombre());
            txtApellido.setText(usuarioEditar.getApellido());
            txtEmail.setText(usuarioEditar.getEmail());
            txtTelefono.setText(usuarioEditar.getTelefono());
            txtDocumento.setText(usuarioEditar.getDocumento());
            cmbTipoUsuario.setSelectedItem(usuarioEditar.getTipoUsuario());
            btnGuardar.setText(" Actualizar usuario");
            cargarObservaciones(idSeleccionado);
        }
    }
    
    private void guardar() {
        try {
            if (txtNombre.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre");
                txtNombre.requestFocus();
                return;
            }
            if (txtApellido.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese el apellido");
                txtApellido.requestFocus();
                return;
            }
            if (txtEmail.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese el email");
                txtEmail.requestFocus();
                return;
            }
            
            Usuario u = new Usuario();
            u.setNombre(txtNombre.getText().trim());
            u.setApellido(txtApellido.getText().trim());
            u.setEmail(txtEmail.getText().trim());
            u.setTelefono(txtTelefono.getText().trim());
            u.setDocumento(txtDocumento.getText().trim());
            u.setTipoUsuario((String) cmbTipoUsuario.getSelectedItem());
            
            boolean exito;
            if (idSeleccionado > 0) {
                u.setIdUsuario(idSeleccionado);
                exito = usuarioLogica.actualizar(u);
                if (exito) JOptionPane.showMessageDialog(this, " Usuario actualizado correctamente");
            } else {
                String pass = "";
                if (txtContrasena != null) {
                    pass = new String(txtContrasena.getPassword());
                }
                if (pass.equals("")) {
                    JOptionPane.showMessageDialog(this, "Ingrese una contraseña");
                    return;
                }
                if (txtConfirmarContrasena != null) {
                    String confirm = new String(txtConfirmarContrasena.getPassword());
                    if (!pass.equals(confirm)) {
                        JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
                        return;
                    }
                }
                u.setContrasena(pass);
                exito = usuarioLogica.registrar(u);
                if (exito) JOptionPane.showMessageDialog(this, " Usuario registrado correctamente");
            }
            
            if (exito) {
                limpiarFormulario();
                cargarTabla();
                if (idSeleccionado > 0) {
                    dispose();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private int prioridadEstadoUsuario(String estado) {
        return DBHelper.prioridadEstado(estado);
    }

    private void ordenarUsuarios(List usuarios) {
        try {
            Collections.sort(usuarios, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Usuario a = (Usuario) o1;
                    Usuario b = (Usuario) o2;
                    int pa = prioridadEstadoUsuario(a.getEstado());
                    int pb = prioridadEstadoUsuario(b.getEstado());
                    if (pa != pb) return pa - pb;
                    String na = (a.getNombre() + " " + a.getApellido()).toUpperCase();
                    String nb = (b.getNombre() + " " + b.getApellido()).toUpperCase();
                    return na.compareTo(nb);
                }
            });
        } catch (Exception ex) { }
    }

    private void activarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para activar");
            return;
        }
        int id = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
        try {
            if (usuarioLogica.activarUsuario(id)) {
                JOptionPane.showMessageDialog(this, "Usuario activado correctamente");
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo activar el usuario");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar");
            return;
        }
        
        int id = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
          "¿Inactivar al usuario " + nombre + "?\nEl usuario quedará en estado INACTIVO y no podrá iniciar sesión.",
          "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (usuarioLogica.desactivar(id)) {
                    JOptionPane.showMessageDialog(this, " Usuario inactivado correctamente");
                    cargarTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al eliminar el usuario");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    private void eliminarDefinitivoUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar definitivamente");
            return;
        }
        int id = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
          "¿Eliminar DEFINITIVAMENTE al usuario " + nombre + "?\nOracle no lo permitirá si tiene matrículas, evaluaciones o registros relacionados.\nPara entrega se recomienda usar Inactivar.",
          "Confirmar eliminación definitiva", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (DBHelper.borrar("USUARIO", "ID_USUARIO", Integer.valueOf(id))) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado definitivamente");
                    cargarTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar definitivamente. Puede tener datos relacionados; use Inactivar.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDocumento.setText("");
        if (txtContrasena != null) txtContrasena.setText("");
        if (txtConfirmarContrasena != null) txtConfirmarContrasena.setText("");
        txtObservaciones.setText("");
        cmbTipoUsuario.setSelectedIndex(0);
        btnGuardar.setText(" Crear usuario");
        idSeleccionado = -1;
        editando = false;
        txtNombre.requestFocus();
    }
    
    private void mostrarDialogoCambiarContrasena() {
        int id = idSeleccionado > 0 ? idSeleccionado : (usuarioEditar != null ? usuarioEditar.getIdUsuario() : 0);
        if (id <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario primero");
            return;
        }
        
        final JPasswordField passField = new JPasswordField(15);
        final JPasswordField confirmField = new JPasswordField(15);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nueva Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Confirmar:"), gbc);
        gbc.gridx = 1;
        panel.add(confirmField, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, " Cambiar Contraseña", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String pass = new String(passField.getPassword());
            String confirm = new String(confirmField.getPassword());
            
            if (pass.equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese una contraseña");
                return;
            }
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
                return;
            }
            if (pass.length() < 4) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres");
                return;
            }
            
            try {
                if (usuarioLogica.cambiarContrasena(id, pass)) {
                    JOptionPane.showMessageDialog(this, " Contraseña cambiada exitosamente");
                    registrarEvento("Cambio de contraseña para usuario ID: " + id);
                } else {
                    JOptionPane.showMessageDialog(this, " Error al cambiar la contraseña");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    private void registrarError() {
        String usuario = (idSeleccionado > 0) ? "Usuario ID: " + idSeleccionado : "Nuevo usuario";
        
        final JTextArea errorArea = new JTextArea(5, 40);
        errorArea.setLineWrap(true);
        errorArea.setWrapStyleWord(true);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Describa la incidencia o error:"), BorderLayout.NORTH);
        panel.add(new JScrollPane(errorArea), BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(this, panel, " Registrar Incidencia", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION && !errorArea.getText().trim().equals("")) {
            String mensaje = errorArea.getText().trim();
            registrarEvento("INCIDENCIA - " + usuario + ": " + mensaje);
            JOptionPane.showMessageDialog(this, " Incidencia registrada correctamente");
        }
    }
    
    private void guardarObservacion() {
        if (idSeleccionado <= 0 && usuarioEditar == null) {
            JOptionPane.showMessageDialog(this, "Primero guarde el usuario para agregar observaciones");
            return;
        }
        
        String observacion = txtObservaciones.getText().trim();
        if (observacion.equals("")) {
            JOptionPane.showMessageDialog(this, "No hay observaciones para guardar");
            return;
        }
        
        int id = idSeleccionado > 0 ? idSeleccionado : usuarioEditar.getIdUsuario();
        registrarEvento("OBSERVACIÓN - Usuario ID " + id + ": " + observacion);
        JOptionPane.showMessageDialog(this, " Observación guardada correctamente");
    }
    
    private void cargarObservaciones(int idUsuario) {
        String archivo = "observaciones_usuario_" + idUsuario + ".txt";
        File file = new File(archivo);
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                try {
                    StringBuffer sb = new StringBuffer();
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        sb.append(linea).append("\n");
                    }
                    txtObservaciones.setText(sb.toString());
                } finally {
                    br.close();
                }
            } catch (IOException e) {
                txtObservaciones.setText("");
            }
        } else {
            txtObservaciones.setText("");
        }
    }
    
    private void registrarEvento(String evento) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = sdf.format(new Date());
        String registro = "[" + fecha + "] " + evento + "\n";
        
        try {
            FileWriter fw = new FileWriter("eventos_sistema.log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(registro);
            out.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.err.println("Error al registrar evento: " + e.getMessage());
        }
        
        System.out.print(registro);
    }
}