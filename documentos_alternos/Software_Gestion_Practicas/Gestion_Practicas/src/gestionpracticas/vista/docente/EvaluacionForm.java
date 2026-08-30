package com.gestionpracticas.vista.docente;

import com.gestionpracticas.logica.EvaluacionLogica;
import com.gestionpracticas.logica.MatriculaPracticaLogica;
import com.gestionpracticas.logica.RubricaLogica;
import com.gestionpracticas.modelo.Evaluacion;
import com.gestionpracticas.modelo.MatriculaPractica;
import com.gestionpracticas.modelo.Rubrica;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class EvaluacionForm extends JFrame {

    private final Usuario usuarioActual;
    private final EvaluacionLogica evaluacionLogica;
    private final MatriculaPracticaLogica matriculaLogica;
    private final RubricaLogica rubricaLogica;

    // Componentes
    private JComboBox<String> cmbMatricula, cmbRubrica;
    private JTextField txtPuntaje, txtObservaciones;
    private JButton btnGuardar, btnRefrescar, btnConfigPantalla;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JPanel mainPanel;
    private JSplitPane split;

    private List<MatriculaPractica> listaMatriculas;
    private List<Rubrica> listaRubricas;

    public EvaluacionForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.evaluacionLogica = new EvaluacionLogica();
        this.matriculaLogica = new MatriculaPracticaLogica();
        this.rubricaLogica = new RubricaLogica();
        initComponents();
        cargarCombos();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Registrar Evaluación - " + usuarioActual.getNombre());
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
        split.setDividerLocation(400);
        split.setResizeWeight(0.32);

        // Panel izquierdo - Formulario
        JPanel formPanel = crearPanelFormulario();
        split.setLeftComponent(formPanel);

        // Panel derecho - Tabla de evaluaciones
        JPanel tablaPanel = crearPanelTabla();
        split.setRightComponent(tablaPanel);

        mainPanel.add(split, BorderLayout.CENTER);

        // Panel inferior - Botones
        JPanel footerPanel = crearFooter();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 97, 140));
        header.setPreferredSize(new Dimension(1200, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel(" REGISTRO DE EVALUACIONES");
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
        JLabel lblTituloForm = new JLabel(" NUEVA EVALUACIÓN");
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
        Dimension fieldSize = new Dimension(280, 32);

        // Matrícula
        gbc.gridy = 0;
        JLabel lblMatricula = new JLabel("ESTUDIANTE / MATRÍCULA");
        lblMatricula.setFont(labelFont);
        lblMatricula.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblMatricula, gbc);

        gbc.gridy = 1;
        cmbMatricula = new JComboBox<>();
        cmbMatricula.setFont(fieldFont);
        cmbMatricula.setPreferredSize(fieldSize);
        cmbMatricula.setBorder(crearBordeCampo());
        camposPanel.add(cmbMatricula, gbc);

        // Rúbrica
        gbc.gridy = 2;
        JLabel lblRubrica = new JLabel("RÚBRICA DE EVALUACIÓN");
        lblRubrica.setFont(labelFont);
        lblRubrica.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblRubrica, gbc);

        gbc.gridy = 3;
        cmbRubrica = new JComboBox<>();
        cmbRubrica.setFont(fieldFont);
        cmbRubrica.setPreferredSize(fieldSize);
        cmbRubrica.setBorder(crearBordeCampo());
        camposPanel.add(cmbRubrica, gbc);

        // Puntaje
        gbc.gridy = 4;
        JLabel lblPuntaje = new JLabel("PUNTAJE OBTENIDO (0-100)");
        lblPuntaje.setFont(labelFont);
        lblPuntaje.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblPuntaje, gbc);

        gbc.gridy = 5;
        txtPuntaje = new JTextField();
        txtPuntaje.setFont(fieldFont);
        txtPuntaje.setPreferredSize(fieldSize);
        txtPuntaje.setBorder(crearBordeCampo());
        camposPanel.add(txtPuntaje, gbc);

        // Observaciones
        gbc.gridy = 6;
        JLabel lblObservaciones = new JLabel("OBSERVACIONES");
        lblObservaciones.setFont(labelFont);
        lblObservaciones.setForeground(new Color(52, 73, 94));
        camposPanel.add(lblObservaciones, gbc);

        gbc.gridy = 7;
        txtObservaciones = new JTextField();
        txtObservaciones.setFont(fieldFont);
        txtObservaciones.setPreferredSize(fieldSize);
        txtObservaciones.setBorder(crearBordeCampo());
        camposPanel.add(txtObservaciones, gbc);

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

        // Título de la tabla
        JLabel lblTablaTitulo = new JLabel(" EVALUACIONES REGISTRADAS");
        lblTablaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTablaTitulo.setForeground(new Color(33, 97, 140));
        lblTablaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Columnas de la tabla
        String[] cols = {"ID", "Estudiante", "Rúbrica", "Puntaje", "Estado", "Fecha"};
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
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setMaxWidth(70);
        tabla.getColumnModel().getColumn(4).setMaxWidth(100);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Evaluaciones"));

        panel.add(lblTablaTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(240, 242, 245));
        footer.setPreferredSize(new Dimension(1200, 45));
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Información adicional
        JLabel lblInfo = new JLabel("💡 Los puntajes se guardan automáticamente en el sistema");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);

        // Panel de configuración de pantalla
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
            
            // Ajustar el divider location proporcionalmente
            double proporcion = (double) ancho / 1200;
            int nuevoDivider = (int) (400 * proporcion);
            split.setDividerLocation(nuevoDivider);
            
            JOptionPane.showMessageDialog(this, "Pantalla configurada a " + resoluciones[idx]);
        }
    }

    private void cargarCombos() {
        try {
            listaMatriculas = matriculaLogica.obtenerTodas();
            cmbMatricula.removeAllItems();
            if (listaMatriculas != null && !listaMatriculas.isEmpty()) {
                for (MatriculaPractica m : listaMatriculas) {
                    String estudiante = m.getEstudiante() != null ? 
                        m.getEstudiante().getNombre() + " " + m.getEstudiante().getApellido() : "Sin nombre";
                    cmbMatricula.addItem("ID: " + m.getIdMatriculaPractica() + " - " + estudiante);
                }
            } else {
                cmbMatricula.addItem("No hay matrículas disponibles");
                cmbMatricula.setEnabled(false);
            }

            listaRubricas = rubricaLogica.listarTodas();
            cmbRubrica.removeAllItems();
            if (listaRubricas != null && !listaRubricas.isEmpty()) {
                for (Rubrica r : listaRubricas) {
                    cmbRubrica.addItem(r.getNombre());
                }
            } else {
                cmbRubrica.addItem("No hay rúbricas disponibles");
                cmbRubrica.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Evaluacion> evaluaciones = evaluacionLogica.listarTodas();
            if (evaluaciones != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (Evaluacion ev : evaluaciones) {
                    String estudiante = ev.getMatricula() != null && ev.getMatricula().getEstudiante() != null ?
                        ev.getMatricula().getEstudiante().getNombre() + " " + ev.getMatricula().getEstudiante().getApellido() : "";
                    String rubrica = ev.getRubrica() != null ? ev.getRubrica().getNombre() : "";
                    String fecha = ev.getFechaEvaluacion() != null ? sdf.format(ev.getFechaEvaluacion()) : "";
                    
                    modeloTabla.addRow(new Object[]{
                        ev.getIdEvaluacion(),
                        estudiante,
                        rubrica,
                        ev.getPuntajeObtenido(),
                        ev.getEstado(),
                        fecha
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        try {
            if (txtPuntaje.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el puntaje obtenido.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtPuntaje.requestFocus();
                return;
            }
            
            double puntaje = Double.parseDouble(txtPuntaje.getText().trim());
            if (puntaje < 0 || puntaje > 100) {
                JOptionPane.showMessageDialog(this, "El puntaje debe estar entre 0 y 100.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtPuntaje.requestFocus();
                return;
            }
            
            int idxM = cmbMatricula.getSelectedIndex();
            if (idxM < 0 || listaMatriculas == null || listaMatriculas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una matrícula válida.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int idxR = cmbRubrica.getSelectedIndex();
            if (idxR < 0 || listaRubricas == null || listaRubricas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una rúbrica válida.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
              "¿Guardar evaluación con puntaje " + puntaje + "?",
              "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            Evaluacion ev = new Evaluacion();
            ev.setMatricula(listaMatriculas.get(idxM));
            ev.setRubrica(listaRubricas.get(idxR));
            ev.setEvaluador(usuarioActual);
            ev.setPuntajeObtenido(puntaje);
            ev.setObservaciones(txtObservaciones.getText().trim());
            ev.setEstado(Utilidades.ESTADO_REALIZADA);

            if (evaluacionLogica.registrarEvaluacion(ev)) {
                JOptionPane.showMessageDialog(this, " Evaluación registrada correctamente.");
                txtPuntaje.setText("");
                txtObservaciones.setText("");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, " Error al registrar la evaluación.", "Error", JOptionPane.ERROR_MESSAGE);
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
}