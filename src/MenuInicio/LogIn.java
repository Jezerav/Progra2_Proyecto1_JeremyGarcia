/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuInicio;

import javax.swing.*;
import java.awt.*;
import LogicaJuego.LogicaGUI;
import LogicaJuego.Player;

public class LogIn extends JFrame {

    private LogicaGUI logica;
    private MenuInicio menuInicio;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnIngresar, btnVolver;

    public LogIn(LogicaGUI logica, MenuInicio menuInicio) {
        this.logica = logica;
        this.menuInicio = menuInicio;

        setTitle("LOG IN");
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
        JLabel lblTitulo = new JLabel("LOG IN", SwingConstants.CENTER);
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

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 16));
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

        btnIngresar = new JButton("Ingresar");
        btnVolver = new JButton("Volver");

        JButton[] botones = {btnIngresar, btnVolver};
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

        btnIngresar.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            Player jugador = logica.buscarJugador(username, password);
            if (jugador != null) {
                dispose();
                new MenuPrincipal.MenuPrincipal(jugador, logica).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o password incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
