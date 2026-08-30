package com.gestionpracticas;

import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.vista.LoginForm;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        System.out.println(
          "==========================================="
        );

        System.out.println(
          " SISTEMA DE GESTION DE PRACTICAS - PROYECTOJD"
        );

        System.out.println(
          "==========================================="
        );

        // VALIDAR CONEXION ORACLE

        if(
            ConexionBD.getConnection()
            != null
        ) {

            System.out.println(
              " Conexion establecida. Sistema listo."
            );

            SwingUtilities.invokeLater(
                new Runnable() {

                    public void run() {

                        new LoginForm()
                        .setVisible(true);
                    }
                }
            );

        } else {

            System.err.println(
              " No se pudo conectar a Oracle."
            );
        }
    }
}