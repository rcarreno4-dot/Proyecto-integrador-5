package com.gestionpracticas.vista;

import com.gestionpracticas.logica.UsuarioLogica;
import com.gestionpracticas.modelo.Usuario;

import com.gestionpracticas.vista.director.DirectorDashboard;
import com.gestionpracticas.vista.coordinador.CoordinadorDashboard;
import com.gestionpracticas.vista.docente.DocenteDashboard;
import com.gestionpracticas.vista.estudiante.EstudianteDashboard;
import com.gestionpracticas.vista.institucion.InstitucionDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    private JButton btnLogin;

    private JLabel lblError;

    private JCheckBox chkMostrar;

    public LoginForm() {

        initComponents();
    }

    private void initComponents() {

        setTitle(
          "PROYECTOJD - Gestión de Prácticas"
        );

        setSize(450, 500);

        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);

        setResizable(false);

        Container c =
        getContentPane();

        c.setLayout(null);

        c.setBackground(
            new Color(33,97,140)
        );

        // =========================
        // TITULO
        // =========================

        JLabel lblTitulo =
        new JLabel(
          "GESTIÓN DE PRÁCTICAS"
        );

        lblTitulo.setBounds(
            70,
            40,
            320,
            40
        );

        lblTitulo.setFont(
            new Font(
              "Arial",
                Font.BOLD,
                26
            )
        );

        lblTitulo.setForeground(
            Color.WHITE
        );

        c.add(lblTitulo);

        // =========================
        // SUBTITULO
        // =========================

        JLabel lblSub =
        new JLabel(
          "Sistema Académico PROYECTOJD"
        );

        lblSub.setBounds(
            105,
            85,
            250,
            20
        );

        lblSub.setForeground(
            Color.WHITE
        );

        c.add(lblSub);

        // =========================
        // EMAIL
        // =========================

        JLabel lblEmail =
        new JLabel(
          "Correo Electrónico"
        );

        lblEmail.setBounds(
            70,
            150,
            150,
            20
        );

        lblEmail.setForeground(
            Color.WHITE
        );

        c.add(lblEmail);

        txtEmail =
        new JTextField();

        txtEmail.setBounds(
            70,
            175,
            300,
            35
        );

        txtEmail.setFont(
            new Font(
              "Arial",
                Font.PLAIN,
                14
            )
        );

        c.add(txtEmail);

        // =========================
        // PASSWORD
        // =========================

        JLabel lblPass =
        new JLabel(
          "Contraseña"
        );

        lblPass.setBounds(
            70,
            230,
            100,
            20
        );

        lblPass.setForeground(
            Color.WHITE
        );

        c.add(lblPass);

        txtPassword =
        new JPasswordField();

        txtPassword.setBounds(
            70,
            255,
            300,
            35
        );

        txtPassword.setFont(
            new Font(
              "Arial",
                Font.PLAIN,
                14
            )
        );

        c.add(txtPassword);

        // =========================
        // CHECKBOX
        // =========================

        chkMostrar =
        new JCheckBox(
          "Mostrar contraseña"
        );

        chkMostrar.setBounds(
            70,
            300,
            180,
            25
        );

        chkMostrar.setBackground(
            new Color(33,97,140)
        );

        chkMostrar.setForeground(
            Color.WHITE
        );

        chkMostrar.addActionListener(
        new ActionListener() {

            public void actionPerformed(
                ActionEvent e
            ) {

                if(chkMostrar.isSelected()) {

                    txtPassword.setEchoChar(
                        (char)0
                    );

                } else {

                    txtPassword.setEchoChar('*');
                }
            }
        });

        c.add(chkMostrar);

        // =========================
        // ERROR
        // =========================

        lblError =
        new JLabel("");

        lblError.setBounds(
            70,
            330,
            300,
            20
        );

        lblError.setForeground(
            Color.YELLOW
        );

        c.add(lblError);

        // =========================
        // LOGIN
        // =========================

        btnLogin =
        new JButton(
          "INGRESAR"
        );

        btnLogin.setBounds(
            120,
            370,
            180,
            40
        );

        btnLogin.setBackground(
            new Color(39,174,96)
        );

        btnLogin.setForeground(
            Color.WHITE
        );

        btnLogin.setFocusPainted(false);

        btnLogin.addActionListener(
        new ActionListener() {

            public void actionPerformed(
                ActionEvent e
            ) {

                login();
            }
        });

        c.add(btnLogin);

        // =========================
        // RECUPERAR
        // =========================

        JButton btnRecuperar =
        new JButton(
          "Restablecer contraseña"
        );

        btnRecuperar.setBounds(
            110,
            420,
            200,
            25
        );

        btnRecuperar.addActionListener(
        new ActionListener() {

            public void actionPerformed(
                ActionEvent e
            ) {

                mostrarRestablecerContrasena();
            }
        });

        c.add(btnRecuperar);
    }

    // ==================================
    // LOGIN
    // ==================================

    private void login() {

        String email =
        txtEmail.getText().trim();

        String pass =
        new String(
            txtPassword.getPassword()
        ).trim();

        if(email.equals("")) {

            lblError.setText(
              "Ingrese el correo"
            );

            return;
        }

        if(pass.equals("")) {

            lblError.setText(
              "Ingrese la contraseña"
            );

            return;
        }

        try {

            Usuario usuario =
            new UsuarioLogica()
            .iniciarSesion(
                email,
                pass
            );

            if(usuario != null) {

                dispose();

                abrirDashboard(usuario);

            } else {

                lblError.setText(
                  "Credenciales incorrectas"
                );
            }

        } catch(Exception e) {

            lblError.setText(
              "Error: " +
                e.getMessage()
            );
        }
    }


    // ==================================
    // RESTABLECER CONTRASEÑA
    // ==================================

    private void mostrarRestablecerContrasena() {

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));

        JTextField txtCorreoRecuperar = new JTextField(txtEmail.getText().trim());
        JPasswordField txtNueva = new JPasswordField();
        JPasswordField txtConfirmar = new JPasswordField();

        panel.add(new JLabel("Correo registrado:"));
        panel.add(txtCorreoRecuperar);
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(txtNueva);
        panel.add(new JLabel("Confirmar contraseña:"));
        panel.add(txtConfirmar);

        int opcion = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Restablecer contraseña",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if(opcion != JOptionPane.OK_OPTION) {
            return;
        }

        String correo = txtCorreoRecuperar.getText().trim();
        String nueva = new String(txtNueva.getPassword()).trim();
        String confirmar = new String(txtConfirmar.getPassword()).trim();

        try {
            boolean ok = new UsuarioLogica().restablecerContrasena(
                correo,
                nueva,
                confirmar
            );

            if(ok) {
                JOptionPane.showMessageDialog(
                    this,
                    "Contraseña restablecida correctamente. Ya puedes iniciar sesión con la nueva clave."
                );
                txtEmail.setText(correo);
                txtPassword.setText("");
                txtPassword.requestFocus();
                lblError.setText("");
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No se pudo restablecer la contraseña. Verifique el correo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "No se pudo restablecer",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ==================================
    // DASHBOARDS
    // ==================================

    private void abrirDashboard(
        Usuario usuario
    ) {

        String tipo =
        usuario.getTipoUsuario();

        if(tipo.equalsIgnoreCase(
          "DIRECTOR"
        )) {

            new DirectorDashboard(
                usuario
            ).setVisible(true);

        } else if(tipo.equalsIgnoreCase(
          "COORDINADOR"
        )) {

            new CoordinadorDashboard(
                usuario
            ).setVisible(true);

        } else if(tipo.equalsIgnoreCase(
          "DOCENTE"
        )) {

            new DocenteDashboard(
                usuario
            ).setVisible(true);

        } else if(tipo.equalsIgnoreCase(
          "ESTUDIANTE"
        )) {

            new EstudianteDashboard(
                usuario
            ).setVisible(true);

        } else if(tipo.equalsIgnoreCase(
          "INSTITUCION"
        )) {

            new InstitucionDashboard(
                usuario
            ).setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                null,
              "Tipo de usuario inválido"
            );
        }
    }

    public static void main(
        String args[]
    ) {

        new LoginForm()
        .setVisible(true);
    }
}