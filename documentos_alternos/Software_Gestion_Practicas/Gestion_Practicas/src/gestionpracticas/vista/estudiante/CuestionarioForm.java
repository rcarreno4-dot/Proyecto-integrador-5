package com.gestionpracticas.vista.estudiante;

import com.gestionpracticas.dao.PreguntaDAO;
import com.gestionpracticas.logica.MatriculaPracticaLogica;
import com.gestionpracticas.logica.EvaluacionLogica;
import com.gestionpracticas.modelo.*;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CuestionarioForm extends JFrame {

    private final Usuario usuarioActual;
    private final MatriculaPracticaLogica matriculaLogica;
    private final PreguntaDAO preguntaDAO;
    private final EvaluacionLogica evaluacionLogica;

    // Componentes
    private JComboBox<String> cmbMatricula;
    private JButton btnCargar, btnEnviar, btnLimpiar, btnConfigPantalla;
    private JPanel panelPreguntas;
    private JScrollPane scrollPreguntas;
    private JLabel lblTituloPractica;
    private JSplitPane split;
    private JPanel mainPanel;

    private List<MatriculaPractica> listaMatriculas;
    private List<Pregunta> listaPreguntas;
    private List<JComponent> camposRespuesta;
    private int idMatriculaSeleccionada = -1;

    public CuestionarioForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.matriculaLogica = new MatriculaPracticaLogica();
        this.preguntaDAO = new PreguntaDAO();
        this.evaluacionLogica = new EvaluacionLogica();
        this.camposRespuesta = new ArrayList<>();
        initComponents();
        cargarMatriculas();
    }

    private void initComponents() {
        setTitle("Cuestionario de Evaluación - " + usuarioActual.getNombre());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        getContentPane().setBackground(new Color(240, 242, 245));

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Header
        JPanel header = crearHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        // Split pane
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(350);
        split.setResizeWeight(0.28);

        // Panel izquierdo - Selección de práctica
        JPanel panelSeleccion = crearPanelSeleccion();
        split.setLeftComponent(panelSeleccion);

        // Panel derecho - Cuestionario
        JPanel panelCuestionario = crearPanelCuestionario();
        split.setRightComponent(panelCuestionario);

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

        JLabel lblTitulo = new JLabel(" CUESTIONARIO DE EVALUACIÓN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel(" Estudiante: " + usuarioActual.getNombre());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(new Color(220, 220, 220));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblUsuario, BorderLayout.EAST);

        return header;
    }

    private JPanel crearPanelSeleccion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTituloSel = new JLabel(" SELECCIONAR PRÁCTICA");
        lblTituloSel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloSel.setForeground(new Color(33, 97, 140));
        lblTituloSel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        gbc.gridy = 0;
        JLabel lblPractica = new JLabel("Práctica asignada:");
        lblPractica.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelCampos.add(lblPractica, gbc);

        gbc.gridy = 1;
        cmbMatricula = new JComboBox<>();
        cmbMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbMatricula.setPreferredSize(new Dimension(280, 35));
        cmbMatricula.setBorder(crearBordeCampo());
        panelCampos.add(cmbMatricula, gbc);

        gbc.gridy = 2;
        btnCargar = new JButton(" Cargar Cuestionario");
        btnCargar.setBackground(new Color(52, 152, 219));
        btnCargar.setForeground(Color.WHITE);
        btnCargar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCargar.setFocusPainted(false);
        btnCargar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCargar.setPreferredSize(new Dimension(280, 38));
        btnCargar.addActionListener(e -> cargarPreguntas());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnCargar);

        gbc.gridy = 3;
        panelCampos.add(panelBoton, gbc);

        // Información de la práctica
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(new Color(248, 249, 250));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblInfoTitulo = new JLabel("ℹ️ Información");
        lblInfoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelInfo.add(lblInfoTitulo, BorderLayout.NORTH);

        lblTituloPractica = new JLabel("Seleccione una práctica y presione 'Cargar'");
        lblTituloPractica.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTituloPractica.setForeground(Color.GRAY);
        panelInfo.add(lblTituloPractica, BorderLayout.CENTER);

        panel.add(lblTituloSel, BorderLayout.NORTH);
        panel.add(panelCampos, BorderLayout.CENTER);
        panel.add(panelInfo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelCuestionario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTituloCuest = new JLabel(" CUESTIONARIO");
        lblTituloCuest.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloCuest.setForeground(new Color(33, 97, 140));

        panelPreguntas = new JPanel();
        panelPreguntas.setLayout(new BoxLayout(panelPreguntas, BoxLayout.Y_AXIS));
        panelPreguntas.setBackground(Color.WHITE);
        panelPreguntas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPreguntas = new JScrollPane(panelPreguntas);
        scrollPreguntas.setBorder(BorderFactory.createTitledBorder("Preguntas"));

        // Panel de botones del cuestionario
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(Color.WHITE);

        btnEnviar = new JButton(" Enviar Respuestas");
        btnEnviar.setBackground(new Color(39, 174, 96));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviar.setPreferredSize(new Dimension(150, 38));
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(e -> enviarRespuestas());

        btnLimpiar = new JButton(" Limpiar");
        btnLimpiar.setBackground(new Color(192, 57, 43));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.setPreferredSize(new Dimension(120, 38));
        btnLimpiar.addActionListener(e -> limpiarCuestionario());

        panelBotones.add(btnEnviar);
        panelBotones.add(btnLimpiar);

        panel.add(lblTituloCuest, BorderLayout.NORTH);
        panel.add(scrollPreguntas, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(240, 242, 245));
        footer.setPreferredSize(new Dimension(1200, 40));
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblInfo = new JLabel("💡 Responde todas las preguntas para completar la evaluación");
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
            int nuevoDivider = (int) (350 * proporcion);
            split.setDividerLocation(nuevoDivider);

            JOptionPane.showMessageDialog(this, "Pantalla configurada a " + resoluciones[idx]);
        }
    }

    private void cargarMatriculas() {
        try {
            listaMatriculas = matriculaLogica.listarPorEstudiante(usuarioActual.getIdUsuario());
            cmbMatricula.removeAllItems();
            if (listaMatriculas != null && !listaMatriculas.isEmpty()) {
                for (MatriculaPractica m : listaMatriculas) {
                    String titulo = m.getPractica() != null ? m.getPractica().getTitulo() : "Práctica #" + m.getIdMatriculaPractica();
                    cmbMatricula.addItem(titulo);
                }
                cmbMatricula.setEnabled(true);
                btnCargar.setEnabled(true);
            } else {
                cmbMatricula.addItem("No hay prácticas asignadas");
                cmbMatricula.setEnabled(false);
                btnCargar.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando matrículas: " + e.getMessage());
        }
    }

    private void cargarPreguntas() {
        int idx = cmbMatricula.getSelectedIndex();
        if (idx < 0 || listaMatriculas == null || listaMatriculas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una práctica válida.");
            return;
        }

        MatriculaPractica matricula = listaMatriculas.get(idx);
        idMatriculaSeleccionada = matricula.getIdMatriculaPractica();
        String practicaTitulo = matricula.getPractica() != null ? matricula.getPractica().getTitulo() : "Práctica seleccionada";
        lblTituloPractica.setText("📘 " + practicaTitulo);

        try {
            listaPreguntas = preguntaDAO.listarPorPractica(matricula.getPractica().getIdPractica());
            if (listaPreguntas == null || listaPreguntas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay preguntas configuradas para esta práctica.");
                return;
            }

            panelPreguntas.removeAll();
            camposRespuesta.clear();

            for (int i = 0; i < listaPreguntas.size(); i++) {
                Pregunta p = listaPreguntas.get(i);
                JPanel pregPanel = crearPanelPregunta(i + 1, p.getTextoPregunta(), p.getTipoPregunta(), p.getPuntajeMaximo());
                panelPreguntas.add(pregPanel);
                panelPreguntas.add(Box.createVerticalStrut(10));
            }

            panelPreguntas.revalidate();
            panelPreguntas.repaint();
            btnEnviar.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando preguntas: " + e.getMessage());
        }
    }

    private JPanel crearPanelPregunta(int numero, String texto, String tipo, double puntajeMaximo) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel lblNumero = new JLabel(numero + ". ");
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNumero.setForeground(new Color(33, 97, 140));

        JLabel lblPregunta = new JLabel(texto);
        lblPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblPuntaje = new JLabel("(" + (int) puntajeMaximo + " pts)");
        lblPuntaje.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPuntaje.setForeground(Color.GRAY);

        JPanel panelTexto = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelTexto.setBackground(Color.WHITE);
        panelTexto.add(lblNumero);
        panelTexto.add(lblPregunta);
        panelTexto.add(lblPuntaje);

        JComponent campoRespuesta;
        if ("NUMERICO".equals(tipo) || "ESCALA".equals(tipo)) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, (int) puntajeMaximo, 1));
            spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            spinner.setPreferredSize(new Dimension(80, 30));
            campoRespuesta = spinner;
        } else {
            JTextField textField = new JTextField();
            textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180)),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            campoRespuesta = textField;
        }

        panel.add(panelTexto, BorderLayout.NORTH);
        panel.add(campoRespuesta, BorderLayout.CENTER);
        camposRespuesta.add(campoRespuesta);

        return panel;
    }

    private void enviarRespuestas() {
        if (listaPreguntas == null || listaPreguntas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay preguntas cargadas.");
            return;
        }

        // Validar que todas las preguntas tengan respuesta
        for (int i = 0; i < camposRespuesta.size(); i++) {
            JComponent comp = camposRespuesta.get(i);
            String respuesta = "";
            if (comp instanceof JTextField) {
                respuesta = ((JTextField) comp).getText().trim();
                if (respuesta.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor responda la pregunta " + (i + 1));
                    return;
                }
            } else if (comp instanceof JSpinner) {
                respuesta = String.valueOf(((JSpinner) comp).getValue());
            }
        }

        int confirm = JOptionPane.showConfirmDialog(this,
              "¿Está seguro de enviar sus respuestas?\nUna vez enviadas no podrá modificarlas.",
              "Confirmar envío", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Aquí se procesarían las respuestas y se guardarían en la base de datos
        // (Se puede implementar guardando las respuestas en EVALUACION o RESPUESTA según requerimiento)
        JOptionPane.showMessageDialog(this,
              " Respuestas enviadas correctamente.\n¡Gracias por completar el cuestionario!",
              "Enviado", JOptionPane.INFORMATION_MESSAGE);

        limpiarCuestionario();
        btnEnviar.setEnabled(false);
    }

    private void limpiarCuestionario() {
        panelPreguntas.removeAll();
        panelPreguntas.revalidate();
        panelPreguntas.repaint();
        camposRespuesta.clear();
        listaPreguntas = null;
        lblTituloPractica.setText("Seleccione una práctica y presione 'Cargar'");
        btnEnviar.setEnabled(false);
    }
}