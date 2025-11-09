/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuInicio;

import javax.swing.*;
import java.awt.*;
import LogicaJuego.LogicaGUI;

public class MenuInicio extends JFrame {

    private LogicaGUI logica;

    public MenuInicio() {
        logica = new LogicaGUI();

        setTitle("VAMPIRE WARGAME");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        Color vino = new Color(90, 0, 40);
        Color negro = new Color(0, 0, 0);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(negro);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        JLabel titulo = new JLabel("VAMPIRE WARGAME", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(vino);
        panelTitulo.add(titulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(negro);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton btnLogIn = new JButton("LOG IN");
        JButton btnCrear = new JButton("CREAR PLAYER");
        JButton btnSalir = new JButton("SALIR");

        JButton[] botones = {btnLogIn, btnCrear, btnSalir};
        for (JButton btn : botones) {
            btn.setFont(new Font("Arial", Font.BOLD, 22));
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

        btnLogIn.addActionListener(e -> new LogIn(logica, this).setVisible(true));
        btnCrear.addActionListener(e -> new CrearPlayer(logica, this).setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0));
    }

}
