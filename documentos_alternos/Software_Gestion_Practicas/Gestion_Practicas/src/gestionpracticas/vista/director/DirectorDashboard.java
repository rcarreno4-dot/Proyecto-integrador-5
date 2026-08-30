package com.gestionpracticas.vista.director;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.Utilidades;
import com.gestionpracticas.vista.LoginForm;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DirectorDashboard extends JFrame {

    private final Usuario usuario;
    private JButton btnUsuarios, btnPracticas, btnCursos, btnProgramas, btnReportes, btnIncidencias, btnCerrarSesion;

    public DirectorDashboard(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(41, 128, 185));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(220, 45));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    private JPanel crearCard(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(80, 80, 80));
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblValor.setForeground(color);
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        return panel;
    }

    private String contar(String tabla) {
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            ps = ConexionBD.getConnection().prepareStatement("SELECT COUNT(*) FROM " + tabla);
            rs = ps.executeQuery();
            return rs.next() ? String.valueOf(rs.getInt(1)) : "0";
        } catch (Exception e) {
            return "0";
        } finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }

    private void cargarPracticasDashboard(DefaultTableModel modelo) {
        PreparedStatement ps = null; ResultSet rs = null;
        modelo.setRowCount(0);
        try {
            String sql = "SELECT ID_PRACTICA, TITULO, TIPO_PRACTICA, NVL(ESTADO,'ACTIVO') ESTADO FROM PRACTICA ORDER BY ID_PRACTICA";
            ps = ConexionBD.getConnection().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)});
            }
        } catch (Exception e) {
            modelo.addRow(new Object[]{"-", "Sin datos", "Revise conexión", "-"});
        } finally { Utilidades.cerrar(rs); Utilidades.cerrar(ps); }
    }

    private void initComponents() {
        setTitle("Sistema Universitario - Director");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(245, 247, 250));

        JPanel menu = new JPanel(new GridLayout(10, 1, 10, 10));
        menu.setBackground(new Color(27, 38, 59));
        menu.setPreferredSize(new Dimension(260, 0));
        menu.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel tituloMenu = new JLabel("PROYECTOJD");
        tituloMenu.setForeground(Color.WHITE);
        tituloMenu.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton btnDashboard = crearBoton("Dashboard");
        btnUsuarios = crearBoton("Usuarios");
        btnPracticas = crearBoton("Prácticas");
        btnCursos = crearBoton("Cursos");
        btnProgramas = crearBoton("Programas");
        btnReportes = crearBoton("Reportes");
        btnIncidencias = crearBoton("Incidencias");
        btnCerrarSesion = crearBoton("Cerrar Sesión");

        btnUsuarios.addActionListener(e -> new UsuarioForm(usuario).setVisible(true));
        btnPracticas.addActionListener(e -> new PracticaForm(usuario).setVisible(true));
        btnCursos.addActionListener(e -> new CursoForm(usuario).setVisible(true));
        btnProgramas.addActionListener(e -> new ProgramaForm(usuario).setVisible(true));
        btnReportes.addActionListener(e -> new com.gestionpracticas.vista.simple.ReportesHistoricoSimpleForm(usuario).setVisible(true));
        btnIncidencias.addActionListener(e -> new com.gestionpracticas.vista.simple.ReporteIncidenciaSimpleForm(usuario).setVisible(true));
        btnCerrarSesion.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });

        menu.add(tituloMenu);
        menu.add(btnDashboard);
        menu.add(btnUsuarios);
        menu.add(btnPracticas);
        menu.add(btnCursos);
        menu.add(btnProgramas);
        menu.add(btnReportes);
        menu.add(btnIncidencias);
        menu.add(btnCerrarSesion);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel bienvenida = new JLabel("Bienvenido, " + usuario.getNombre());
        bienvenida.setForeground(Color.WHITE);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JLabel subtitulo = new JLabel("Panel administrativo");
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(bienvenida);
        textos.add(subtitulo);
        header.add(textos, BorderLayout.WEST);

        JPanel estadisticas = new JPanel(new GridLayout(1, 4, 20, 20));
        estadisticas.setOpaque(false);
        estadisticas.setBorder(new EmptyBorder(30, 30, 20, 30));
        estadisticas.add(crearCard("Usuarios", contar("USUARIO"), new Color(52, 152, 219)));
        estadisticas.add(crearCard("Prácticas", contar("PRACTICA"), new Color(39, 174, 96)));
        estadisticas.add(crearCard("Cursos", contar("CURSO"), new Color(243, 156, 18)));
        estadisticas.add(crearCard("Reportes", contar("REPORTE"), new Color(231, 76, 60)));

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Práctica", "Tipo", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setBackground(new Color(41, 128, 185));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        cargarPracticasDashboard(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Prácticas registradas"));
        scroll.setPreferredSize(new Dimension(0, 250));

        contenido.add(header, BorderLayout.NORTH);
        contenido.add(estadisticas, BorderLayout.CENTER);
        contenido.add(scroll, BorderLayout.SOUTH);

        principal.add(menu, BorderLayout.WEST);
        principal.add(contenido, BorderLayout.CENTER);
        setContentPane(principal);
    }
}
