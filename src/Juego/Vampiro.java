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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public final class Vampiro extends Pieza {
    public Vampiro(Player retador, String archivo) { super(retador,3,4,5,archivo); }

    @Override
    public void accionEspecial(Pieza[][] tablero, int filaOrigen, int colOrigen, int filaDestino, int colDestino, int opcion) {
        Pieza enemigo = tablero[filaDestino][colDestino];
        if(enemigo!=null && enemigo.getRetador()!=retador) {
            if(opcion==1) { // chupar sangre (opción 1)
                enemigo.recibirDanioEspecial(1); // quita 1 vida directa
                this.recibirSanacion(1);         // vampiro se cura 1
                if(!enemigo.estaViva()) tablero[filaDestino][colDestino]=null;
            } else { // ataque normal (opción 0)
                enemigo.recibirDanioEspecial(3);
                if(!enemigo.estaViva()) tablero[filaDestino][colDestino]=null;
            }
        }
    }

    @Override
    public List<int[]> obtenerCasillasEspecial(Pieza[][] tablero, int fila, int col, int opcion) {
        List<int[]> disponibles = new ArrayList<>();
        int[] dir = {-1,0,1};
        for(int df: dir) for(int dc: dir) {
            if(df==0 && dc==0) continue;
            int nf=fila+df, nc=col+dc;
            if(nf>=0 && nf<6 && nc>=0 && nc<6) {
                Pieza p = tablero[nf][nc];
                if(p!=null && p.getRetador()!=retador) disponibles.add(new int[]{nf,nc});
            }
        }
        return disponibles;
    }
    
    public List<int[]> obtenerCasillasConjurar(Pieza[][] tablero, int fila, int col) {
        // Vampiro no conjura nada, devuelve vacío
        return new ArrayList<>();
    }
}
