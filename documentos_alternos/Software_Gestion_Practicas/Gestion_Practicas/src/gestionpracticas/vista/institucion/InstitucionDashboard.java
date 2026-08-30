package com.gestionpracticas.vista.institucion;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.vista.LoginForm;
import com.gestionpracticas.vista.comun.CrudTablaFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InstitucionDashboard extends JFrame {

    private final Usuario usuario;

    public InstitucionDashboard(Usuario usuario) {
        this.usuario = usuario;
        init();
    }

    private JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(41,128,185));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(230,42));
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
        setTitle("Dashboard Institución Receptora");
        setSize(1280,720);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout()); root.setBackground(new Color(245,247,250));
        JPanel menu = new JPanel(new GridLayout(10,1,8,8)); menu.setPreferredSize(new Dimension(265,0)); menu.setBackground(new Color(27,38,59)); menu.setBorder(new EmptyBorder(25,20,25,20));
        JLabel logo = new JLabel("PROYECTOJD"); logo.setForeground(Color.WHITE); logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JButton btnHoras = boton("Confirmar Horas");
        JButton btnEstudiantes = boton("Estudiantes Asignados");
        JButton btnPracticas = boton("Prácticas en la Sede");
        JButton btnConvenio = boton("Datos Institución");
        JButton btnIncidencias = boton("Reportes / Incidencias");
        JButton btnSalir = boton("Cerrar Sesión");
        btnHoras.addActionListener(e -> new com.gestionpracticas.vista.simple.HorasSeguimientoSimpleForm(usuario).setVisible(true));
        btnEstudiantes.addActionListener(e -> new com.gestionpracticas.vista.simple.SeguimientoIntegralSimpleForm(usuario).setVisible(true));
        btnPracticas.addActionListener(e -> new com.gestionpracticas.vista.simple.SeguimientoIntegralSimpleForm(usuario).setVisible(true));
        btnIncidencias.addActionListener(e -> new com.gestionpracticas.vista.simple.ReporteIncidenciaSimpleForm(usuario).setVisible(true));
        btnConvenio.addActionListener(e -> new CrudTablaFrame("Institución Receptora", "INSTITUCION", "ID_INSTITUCION", new String[]{"ID_INSTITUCION","NOMBRE","NIT","DIRECCION","TELEFONO","EMAIL","CORREO","CIUDAD","MUNICIPIO","REPRESENTANTE","RECTOR","SITIO_WEB","LOGO","ESTADO"}).setVisible(true));
        btnSalir.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });
        menu.add(logo); menu.add(btnHoras); menu.add(btnEstudiantes); menu.add(btnPracticas); menu.add(btnConvenio); menu.add(btnIncidencias); menu.add(new JLabel("")); menu.add(new JLabel("")); menu.add(new JLabel("")); menu.add(btnSalir);

        JPanel contenido = new JPanel(new BorderLayout(20,20)); contenido.setOpaque(false);
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(new Color(41,128,185)); header.setBorder(new EmptyBorder(20,25,20,25));
        JLabel titulo = new JLabel("Bienvenida Institución Receptora: " + usuario.getNombre()); titulo.setFont(new Font("Segoe UI", Font.BOLD, 26)); titulo.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Panel para confirmar horas, consultar estudiantes asignados y revisar prácticas activas"); sub.setForeground(Color.WHITE);
        JPanel tx = new JPanel(new GridLayout(2,1)); tx.setOpaque(false); tx.add(titulo); tx.add(sub); header.add(tx, BorderLayout.WEST);
        JPanel cards = new JPanel(new GridLayout(2,2,20,20)); cards.setOpaque(false); cards.setBorder(new EmptyBorder(30,30,30,30));
        cards.add(card("Estudiantes asignados", DBHelper.contar("MATRICULA_PRACTICA") + " matrícula(s) registradas en el sistema."));
        cards.add(card("Horas", DBHelper.contar("HORAS_PRACTICA") + " registro(s) de horas para confirmación."));
        cards.add(card("Prácticas", DBHelper.contar("PRACTICA") + " práctica(s) asociadas a procesos formativos."));
        cards.add(card("Función de la institución", "La institución confirma horas ejecutadas y permite validar que el estudiante realmente desarrolló actividades en la sede receptora."));
        contenido.add(header, BorderLayout.NORTH); contenido.add(cards, BorderLayout.CENTER);
        root.add(menu, BorderLayout.WEST); root.add(contenido, BorderLayout.CENTER); setContentPane(root);
    }
}
