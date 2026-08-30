package com.gestionpracticas.vista.director;

import com.gestionpracticas.logica.CursoLogica;
import com.gestionpracticas.logica.ProgramaLogica;
import com.gestionpracticas.modelo.Curso;
import com.gestionpracticas.modelo.Programa;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import com.gestionpracticas.util.DBHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class CursoForm extends JFrame {
    
    private final Usuario usuario;
    private final CursoLogica cursoLogica;
    private final ProgramaLogica programaLogica;
    
    // Componentes del formulario
    private JTextField txtNombre, txtCodigo, txtCreditos, txtDescripcion, txtBuscar;
    private JComboBox cmbPrograma;
    private JButton btnGuardar, btnLimpiar, btnEliminar, btnActivar, btnRefrescar, btnBuscar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    
    private List listaProgramas;
    private int idSeleccionado = -1;
    
    public CursoForm(Usuario usuario) {
        this.usuario = usuario;
        this.cursoLogica = new CursoLogica();
        this.programaLogica = new ProgramaLogica();
        initComponents();
        cargarCombos();
        cargarTabla();
    }
    
    private void initComponents() {
        setTitle("Gestión de Cursos - " + usuario.getNombre() + " " + usuario.getApellido());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        getContentPane().setBackground(new Color(240, 242, 245));
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 242, 245));
        
        // Panel superior con título
        JPanel header = crearHeader();
        mainPanel.add(header, BorderLayout.NORTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.4);
        
        // Panel izquierdo - Tabla de cursos
        JPanel panelTabla = crearPanelTabla();
        splitPane.setLeftComponent(panelTabla);
        
        // Panel derecho - Formulario
        JPanel panelFormulario = crearPanelFormulario();
        splitPane.setRightComponent(panelFormulario);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Panel inferior - Botones
        JPanel panelBotones = crearPanelBotones();
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 97, 140));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("📖 GESTIÓN DE CURSOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        
        JLabel lblUsuario = new JLabel(" " + usuario.getNombre() + " " + usuario.getApellido());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(new Color(220, 220, 220));
        
        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblUsuario, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.setBackground(Color.WHITE);
        
        JLabel lblBuscar = new JLabel(" Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.setPreferredSize(new Dimension(200, 32));
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });
        
        btnRefrescar = new JButton(" Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
                limpiarFormulario();
            }
        });
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(Box.createHorizontalStrut(20));
        panelBusqueda.add(btnRefrescar);
        
        // Tabla
        String[] columnas = {"ID", "Nombre", "Código", "Créditos", "Programa", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(32);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(33, 97, 140));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(52, 152, 219, 50));
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(3).setMaxWidth(70);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(5).setMaxWidth(80);
        
        // Popup menu
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editarItem = new JMenuItem(" Editar Curso");
        JMenuItem activarItem = new JMenuItem(" Activar Curso");
        JMenuItem eliminarItem = new JMenuItem(" Inactivar Curso");
        popupMenu.add(editarItem);
        popupMenu.add(activarItem);
        popupMenu.add(eliminarItem);
        
        tabla.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tabla.rowAtPoint(e.getPoint());
                    tabla.setRowSelectionInterval(row, row);
                    popupMenu.show(tabla, e.getX(), e.getY());
                }
                if (e.getClickCount() == 2) {
                    cargarCursoSeleccionado();
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
                cargarCursoSeleccionado();
            }
        });
        
        activarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoCurso("ACTIVO");
            }
        });

        eliminarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoCurso("INACTIVO");
            }
        });
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Cursos Registrados"));
        
        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Título del formulario
        JLabel lblTituloForm = new JLabel(" REGISTRAR NUEVO CURSO");
        lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloForm.setForeground(new Color(33, 97, 140));
        lblTituloForm.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloForm.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Panel de campos
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        Dimension fieldSize = new Dimension(280, 35);
        
        // Nombre
        gbc.gridy = 0;
        JLabel lblNombre = new JLabel("NOMBRE DEL CURSO");
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
        JLabel lblCodigo = new JLabel("CÓDIGO DEL CURSO");
        lblCodigo.setFont(labelFont);
        lblCodigo.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblCodigo, gbc);
        
        gbc.gridy = 3;
        txtCodigo = new JTextField();
        txtCodigo.setFont(fieldFont);
        txtCodigo.setPreferredSize(fieldSize);
        txtCodigo.setBorder(crearBordeCampo());
        camposPanel.add(txtCodigo, gbc);
        
        // Créditos
        gbc.gridy = 4;
        JLabel lblCreditos = new JLabel("CRÉDITOS");
        lblCreditos.setFont(labelFont);
        lblCreditos.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblCreditos, gbc);
        
        gbc.gridy = 5;
        txtCreditos = new JTextField();
        txtCreditos.setFont(fieldFont);
        txtCreditos.setPreferredSize(fieldSize);
        txtCreditos.setBorder(crearBordeCampo());
        camposPanel.add(txtCreditos, gbc);
        
        // Programa
        gbc.gridy = 6;
        JLabel lblPrograma = new JLabel("PROGRAMA ACADÉMICO");
        lblPrograma.setFont(labelFont);
        lblPrograma.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblPrograma, gbc);
        
        gbc.gridy = 7;
        cmbPrograma = new JComboBox();
        cmbPrograma.setFont(fieldFont);
        cmbPrograma.setPreferredSize(fieldSize);
        cmbPrograma.setBackground(Color.WHITE);
        cmbPrograma.setBorder(crearBordeCampo());
        camposPanel.add(cmbPrograma, gbc);
        
        // Descripción
        gbc.gridy = 8;
        JLabel lblDescripcion = new JLabel("DESCRIPCIÓN");
        lblDescripcion.setFont(labelFont);
        lblDescripcion.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblDescripcion, gbc);
        
        gbc.gridy = 9;
        txtDescripcion = new JTextField();
        txtDescripcion.setFont(fieldFont);
        txtDescripcion.setPreferredSize(fieldSize);
        txtDescripcion.setBorder(crearBordeCampo());
        camposPanel.add(txtDescripcion, gbc);
        
        panel.add(lblTituloForm, BorderLayout.NORTH);
        panel.add(camposPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(240, 242, 245));
        
        btnGuardar = new JButton(" Guardar Curso");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(150, 42));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarCurso();
            }
        });
        
        btnLimpiar = new JButton(" Limpiar");
        btnLimpiar.setBackground(new Color(52, 152, 219));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.setPreferredSize(new Dimension(150, 42));
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        
        btnActivar = new JButton(" Activar Curso");
        btnActivar.setBackground(new Color(22, 160, 133));
        btnActivar.setForeground(Color.WHITE);
        btnActivar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnActivar.setFocusPainted(false);
        btnActivar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActivar.setPreferredSize(new Dimension(150, 42));
        btnActivar.setEnabled(false);
        btnActivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoCurso("ACTIVO");
            }
        });

        btnEliminar = new JButton(" Inactivar Curso");
        btnEliminar.setBackground(new Color(243, 156, 18));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(150, 42));
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCurso();
            }
        });
        
        JButton btnCerrar = new JButton(" Cerrar");
        btnCerrar.setBackground(new Color(192, 57, 43));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(150, 42));
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panel.add(btnGuardar);
        panel.add(btnLimpiar);
        panel.add(btnActivar);
        panel.add(btnEliminar);
        panel.add(btnCerrar);
        
        // Habilitar botón eliminar cuando se selecciona una fila
        tabla.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                boolean seleccionado = tabla.getSelectedRow() >= 0;
                btnEliminar.setEnabled(seleccionado);
                if (btnActivar != null) btnActivar.setEnabled(seleccionado);
            }
        });
        
        return panel;
    }
    
    private Border crearBordeCampo() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }
    
    private void cargarCombos() {
        try {
            listaProgramas = programaLogica.obtenerTodos();
            cmbPrograma.removeAllItems();
            if (listaProgramas != null && listaProgramas.size() > 0) {
                for (int i = 0; i < listaProgramas.size(); i++) {
                    Programa p = (Programa) listaProgramas.get(i);
                    cmbPrograma.addItem(p.getNombre() + (p.getCodigo() != null && !p.getCodigo().trim().equals("") ? " (" + p.getCodigo() + ")" : ""));
                }
            } else {
                cmbPrograma.addItem("No hay programas disponibles");
                cmbPrograma.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando programas: " + e.getMessage());
        }
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List cursos = cursoLogica.listarTodos();
            ordenarCursos(cursos);
            for (int i = 0; i < cursos.size(); i++) {
                Curso c = (Curso) cursos.get(i);
                String programa = c.getPrograma() != null ? c.getPrograma().getNombre() : "N/A";
                modeloTabla.addRow(new Object[]{
                    Integer.valueOf(c.getIdCurso()), c.getNombre(), c.getCodigo(),
                    Integer.valueOf(c.getCreditos()), programa, c.getEstado()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando cursos: " + e.getMessage());
        }
    }
    
    private void buscar() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        modeloTabla.setRowCount(0);
        try {
            List cursos = cursoLogica.listarTodos();
            ordenarCursos(cursos);
            for (int i = 0; i < cursos.size(); i++) {
                Curso c = (Curso) cursos.get(i);
                String programa = c.getPrograma() != null ? c.getPrograma().getNombre() : "N/A";
                if (c.getNombre().toLowerCase().indexOf(busqueda) >= 0 ||
                    c.getCodigo().toLowerCase().indexOf(busqueda) >= 0) {
                    modeloTabla.addRow(new Object[]{
                        Integer.valueOf(c.getIdCurso()), c.getNombre(), c.getCodigo(),
                        Integer.valueOf(c.getCreditos()), programa, c.getEstado()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en búsqueda: " + e.getMessage());
        }
    }
    
    private void cargarCursoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
            txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
            txtCodigo.setText((String) modeloTabla.getValueAt(fila, 2));
            txtCreditos.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
            btnGuardar.setText("Actualizar Curso");
            btnEliminar.setEnabled(true);
            
            // Seleccionar programa correspondiente
            String programaNombre = (String) modeloTabla.getValueAt(fila, 4);
            for (int i = 0; i < cmbPrograma.getItemCount(); i++) {
                String item = (String) cmbPrograma.getItemAt(i);
                if (item.indexOf(programaNombre) >= 0) {
                    cmbPrograma.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void guardarCurso() {
        try {
            // Validaciones
            if (txtNombre.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre del curso");
                txtNombre.requestFocus();
                return;
            }
            if (txtCodigo.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese el código del curso");
                txtCodigo.requestFocus();
                return;
            }
            if (txtCreditos.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Ingrese los créditos");
                txtCreditos.requestFocus();
                return;
            }
            
            int creditos = Integer.parseInt(txtCreditos.getText().trim());
            if (creditos <= 0 || creditos > 10) {
                JOptionPane.showMessageDialog(this, "Los créditos deben estar entre 1 y 10");
                txtCreditos.requestFocus();
                return;
            }
            
            int idxPrograma = cmbPrograma.getSelectedIndex();
            if (idxPrograma < 0 || listaProgramas == null || listaProgramas.size() == 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un programa");
                return;
            }
            
            Curso curso = new Curso();
            curso.setNombre(txtNombre.getText().trim());
            curso.setCodigo(txtCodigo.getText().trim());
            curso.setCreditos(creditos);
            curso.setDescripcion(txtDescripcion.getText().trim());
            curso.setPrograma((Programa) listaProgramas.get(idxPrograma));
            curso.setEstado(Utilidades.ESTADO_ACTIVO);
            
            boolean exito;
            if (idSeleccionado > 0) {
                curso.setIdCurso(idSeleccionado);
                exito = cursoLogica.actualizar(curso);
                if (exito) JOptionPane.showMessageDialog(this, " Curso actualizado correctamente");
            } else {
                exito = cursoLogica.guardar(curso);
                if (exito) JOptionPane.showMessageDialog(this, " Curso guardado correctamente");
            }
            
            if (exito) {
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, " Error al guardar el curso");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los créditos deben ser un número válido");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void ordenarCursos(List cursos) {
        try {
            Collections.sort(cursos, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Curso a = (Curso) o1;
                    Curso b = (Curso) o2;
                    int pa = DBHelper.prioridadEstado(a.getEstado());
                    int pb = DBHelper.prioridadEstado(b.getEstado());
                    if (pa != pb) return pa - pb;
                    return a.getNombre().compareToIgnoreCase(b.getNombre());
                }
            });
        } catch (Exception ex) { }
    }

    private void cambiarEstadoCurso(String estado) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para cambiar estado");
            return;
        }
        int id = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
        try {
            LinkedHashMap datos = new LinkedHashMap();
            datos.put("ESTADO", estado);
            if (DBHelper.actualizar("CURSO", "ID_CURSO", Integer.valueOf(id), datos)) {
                JOptionPane.showMessageDialog(this, "Curso actualizado a " + estado);
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo cambiar el estado del curso");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarCurso() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para inactivar");
            return;
        }
        
        int id = ((Integer) modeloTabla.getValueAt(fila, 0)).intValue();
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
          "¿Inactivar el curso '" + nombre + "'?\nEl curso quedará oculto como inactivo, pero no se borra de la base de datos.",
          "Confirmar inactivación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (cursoLogica.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, " Curso inactivado correctamente");
                    limpiarFormulario();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al inactivar el curso");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCodigo.setText("");
        txtCreditos.setText("");
        txtDescripcion.setText("");
        if (cmbPrograma.getItemCount() > 0) cmbPrograma.setSelectedIndex(0);
        btnGuardar.setText("Guardar Curso");
        idSeleccionado = -1;
        btnEliminar.setEnabled(false);
        if (btnActivar != null) btnActivar.setEnabled(false);
        txtNombre.requestFocus();
    }
}