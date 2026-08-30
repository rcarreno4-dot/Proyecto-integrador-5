package com.gestionpracticas.vista.coordinador;

import com.gestionpracticas.logica.AsignacionDocenteLogica;
import com.gestionpracticas.logica.PracticaLogica;
import com.gestionpracticas.logica.UsuarioLogica;
import com.gestionpracticas.modelo.AsignacionDocente;
import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionForm extends JFrame {

    private final AsignacionDocenteLogica logica;
    private final PracticaLogica practicaLogica;
    private final UsuarioLogica usuarioLogica;

    private JComboBox<String> cmbPractica, cmbDocente;
    private JTextField txtRol;
    private JButton btnAsignar, btnEliminar, btnRefrescar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private List<Practica> listaPracticas;
    private List<Usuario> listaDocentes;
    private Usuario usuarioActual;

    public AsignacionForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.logica = new AsignacionDocenteLogica();
        this.practicaLogica = new PracticaLogica();
        this.usuarioLogica = new UsuarioLogica();
        initComponents();
        cargarCombos();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Asignación de Docentes a Prácticas - " + usuarioActual.getNombre());
        setSize(950, 600);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Nueva Asignación"));
        form.setPreferredSize(new Dimension(350, 450));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel lblTitulo = new JLabel("Asignar Docente a Práctica");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(33, 97, 140));
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 8, 20, 8);
        form.add(lblTitulo, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(7, 8, 2, 8);
        form.add(new JLabel("Práctica:"), gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 8, 7, 8);
        cmbPractica = new JComboBox<>();
        cmbPractica.setPreferredSize(new Dimension(250, 35));
        form.add(cmbPractica, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(7, 8, 2, 8);
        form.add(new JLabel("Docente:"), gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 8, 7, 8);
        cmbDocente = new JComboBox<>();
        cmbDocente.setPreferredSize(new Dimension(250, 35));
        form.add(cmbDocente, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(7, 8, 2, 8);
        form.add(new JLabel("Rol en la práctica:"), gbc);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 8, 7, 8);
        txtRol = new JTextField();
        txtRol.setPreferredSize(new Dimension(250, 35));
        form.add(txtRol, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnAsignar = new JButton(" Asignar");
        btnAsignar.setBackground(new Color(39, 174, 96));
        btnAsignar.setForeground(Color.WHITE);
        btnAsignar.setFocusPainted(false);
        btnAsignar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAsignar.setPreferredSize(new Dimension(120, 40));
        
        btnRefrescar = new JButton(" Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setPreferredSize(new Dimension(120, 40));
        
        panelBotones.add(btnAsignar);
        panelBotones.add(btnRefrescar);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 8, 15, 8);
        form.add(panelBotones, gbc);

        String[] cols = {"ID", "Práctica", "Docente", "Rol", "Fecha Asignación", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { 
                return false; 
            }
        };
        
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(5).setMaxWidth(100);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnEliminar = new JButton(" Eliminar Asignación");
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setEnabled(false);
        btnEliminar.setPreferredSize(new Dimension(180, 35));
        panelSur.add(btnEliminar);
        
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Asignaciones Actuales"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, scrollTabla);
        split.setDividerLocation(370);
        split.setResizeWeight(0.3);
        
        mainPanel.add(split, BorderLayout.CENTER);
        mainPanel.add(panelSur, BorderLayout.SOUTH);
        
        add(mainPanel);

        btnAsignar.addActionListener(e -> asignar());
        btnEliminar.addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> {
            cargarCombos();
            cargarTabla();
        });
        cmbPractica.addActionListener(e -> cargarTabla());
        
        tabla.getSelectionModel().addListSelectionListener(e -> {
            btnEliminar.setEnabled(tabla.getSelectedRow() >= 0);
        });
    }

    private void cargarCombos() {
        try {
            listaPracticas = practicaLogica.obtenerTodas();
            cmbPractica.removeAllItems();
            if (listaPracticas != null && !listaPracticas.isEmpty()) {
                for (Practica p : listaPracticas) {
                    cmbPractica.addItem(p.getTitulo());
                }
            } else {
                cmbPractica.addItem("No hay prácticas disponibles");
                cmbPractica.setEnabled(false);
            }

            List<Usuario> todosUsuarios = usuarioLogica.obtenerTodos();
            listaDocentes = new ArrayList<>();
            cmbDocente.removeAllItems();
            
            if (todosUsuarios != null) {
                for (Usuario u : todosUsuarios) {
                    if (u != null && Utilidades.TIPO_DOCENTE.equals(u.getTipoUsuario()) && Utilidades.ESTADO_ACTIVO.equals(u.getEstado())) {
                        listaDocentes.add(u);
                        cmbDocente.addItem(u.getNombre() + " " + u.getApellido() + " (" + u.getEmail() + ")");
                    }
                }
            }
            
            if (listaDocentes == null || listaDocentes.isEmpty()) {
                cmbDocente.addItem("No hay docentes disponibles");
                cmbDocente.setEnabled(false);
            } else {
                cmbDocente.setEnabled(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar combos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        
        if (listaPracticas == null || listaPracticas.isEmpty()) {
            return;
        }
        
        int idx = cmbPractica.getSelectedIndex();
        if (idx < 0 || idx >= listaPracticas.size()) {
            return;
        }
        
        try {
            int idPractica = listaPracticas.get(idx).getIdPractica();
            List<AsignacionDocente> asignaciones = logica.listarPorPractica(idPractica);
            
            if (asignaciones != null) {
                for (AsignacionDocente a : asignaciones) {
                    String docNombre = "";
                    if (a.getUsuarios() != null && !a.getUsuarios().isEmpty() && a.getUsuarios().get(0) != null) {
                        Usuario docente = a.getUsuarios().get(0);
                        docNombre = docente.getNombre() + " " + docente.getApellido();
                    }
                    
                    String tituloPractica = a.getPractica() != null ? a.getPractica().getTitulo() : "";
                    String rol = a.getRolEnPractica() != null ? a.getRolEnPractica() : "";
                    String fecha = a.getFechaAsignacion() != null ? a.getFechaAsignacion().toString() : "";
                    String estado = a.getEstado() != null ? a.getEstado() : "ACTIVO";
                    
                    modeloTabla.addRow(new Object[]{
                        a.getIdAsignacionDocente(),
                        tituloPractica,
                        docNombre,
                        rol,
                        fecha,
                        estado
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asignar() {
        try {
            if (txtRol.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el rol del docente.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtRol.requestFocus();
                return;
            }
            
            int idxP = cmbPractica.getSelectedIndex();
            if (idxP < 0 || listaPracticas == null || listaPracticas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona una práctica válida.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int idxD = cmbDocente.getSelectedIndex();
            if (idxD < 0 || listaDocentes == null || listaDocentes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona un docente válido.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
              "¿Asignar a " + listaDocentes.get(idxD).getNombre() + " como " + txtRol.getText().trim() + "?",
              "Confirmar Asignación",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            AsignacionDocente a = new AsignacionDocente();
            a.setPractica(listaPracticas.get(idxP));
            a.setRolEnPractica(txtRol.getText().trim());
            a.setEstado("ACTIVO");
            
            List<Usuario> seleccionados = new ArrayList<>();
            seleccionados.add(listaDocentes.get(idxD));
            a.setUsuarios(seleccionados);
            
            if (logica.asignar(a)) {
                JOptionPane.showMessageDialog(this, " Docente asignado correctamente.");
                txtRol.setText("");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, " Error al asignar el docente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una asignación para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String docente = (String) modeloTabla.getValueAt(fila, 2);
        String practica = (String) modeloTabla.getValueAt(fila, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
          "¿Eliminar la asignación de " + docente + " en la práctica " + practica + "?\nEsta acción no se puede deshacer.", 
          "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (logica.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, " Asignación eliminada correctamente.");
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al eliminar la asignación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}