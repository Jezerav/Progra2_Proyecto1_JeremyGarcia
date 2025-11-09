/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Juego;

import LogicaJuego.Player;

import java.util.ArrayList;
import java.util.List;

public final class Lobo extends Pieza {
    public Lobo(Player retador, String archivo) { super(retador,5,5,2,archivo); }

    @Override
    public void accionEspecial(Pieza[][] tablero, int filaOrigen, int colOrigen, int filaDestino, int colDestino, int opcion) {
        // Movimiento especial: solo a casilla VACÍA hasta 2 casillas
        if(tablero[filaDestino][colDestino]==null) {
            tablero[filaDestino][colDestino] = this;
            tablero[filaOrigen][colOrigen] = null;
        }
    }

    @Override
    public List<int[]> obtenerCasillasEspecial(Pieza[][] tablero, int fila, int col, int opcion) {
        List<int[]> disponibles = new ArrayList<>();
        // solo desplazamientos con dr,dc en {-2,-1,0,1,2} excepto 0,0 pero sólo los destinos VACÍOS
        int[] dir = {-2,-1,0,1,2};
        for(int df: dir) for(int dc: dir) {
            if(df==0 && dc==0) continue;
            int nf=fila+df, nc=col+dc;
            if(nf>=0 && nf<6 && nc>=0 && nc<6 && tablero[nf][nc]==null) {
                // También evitar movimientos "sobre la misma casilla" (ya filtrado) y permitir cualquier dirección
                disponibles.add(new int[]{nf,nc});
            }
        }
        return disponibles;
    }
    
    public List<int[]> obtenerCasillasConjurar(Pieza[][] tablero, int fila, int col) {
        // Vampiro no conjura nada, devuelve vacío
        return new ArrayList<>();
    }
}
