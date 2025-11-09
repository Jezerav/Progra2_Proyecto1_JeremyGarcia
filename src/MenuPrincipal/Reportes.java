/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import LogicaJuego.LogicaGUI;
import LogicaJuego.Player;

public class Reportes extends JFrame {

    private LogicaGUI logica;

    public Reportes(LogicaGUI logica) {
        this.logica = logica;

        setTitle("REPORTES");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        Color vino = new Color(90, 0, 40);
        Color negro = new Color(0, 0, 0);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(negro);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel lblTitulo = new JLabel("REPORTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(vino);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 16));

        // Tab Ranking
        String[] columnasRanking = {"Username", "Puntos", "Activo"};
        ArrayList<Player> jugadoresOrdenados = logica.obtenerRanking();
        DefaultTableModel modeloRanking = new DefaultTableModel(columnasRanking, 0);

        for (Player p : jugadoresOrdenados) {
            Object[] fila = {p.getUsername(), p.getPuntos(), p.isActivo()};
            modeloRanking.addRow(fila);
        }

        JTable tablaRanking = new JTable(modeloRanking);
        tablaRanking.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaRanking.setRowHeight(25);
        JScrollPane scrollRanking = new JScrollPane(tablaRanking);
        tabs.addTab("Ranking Jugadores", scrollRanking);

        // Tab Logs (simulado con fecha de ingreso y puntos)
        String[] columnasLogs = {"Username", "Fecha Ingreso", "Puntos"};
        DefaultTableModel modeloLogs = new DefaultTableModel(columnasLogs, 0);
        for (Player p : logica.getJugadores()) {
            Object[] fila = {p.getUsername(), p.getFechaString(), p.getPuntos()};
            modeloLogs.addRow(fila);
        }

        JTable tablaLogs = new JTable(modeloLogs);
        tablaLogs.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaLogs.setRowHeight(25);
        JScrollPane scrollLogs = new JScrollPane(tablaLogs);
        tabs.addTab("Logs Últimos Juegos", scrollLogs);

        add(tabs, BorderLayout.CENTER);

        // Botón Volver
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(negro);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btnVolver(panelBotones, vino);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void btnVolver(JPanel panel, Color vino) {
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 16));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(vino);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setMaximumSize(new Dimension(150, 40));
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVolver.setBackground(new Color(150, 0, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVolver.setBackground(vino);
            }
        });
        btnVolver.addActionListener(e -> dispose());
        panel.add(btnVolver);
    }
}
