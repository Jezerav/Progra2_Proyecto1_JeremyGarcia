/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Juego;

import LogicaJuego.Player;
import java.util.ArrayList;

public class Necromancer extends Pieza {

    public Necromancer(Player retador, String nombreArchivo) {
        super(retador, 4, 3, 1, nombreArchivo);
    }

    // --- Acción especial del Necromancer ---
    @Override
    public void accionEspecial(Pieza[][] tablero, int filaOrigen, int colOrigen, int filaDestino, int colDestino, int opcion) {
        // Opción 0 = ataque a 2 casillas de distancia
        if (opcion == 0 && tablero[filaDestino][colDestino] != null
                && tablero[filaDestino][colDestino].getRetador() != this.retador) {
            tablero[filaDestino][colDestino].recibirDanio(this.ataque);
            if (!tablero[filaDestino][colDestino].estaViva()) {
                tablero[filaDestino][colDestino] = null;
            }
        }
        // Las demás opciones se manejan desde Tablero (Conjurar Zombie, Ataque Zombie)
    }

    // --- Casillas disponibles para ataque a 2 ---
    @Override
    public ArrayList<int[]> obtenerCasillasEspecial(Pieza[][] tablero, int fila, int col, int opcion) {
        ArrayList<int[]> res = new ArrayList<>();
        if (opcion == 0) { // Ataque a 2
            int[] offsets = {-2, -1, 0, 1, 2};
            for (int df : offsets) {
                for (int dc : offsets) {
                    if (df == 0 && dc == 0) continue;
                    int nf = fila + df;
                    int nc = col + dc;
                    if (nf >= 0 && nf < 6 && nc >= 0 && nc < 6) {
                        if (tablero[nf][nc] == null || tablero[nf][nc].getRetador() != this.retador) {
                            res.add(new int[]{nf, nc});
                        }
                    }
                }
            }
        }
        return res;
    }

    // --- Casillas disponibles para conjurar zombies ---
    @Override
    public ArrayList<int[]> obtenerCasillasConjurar(Pieza[][] tablero, int fila, int col) {
        ArrayList<int[]> disponibles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (tablero[i][j] == null) {
                    disponibles.add(new int[]{i, j});
                }
            }
        }
        return disponibles;
    }

    // --- Crear un zombie del jugador correspondiente ---
    public Zombie conjurarZombie(int fila, int col) {
        String sprite = retador == null
                ? "zombie.png"
                : (retador.getUsername().equals("Jugador1") ? "zombie.png" : "zombie2.png");
        return new Zombie(retador, sprite, fila, col);
    }
}
