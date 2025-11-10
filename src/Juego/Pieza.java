/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Juego;

/**
 *
 * @author jerem
 */

import LogicaJuego.Player;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;

public abstract class Pieza {
    protected Player retador;
    protected int ataque;
    protected int vida;
    protected int escudo;
    protected ImageIcon imagen;

    public Pieza(Player retador, int ataque, int vida, int escudo, String nombreArchivo) {
        this.retador = retador;
        this.ataque = ataque;
        this.vida = vida;
        this.escudo = escudo;
        cargarImagen(nombreArchivo);
    }

    protected void cargarImagen(String nombreArchivo) {
    String ruta = "/Recursos/" + nombreArchivo;
    java.net.URL location = getClass().getResource(ruta);
    if(location != null) {
        ImageIcon icon = new ImageIcon(location);
        Image img = icon.getImage().getScaledInstance(60,60,Image.SCALE_SMOOTH);
        imagen = new ImageIcon(img);
    } else {
        System.out.println("No se encontró: " + ruta);
        imagen = null;
    }
}

    
    public ImageIcon getImagen() { return imagen; }
    public Player getRetador() { return retador; }
    public int getAtaque() { return ataque; }
    public int getVida() { return vida; }
    public int getEscudo() { return escudo; }

    public final void recibirDanio(int cantidad) {
    if (escudo > 0) {
        // Si hay escudo, se reduce primero el escudo
        int restante = escudo - cantidad;
        if (restante >= 0) {
            escudo = restante; // Todo el daño fue absorbido
            return;
        } else {
            escudo = 0; // Se rompió el escudo
            cantidad = -restante; // Daño restante pasa a la vida
        }
    }

    // Si no hay escudo, o ya se rompió
    vida -= cantidad;
    if (vida < 0) vida = 0;
}


    public final void recibirSanacion(int cantidad) { 
        vida += cantidad; 
    }
    
    public void recibirDanioEspecial(int cantidad) { 
        vida -= cantidad; 
        if(vida<0) 
            vida=0; 
    }
    public boolean estaViva() { 
        return vida > 0; 
    }

    // Métodos abstractos obligatorios
    public abstract void accionEspecial(Pieza[][] tablero, int filaOrigen, int colOrigen, int filaDestino, int colDestino, int opcion);
    public abstract List<int[]> obtenerCasillasEspecial(Pieza[][] tablero, int fila, int col, int opcion);
    // Método abstracto para conjurar
public abstract List<int[]> obtenerCasillasConjurar(Pieza[][] tablero, int fila, int col);

public abstract String getTipo();

}