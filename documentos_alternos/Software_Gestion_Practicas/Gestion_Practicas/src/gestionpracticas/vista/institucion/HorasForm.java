package com.gestionpracticas.vista.institucion;

import com.gestionpracticas.logica.RegistroActividadLogica;
import com.gestionpracticas.modelo.RegistroActividad;
import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HorasForm extends JFrame {

    private final RegistroActividadLogica logica;
    private final Usuario usuarioActual;

    // Componentes principales
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnAprobar, btnRechazar, btnRefrescar, btnConfigPantalla;
    private JLabel lblTotalHoras, lblPendientes, lblRechazadas;
    private JPanel mainPanel;
    private JSplitPane split;

    public HorasForm(Usuario usuario) {
        this.usuarioActual = usuario;
        this.logica = new RegistroActividadLogica();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Validación de Horas - " + usuarioActual.getNombre());
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

        // Panel central con estadísticas y tabla
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(240, 242, 245));

        // Panel de estadísticas
        JPanel statsPanel = crearPanelEstadisticas();
        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Tabla
        JPanel tablaPanel = crearPanelTabla();
        centerPanel.add(tablaPanel, BorderLayout.CENTER);

        // Panel de botones de acción
        JPanel botonesPanel = crearPanelBotones();
        centerPanel.add(botonesPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

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

        JLabel lblTitulo = new JLabel("⏱ VALIDACIÓN DE HORAS DE PRÁCTICA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel(" Institución: " + usuarioActual.getNombre());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(new Color(220, 220, 220));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblUsuario, BorderLayout.EAST);

        return header;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 10));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        lblTotalHoras = new JLabel(" Total aprobadas: 0");
        lblTotalHoras.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalHoras.setForeground(new Color(39, 174, 96));
        lblTotalHoras.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalHoras.setBorder(crearBordeTarjeta());

        lblPendientes = new JLabel("⏳ Pendientes: 0");
        lblPendientes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPendientes.setForeground(new Color(241, 196, 15));
        lblPendientes.setHorizontalAlignment(SwingConstants.CENTER);
        lblPendientes.setBorder(crearBordeTarjeta());

        lblRechazadas = new JLabel(" Rechazadas: 0");
        lblRechazadas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRechazadas.setForeground(new Color(192, 57, 43));
        lblRechazadas.setHorizontalAlignment(SwingConstants.CENTER);
        lblRechazadas.setBorder(crearBordeTarjeta());

        panel.add(lblTotalHoras);
        panel.add(lblPendientes);
        panel.add(lblRechazadas);

        return panel;
    }

    private Border crearBordeTarjeta() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Columnas de la tabla
        String[] cols = {"ID", "Estudiante", "Fecha", "Descripción", "Horas", "Tipo", "Estado", "Observaciones"};
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
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(4).setMaxWidth(60);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(150);

        // Renderer para colorear filas según estado
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String estado = (String) modeloTabla.getValueAt(row, 6);
                    if (estado != null) {
                        switch (estado) {
                            case "APROBADO":
                                c.setBackground(new Color(212, 237, 218));
                                break;
                            case "RECHAZADO":
                                c.setBackground(new Color(248, 215, 218));
                                break;
                            default:
                                c.setBackground(new Color(255, 243, 205));
                                break;
                        }
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Actividades registradas por estudiantes"));

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(240, 242, 245));

        btnAprobar = new JButton(" Aprobar Horas");
        btnAprobar.setBackground(new Color(39, 174, 96));
        btnAprobar.setForeground(Color.WHITE);
        btnAprobar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAprobar.setFocusPainted(false);
        btnAprobar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAprobar.setPreferredSize(new Dimension(140, 38));
        btnAprobar.setEnabled(false);
        btnAprobar.addActionListener(e -> aprobar());

        btnRechazar = new JButton(" Rechazar");
        btnRechazar.setBackground(new Color(192, 57, 43));
        btnRechazar.setForeground(Color.WHITE);
        btnRechazar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRechazar.setFocusPainted(false);
        btnRechazar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRechazar.setPreferredSize(new Dimension(140, 38));
        btnRechazar.setEnabled(false);
        btnRechazar.addActionListener(e -> rechazar());

        btnRefrescar = new JButton(" Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setPreferredSize(new Dimension(120, 38));
        btnRefrescar.addActionListener(e -> cargarTabla());

        panel.add(btnAprobar);
        panel.add(btnRechazar);
        panel.add(btnRefrescar);

        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(240, 242, 245));
        footer.setPreferredSize(new Dimension(1200, 40));
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblInfo = new JLabel("💡 Seleccione una actividad pendiente para aprobar o rechazar");
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

            JOptionPane.showMessageDialog(this, "Pantalla configurada a " + resoluciones[idx]);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        double totalHorasAprobadas = 0;
        int pendientes = 0;
        int rechazadas = 0;

        try {
            List<RegistroActividad> actividades = logica.listarTodas();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            if (actividades != null) {
                for (RegistroActividad r : actividades) {
                    String estudiante = "";
                    if (r.getMatricula() != null && r.getMatricula().getEstudiante() != null) {
                        estudiante = r.getMatricula().getEstudiante().getNombre() + " " +
                                r.getMatricula().getEstudiante().getApellido();
                    }

                    String fecha = r.getFechaActividad() != null ? sdf.format(r.getFechaActividad()) : "";
                    String estado = r.getEstado() != null ? r.getEstado() : "PENDIENTE";

                    modeloTabla.addRow(new Object[]{
                            r.getIdRegistro(),
                            estudiante,
                            fecha,
                            r.getDescripcion(),
                            r.getHoras(),
                            r.getTipoActividad(),
                            estado,
                            r.getObservaciones() != null ? r.getObservaciones() : ""
                    });

                    if ("APROBADO".equals(estado)) {
                        totalHorasAprobadas += r.getHoras();
                    } else if ("PENDIENTE".equals(estado)) {
                        pendientes++;
                    } else if ("RECHAZADO".equals(estado)) {
                        rechazadas++;
                    }
                }
            }

            lblTotalHoras.setText(" Total aprobadas: " + totalHorasAprobadas);
            lblPendientes.setText("⏳ Pendientes: " + pendientes);
            lblRechazadas.setText(" Rechazadas: " + rechazadas);

            if (actividades == null || actividades.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                      "No hay actividades registradas para validar.",
                      "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                  "Error al cargar actividades: " + e.getMessage(),
                  "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void aprobar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad para aprobar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String estudiante = (String) modeloTabla.getValueAt(fila, 1);
        double horas = (double) modeloTabla.getValueAt(fila, 4);

        int confirm = JOptionPane.showConfirmDialog(this,
              "¿Aprobar " + horas + " horas para el estudiante " + estudiante + "?",
              "Confirmar aprobación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (logica.aprobar(id, "Aprobado por la institución")) {
                    JOptionPane.showMessageDialog(this, " Horas aprobadas correctamente.");
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al aprobar las horas.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void rechazar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad para rechazar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String estudiante = (String) modeloTabla.getValueAt(fila, 1);
        double horas = (double) modeloTabla.getValueAt(fila, 4);

        String motivo = JOptionPane.showInputDialog(this,
              "Ingrese el motivo del rechazo para " + estudiante + ":",
              "Motivo de rechazo", JOptionPane.QUESTION_MESSAGE);

        if (motivo == null || motivo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un motivo para rechazar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
              "¿Rechazar " + horas + " horas para el estudiante " + estudiante + "?\nMotivo: " + motivo,
              "Confirmar rechazo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (logica.rechazar(id, motivo)) {
                    JOptionPane.showMessageDialog(this, " Actividad rechazada.\nMotivo: " + motivo);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al rechazar la actividad.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}