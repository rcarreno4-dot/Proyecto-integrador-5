package com.gestionpracticas.vista.coordinador;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.vista.LoginForm;
import com.gestionpracticas.vista.comun.CrudTablaFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CoordinadorDashboard extends JFrame {

    private final Usuario usuario;

    public CoordinadorDashboard(Usuario usuario) {
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

    private JPanel card(String titulo, String valor, String detalle, Color color) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(15,15,15,15)));
        JLabel t = new JLabel(titulo); t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel v = new JLabel(valor); v.setFont(new Font("Segoe UI", Font.BOLD, 32)); v.setForeground(color);
        JLabel d = new JLabel(detalle); d.setFont(new Font("Segoe UI", Font.PLAIN, 12)); d.setForeground(new Color(90,90,90));
        p.add(t, BorderLayout.NORTH); p.add(v, BorderLayout.CENTER); p.add(d, BorderLayout.SOUTH);
        return p;
    }

    private void init() {
        setTitle("Dashboard Coordinador de Práctica");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));

        JPanel menu = new JPanel(new GridLayout(11,1,8,8));
        menu.setPreferredSize(new Dimension(265,0));
        menu.setBackground(new Color(27, 38, 59));
        menu.setBorder(new EmptyBorder(25,20,25,20));
        JLabel logo = new JLabel("PROYECTOJD"); logo.setForeground(Color.WHITE); logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JButton btnGrupos = boton("Grupos de Práctica");
        JButton btnAsignacion = boton("Asignación Docente");
        JButton btnMatricula = boton("Vincular Estudiantes");
        JButton btnInstitucion = boton("Instituciones Receptoras");
        JButton btnHoras = boton("Control de Horas");
        JButton btnIncidencias = boton("Reportes / Incidencias");
        JButton btnSalir = boton("Cerrar Sesión");

        btnGrupos.addActionListener(e -> new com.gestionpracticas.vista.simple.SeguimientoIntegralSimpleForm(usuario).setVisible(true));
        btnAsignacion.addActionListener(e -> new com.gestionpracticas.vista.simple.SeguimientoIntegralSimpleForm(usuario).setVisible(true));
        btnMatricula.addActionListener(e -> new com.gestionpracticas.vista.simple.MatriculaEstudianteSimpleForm(usuario).setVisible(true));
        btnInstitucion.addActionListener(e -> new CrudTablaFrame("Instituciones Receptoras", "INSTITUCION", "ID_INSTITUCION", new String[]{"ID_INSTITUCION","NOMBRE","NIT","DIRECCION","TELEFONO","EMAIL","CORREO","CIUDAD","MUNICIPIO","REPRESENTANTE","RECTOR","ESTADO"}).setVisible(true));
        btnHoras.addActionListener(e -> new com.gestionpracticas.vista.simple.HorasSeguimientoSimpleForm(usuario).setVisible(true));
        btnIncidencias.addActionListener(e -> new com.gestionpracticas.vista.simple.ReporteIncidenciaSimpleForm(usuario).setVisible(true));
        btnSalir.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });

        menu.add(logo); menu.add(btnGrupos); menu.add(btnAsignacion); menu.add(btnMatricula); menu.add(btnInstitucion); menu.add(btnHoras); menu.add(btnIncidencias); menu.add(new JLabel("")); menu.add(new JLabel("")); menu.add(new JLabel("")); menu.add(btnSalir);

        JPanel contenido = new JPanel(new BorderLayout(20,20)); contenido.setOpaque(false);
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(new Color(41,128,185)); header.setBorder(new EmptyBorder(20,25,20,25));
        JLabel titulo = new JLabel("Bienvenido Coordinador"); titulo.setFont(new Font("Segoe UI", Font.BOLD, 26)); titulo.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Operación de prácticas: grupos, instituciones, docentes, estudiantes y horas reglamentarias"); sub.setForeground(Color.WHITE);
        JPanel txt = new JPanel(new GridLayout(2,1)); txt.setOpaque(false); txt.add(titulo); txt.add(sub); header.add(txt, BorderLayout.WEST);

        JPanel cards = new JPanel(new GridLayout(2,3,20,20)); cards.setOpaque(false); cards.setBorder(new EmptyBorder(30,30,30,30));
        cards.add(card("Grupos", String.valueOf(DBHelper.contar("GRUPO")), "Grupos creados", new Color(52,152,219)));
        cards.add(card("Matrículas", String.valueOf(DBHelper.contar("MATRICULA_PRACTICA")), "Estudiantes vinculados", new Color(39,174,96)));
        cards.add(card("Instituciones", String.valueOf(DBHelper.contar("INSTITUCION")), "Sedes receptoras", new Color(155,89,182)));
        cards.add(card("Asignaciones", String.valueOf(DBHelper.contar("ASIGNACION_DOCENTE")), "Docentes asignados", new Color(243,156,18)));
        cards.add(card("Horas", String.valueOf(DBHelper.contar("HORAS_PRACTICA")), "Registros de horas", new Color(22,160,133)));
        cards.add(card("Prácticas", String.valueOf(DBHelper.contar("PRACTICA")), "Prácticas activas", new Color(231,76,60)));

        JTextArea ayuda = new JTextArea("Flujo recomendado: 1) Crear o validar institución receptora. 2) Crear grupo. 3) Asignar docente. 4) Vincular estudiante a la práctica. 5) Controlar horas y confirmar avances. El sistema trabaja con los datos reales registrados en Oracle; si falta un dato, créalo desde el módulo correspondiente.");
        ayuda.setLineWrap(true); ayuda.setWrapStyleWord(true); ayuda.setEditable(false); ayuda.setFont(new Font("Segoe UI", Font.PLAIN, 13)); ayuda.setBorder(new EmptyBorder(10,30,15,30)); ayuda.setBackground(new Color(245,247,250));
        contenido.add(header, BorderLayout.NORTH); contenido.add(cards, BorderLayout.CENTER); contenido.add(ayuda, BorderLayout.SOUTH);
        root.add(menu, BorderLayout.WEST); root.add(contenido, BorderLayout.CENTER); setContentPane(root);
    }
}
