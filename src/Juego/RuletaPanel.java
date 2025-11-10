/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Juego;

/**
 *
 * @author jerem
 */
import javax.swing.*;
import java.awt.*;

public class RuletaPanel extends JPanel {

    private ImageIcon[] icons;  // 6 piezas
    private double angulo = 0; // fondo
    private double anguloPiezas = 0; // piezas girando al contrario

    public RuletaPanel(ImageIcon[] icons) {
        this.icons = icons;
        setPreferredSize(new Dimension(180, 180));
        setBackground(Color.BLACK); // fondo negro sólido
    }

    public double getAngulo() {
        return angulo;
    }

    public void setAngulo(double angulo) {
        this.angulo = angulo % 360;
        repaint();
    }

    public double getAnguloPiezas() {
        return anguloPiezas;
    }

    public void setAnguloPiezas(double ang) {
        this.anguloPiezas = ang % 360;
        repaint();
    }

    // Devuelve el índice de la pieza que está arriba (flecha amarilla)
    public int getIndiceSeleccionado() {
        double anguloArriba = (360 - anguloPiezas + 30) % 360; // centramos sector
        return (int) (anguloArriba / 60) % 6;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int radio = Math.min(w, h) / 2 - 10;
        int cx = w / 2;
        int cy = h / 2;

        // Dibujar fondo dividido en 6 sectores (negro y gris)
        for (int i = 0; i < 6; i++) {
            g2.setColor(i % 2 == 0 ? Color.GRAY : Color.DARK_GRAY);
            g2.fillArc(cx - radio, cy - radio, 2 * radio, 2 * radio, (int) (i * 60 + angulo), 60);
        }

        // Dibujar piezas centradas dentro de su sector
        for (int i = 0; i < 6; i++) {
            double sectorAng = Math.toRadians(i * 60 + 30 + anguloPiezas);
            int px = (int) (cx + Math.cos(sectorAng) * (radio - 35) - icons[i].getIconWidth() / 2);
            int py = (int) (cy + Math.sin(sectorAng) * (radio - 35) - icons[i].getIconHeight() / 2);
            icons[i].paintIcon(this, g2, px, py);
        }

        // Flecha amarilla arriba
        Polygon flecha = new Polygon();
        flecha.addPoint(cx - 10, cy - radio - 5);
        flecha.addPoint(cx + 10, cy - radio - 5);
        flecha.addPoint(cx, cy - radio + 15);
        g2.setColor(Color.YELLOW);
        g2.fill(flecha);
    }
}
