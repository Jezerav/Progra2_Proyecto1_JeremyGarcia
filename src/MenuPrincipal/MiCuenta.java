/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package MenuPrincipal;

import javax.swing.*;
import java.awt.*;
import LogicaJuego.Player;
import LogicaJuego.LogicaGUI;
import MenuInicio.MenuInicio;

public class MiCuenta extends JFrame {

    private Player jugador;
    private LogicaGUI logica;

    public MiCuenta(Player jugador, LogicaGUI logica) {
        this.jugador = jugador;
        this.logica = logica;

        setTitle("MI CUENTA");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        Color vino = new Color(90, 0, 40);
        Color negro = new Color(0, 0, 0);

        JLabel lblTitulo = new JLabel("MI CUENTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(vino);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(negro);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(negro);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel lblUsername = new JLabel("Username: " + jugador.getUsername());
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblPuntos = new JLabel("Puntos: " + jugador.getPuntos());
        lblPuntos.setForeground(Color.WHITE);
        lblPuntos.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblFecha = new JLabel("Fecha de Ingreso: " + jugador.getFechaString());
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblActivo = new JLabel("Activo: " + jugador.isActivo());
        lblActivo.setForeground(Color.WHITE);
        lblActivo.setFont(new Font("Arial", Font.BOLD, 18));

        panelContenido.add(lblUsername);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(lblPuntos);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(lblFecha);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(lblActivo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnCambiarPassword = new JButton("Cambiar Password");
        JButton btnEliminarCuenta = new JButton("Eliminar mi Cuenta");

        JButton[] botones = {btnCambiarPassword, btnEliminarCuenta};
        for (JButton btn : botones) {
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setForeground(Color.WHITE);
            btn.setBackground(vino);
            btn.setFocusPainted(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(150, 0, 50)); }
                public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(vino); }
            });
            panelContenido.add(btn);
            panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(panelContenido, BorderLayout.CENTER);

        btnCambiarPassword.addActionListener(e -> new CambiarPassword(jugador).setVisible(true));

        btnEliminarCuenta.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog(this, "Ingrese su password actual:");
            if (pass != null && pass.equals(jugador.getPassword())) {
                logica.eliminarJugador(jugador);
                JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente");
                dispose();
                SwingUtilities.getWindowAncestor(this).dispose();
                new MenuInicio().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Password incorrecto");
            }
        });
    }
}
