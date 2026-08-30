package com.gestionpracticas.vista.docente;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.vista.LoginForm;
import com.gestionpracticas.vista.comun.CrudTablaFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DocenteDashboard extends JFrame {

    private final Usuario usuario;

    public DocenteDashboard(Usuario usuario) {
        this.usuario = usuario;
        init();
    }

    private JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(41, 128, 185));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(230, 42));
        return b;
    }

    private JPanel card(String titulo, String texto) {
        JPanel p = new JPanel(new BorderLayout(8,8));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(18,18,18,18)));
        JLabel t = new JLabel(titulo); t.setFont(new Font("Segoe UI", Font.BOLD, 18)); t.setForeground(new Color(33,97,140));
        JTextArea a = new JTextArea(texto); a.setEditable(false); a.setLineWrap(true); a.setWrapStyleWord(true); a.setOpaque(false); a.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        p.add(t, BorderLayout.NORTH); p.add(a, BorderLayout.CENTER); return p;
    }

    private void init() {
        setTitle("Dashboard Docente Asesor");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout()); root.setBackground(new Color(245,247,250));
        JPanel menu = new JPanel(new GridLayout(11,1,8,8)); menu.setPreferredSize(new Dimension(265,0)); menu.setBackground(new Color(27,38,59)); menu.setBorder(new EmptyBorder(25,20,25,20));
        JLabel logo = new JLabel("PROYECTOJD"); logo.setForeground(Color.WHITE); logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JButton btnRubrica = boton("Rúbricas");
        JButton btnPreguntas = boton("Banco de Preguntas");
        JButton btnEvaluacion = boton("Evaluar Estudiante");
        JButton btnActividades = boton("Revisar Actividades");
        JButton btnRespuestas = boton("Respuestas / Feedback");
        JButton btnIncidencias = boton("Reportes / Incidencias");
        JButton btnSalir = boton("Cerrar Sesión");

        btnRubrica.addActionListener(e -> new RubricaForm(usuario).setVisible(true));
        btnPreguntas.addActionListener(e -> new com.gestionpracticas.vista.simple.EvaluacionDocenteSimpleForm(usuario).setVisible(true));
        btnEvaluacion.addActionListener(e -> new com.gestionpracticas.vista.simple.EvaluacionDocenteSimpleForm(usuario).setVisible(true));
        btnActividades.addActionListener(e -> new com.gestionpracticas.vista.simple.HorasSeguimientoSimpleForm(usuario).setVisible(true));
        btnRespuestas.addActionListener(e -> new com.gestionpracticas.vista.simple.EvaluacionDocenteSimpleForm(usuario).setVisible(true));
        btnIncidencias.addActionListener(e -> new com.gestionpracticas.vista.simple.ReporteIncidenciaSimpleForm(usuario).setVisible(true));
        btnSalir.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });
        menu.add(logo); menu.add(btnRubrica); menu.add(btnPreguntas); menu.add(btnEvaluacion); menu.add(btnActividades); menu.add(btnRespuestas); menu.add(btnIncidencias); menu.add(new JLabel("")); menu.add(new JLabel("")); menu.add(new JLabel("")); menu.add(btnSalir);

        JPanel contenido = new JPanel(new BorderLayout(20,20)); contenido.setOpaque(false);
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(new Color(41,128,185)); header.setBorder(new EmptyBorder(20,25,20,25));
        JLabel titulo = new JLabel("Bienvenido Docente Asesor: " + usuario.getNombre()); titulo.setFont(new Font("Segoe UI", Font.BOLD, 26)); titulo.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Panel para acompañamiento pedagógico, rúbricas, evaluación, retroalimentación y aprobación de actividades"); sub.setForeground(Color.WHITE);
        JPanel tx = new JPanel(new GridLayout(2,1)); tx.setOpaque(false); tx.add(titulo); tx.add(sub); header.add(tx, BorderLayout.WEST);

        JPanel resumen = new JPanel(new GridLayout(1,4,20,20)); resumen.setOpaque(false); resumen.setBorder(new EmptyBorder(25,30,10,30));
        resumen.add(card("Evaluaciones", DBHelper.contar("EVALUACION") + " registros en el sistema"));
        resumen.add(card("Rúbricas", DBHelper.contar("RUBRICA") + " instrumentos configurados"));
        resumen.add(card("Preguntas", DBHelper.contar("PREGUNTA") + " preguntas disponibles"));
        resumen.add(card("Actividades", DBHelper.contar("REGISTRO_ACTIVIDAD") + " actividades por revisar"));

        JPanel cards = new JPanel(new GridLayout(2,2,20,20)); cards.setOpaque(false); cards.setBorder(new EmptyBorder(10,30,30,30));
        cards.add(card("Rol del docente", "El docente asesor revisa las actividades registradas por el estudiante, valida evidencias, registra observaciones pedagógicas y evalúa mediante rúbricas."));
        cards.add(card("Evaluación", "Use el módulo Evaluar Estudiante para registrar nota, observación y estado de la evaluación. Las respuestas pueden retroalimentarse en el módulo Respuestas."));
        cards.add(card("Seguimiento", "Revise actividades pendientes y cambie su estado a APROBADO o RECHAZADO según corresponda."));
        cards.add(card("Coherencia con práctica", "La evaluación se vincula a matrícula, práctica, grupo, estudiante y docente para que el Director pueda supervisar el proceso completo."));

        contenido.add(header, BorderLayout.NORTH); contenido.add(resumen, BorderLayout.CENTER); contenido.add(cards, BorderLayout.SOUTH);
        root.add(menu, BorderLayout.WEST); root.add(contenido, BorderLayout.CENTER); setContentPane(root);
    }
}
