package com.gestionpracticas.util;

import javax.swing.*;
import java.awt.*;

/**
 * Utilidad global para configurar la proporción de pantalla en todas las ventanas.
 * Opciones requeridas para el proyecto: 16:9, 16:10 y 4:3.
 */
public final class PantallaUtil {

    private PantallaUtil() {}

    public static void instalarMenuResolucion(final JFrame frame) {
        if (frame == null) return;

        JMenuBar barra = frame.getJMenuBar();
        if (barra == null) {
            barra = new JMenuBar();
            frame.setJMenuBar(barra);
        }

        // Evita duplicar el menú si la ventana llama dos veces este método.
        for (int i = 0; i < barra.getMenuCount(); i++) {
            JMenu menu = barra.getMenu(i);
            if (menu != null && "Pantalla".equals(menu.getText())) {
                return;
            }
        }

        JMenu menuPantalla = new JMenu("Pantalla");
        JMenuItem op169 = new JMenuItem("16:9 - 1280 x 720");
        JMenuItem op1610 = new JMenuItem("16:10 - 1280 x 800");
        JMenuItem op43 = new JMenuItem("4:3 - 1024 x 768");
        JMenuItem opMax = new JMenuItem("Maximizar");
        JMenuItem opCentro = new JMenuItem("Centrar ventana");

        op169.addActionListener(e -> aplicarResolucion(frame, 1280, 720));
        op1610.addActionListener(e -> aplicarResolucion(frame, 1280, 800));
        op43.addActionListener(e -> aplicarResolucion(frame, 1024, 768));
        opMax.addActionListener(e -> frame.setExtendedState(JFrame.MAXIMIZED_BOTH));
        opCentro.addActionListener(e -> frame.setLocationRelativeTo(null));

        menuPantalla.add(op169);
        menuPantalla.add(op1610);
        menuPantalla.add(op43);
        menuPantalla.addSeparator();
        menuPantalla.add(opMax);
        menuPantalla.add(opCentro);
        barra.add(menuPantalla);
    }

    public static void aplicarResolucion(JFrame frame, int ancho, int alto) {
        if (frame == null) return;
        frame.setExtendedState(JFrame.NORMAL);
        frame.setSize(ancho, alto);
        frame.setMinimumSize(new Dimension(Math.min(ancho, 900), Math.min(alto, 600)));
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }
}
