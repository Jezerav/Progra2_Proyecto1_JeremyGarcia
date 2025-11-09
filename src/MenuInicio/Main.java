/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuInicio;

import Juego.Tablero;
import LogicaJuego.Player;
import java.util.Calendar;
import javax.swing.SwingUtilities;

/**
 *
 * @author jerem
 */
public class Main {
    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new MenuInicio().setVisible(true));
        Calendar fecha = Calendar.getInstance();
        Player jugador1 = new Player("Jugador1", "pass1", 0, fecha, true);
        Player jugador2 = new Player("Jugador2", "pass2", 0, fecha, true);

        Tablero tablero = new Tablero(jugador1, jugador2);
        tablero.setVisible(true);
    }
}
