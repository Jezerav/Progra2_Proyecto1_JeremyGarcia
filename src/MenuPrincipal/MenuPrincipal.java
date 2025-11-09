/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package MenuPrincipal;

import Juego.Tablero;
import javax.swing.*;
import java.awt.*;
import LogicaJuego.Player;
import LogicaJuego.LogicaGUI;
import MenuInicio.MenuInicio;

public class MenuPrincipal extends JFrame {

    private Player jugadorActual;
    private LogicaGUI logica;

    public MenuPrincipal(Player jugador, LogicaGUI logica) {
        this.jugadorActual = jugador;
        this.logica = logica;

        setTitle("VAMPIRE WARGAME - MENU PRINCIPAL");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        Color vino = new Color(90, 0, 40);
        Color negro = new Color(0, 0, 0);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(negro);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        JLabel lblTitulo = new JLabel("VAMPIRE WARGAME", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(vino);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(negro);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton btnJugar = new JButton("JUGAR");
        JButton btnMiCuenta = new JButton("MI CUENTA");
        JButton btnReportes = new JButton("REPORTES");
        JButton btnLogOut = new JButton("LOG OUT");

        JButton[] botones = {btnJugar, btnMiCuenta, btnReportes, btnLogOut};
        for (JButton btn : botones) {
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.setForeground(Color.WHITE);
            btn.setBackground(vino);
            btn.setFocusPainted(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
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
            panelBotones.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        add(panelBotones, BorderLayout.CENTER);

        
        btnJugar.addActionListener(e -> {
            Tablero tablero = new Tablero(jugadorActual, null);
    tablero.setVisible(true);
    dispose();
        });

        btnMiCuenta.addActionListener(e -> new MiCuenta(jugadorActual, logica).setVisible(true));
        btnReportes.addActionListener(e -> new Reportes(logica).setVisible(true));
        btnLogOut.addActionListener(e -> {
            dispose();
            new MenuInicio().setVisible(true);
        });
    }
}
