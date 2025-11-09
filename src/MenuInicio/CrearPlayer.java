/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package MenuInicio;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import LogicaJuego.LogicaGUI;
import LogicaJuego.Player;

public class CrearPlayer extends JFrame {

    private LogicaGUI logica;
    private MenuInicio menuInicio;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnCrear, btnVolver;

    public CrearPlayer(LogicaGUI logica, MenuInicio menuInicio) {
        this.logica = logica;
        this.menuInicio = menuInicio;

        setTitle("CREAR PLAYER");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        Color vino = new Color(90, 0, 40);
        Color negro = new Color(0, 0, 0);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(negro);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        JLabel lblTitulo = new JLabel("CREAR PLAYER", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(vino);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel();
        panelCampos.setBackground(negro);
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 90, 15, 90));

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 16));
        txtUsername = new JTextField();

        JLabel lblPassword = new JLabel("Password (5 chars, 1 número, 1 especial):");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        txtPassword = new JPasswordField();

        panelCampos.add(lblUsername);
        panelCampos.add(txtUsername);
        panelCampos.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCampos.add(lblPassword);
        panelCampos.add(txtPassword);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(negro);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 120, 25, 120));

        btnCrear = new JButton("Crear");
        btnVolver = new JButton("Volver");

        JButton[] botones = {btnCrear, btnVolver};
        for (JButton btn : botones) {
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setForeground(Color.WHITE);
            btn.setBackground(vino);
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(150, 0, 50));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(vino);
                }
            });
            panelBotones.add(btn);
            panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(panelBotones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> dispose());

        btnCrear.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!logica.isUsernameUnico(username)) {
                JOptionPane.showMessageDialog(this, "Username ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!logica.validarPasswordGUI(password)) {
                JOptionPane.showMessageDialog(this, "Password inválido. Debe tener 5 caracteres, al menos 1 número y 1 especial", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Calendar fecha = logica.obtenerFechaActual();
            Player nuevo = new Player(username, password, 0, fecha, true);
            logica.agregarJugador(nuevo);
            JOptionPane.showMessageDialog(this, "Jugador creado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MenuPrincipal.MenuPrincipal(nuevo, logica).setVisible(true);
        });
    }
}
