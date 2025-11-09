/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal;

import javax.swing.*;
import java.awt.*;
import LogicaJuego.Player;
import LogicaJuego.LogicaGUI;

public class CambiarPassword extends JFrame {

    private Player jugador;
    private JPasswordField txtActual, txtNuevo;
    private JButton btnCambiar, btnVolver;

    public CambiarPassword(Player jugador) {
        this.jugador = jugador;

        setTitle("CAMBIAR PASSWORD");
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
        JLabel lblTitulo = new JLabel("CAMBIAR PASSWORD", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(vino);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel();
        panelCampos.setBackground(negro);
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 90, 15, 90));

        JLabel lblActual = new JLabel("Password actual:");
        lblActual.setForeground(Color.WHITE);
        lblActual.setFont(new Font("Arial", Font.BOLD, 16));
        txtActual = new JPasswordField();

        JLabel lblNuevo = new JLabel("Nuevo password (5 chars, 1 número, 1 especial):");
        lblNuevo.setForeground(Color.WHITE);
        lblNuevo.setFont(new Font("Arial", Font.BOLD, 14));
        txtNuevo = new JPasswordField();

        panelCampos.add(lblActual);
        panelCampos.add(txtActual);
        panelCampos.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCampos.add(lblNuevo);
        panelCampos.add(txtNuevo);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(negro);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 120, 25, 120));

        btnCambiar = new JButton("Cambiar");
        btnVolver = new JButton("Volver");

        JButton[] botones = {btnCambiar, btnVolver};
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

        btnCambiar.addActionListener(e -> {
            String actual = new String(txtActual.getPassword());
            String nuevo = new String(txtNuevo.getPassword());

            if (!actual.equals(jugador.getPassword())) {
                JOptionPane.showMessageDialog(this, "Password actual incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!LogicaGUI.validarPasswordGUI(nuevo)) {
                JOptionPane.showMessageDialog(this, "Nuevo password inválido. Debe tener 5 caracteres, al menos 1 número y 1 especial.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            jugador.setPassword(nuevo);
            JOptionPane.showMessageDialog(this, "Password cambiado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }
}

