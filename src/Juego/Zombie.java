/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Juego;

import LogicaJuego.Player;
import java.util.ArrayList;
import java.util.List;

public class Zombie extends Pieza {

    private int fila, col;

    public Zombie(Player retador, String nombreArchivo, int fila, int col) {
        super(retador, 2, 1, 1, nombreArchivo);
        this.fila = fila;
        this.col = col;
        // Imagen ya cargada desde Pieza
    }

    @Override
    public void accionEspecial(Pieza[][] tablero, int filaOrigen, int colOrigen, int filaDestino, int colDestino, int opcion) {}

    @Override
    public ArrayList<int[]> obtenerCasillasEspecial(Pieza[][] tablero, int fila, int col, int opcion) {
        ArrayList<int[]> disponibles = new ArrayList<>();
        int[] offsets = {-1,0,1};
        for(int df : offsets){
            for(int dc : offsets){
                if(df==0 && dc==0) continue;
                int nf = fila + df, nc = col + dc;
                if(nf>=0 && nf<6 && nc>=0 && nc<6){
                    if(tablero[nf][nc]==null || tablero[nf][nc].getRetador()!=this.retador){
                        disponibles.add(new int[]{nf,nc});
                    }
                }
            }
        }
        return disponibles;
    }

    @Override
    public ArrayList<int[]> obtenerCasillasConjurar(Pieza[][] tablero, int fila, int col) {
        return new ArrayList<>();
    }

    public void atacar(Pieza[][] tablero, int filaDestino, int colDestino) {
        Pieza objetivo = tablero[filaDestino][colDestino];
        if(objetivo != null && objetivo.getRetador() != this.retador){
            objetivo.recibirDanio(this.ataque);
            if(!objetivo.estaViva()){
                tablero[filaDestino][colDestino] = this; // Zombie se mueve
                tablero[fila][col] = null;
                setPosicion(filaDestino, colDestino);
                // Ajustar sprite según jugador
                if(this.retador.getUsername().equals("Jugador1")) setImagen("zombie.png");
                else setImagen("zombie2.png");
            }
        } else if(objetivo == null){
            tablero[filaDestino][colDestino] = this;
            tablero[fila][col] = null;
            setPosicion(filaDestino, colDestino);
        }
    }

    public void setPosicion(int fila, int col){
        this.fila = fila;
        this.col = col;
    }

    public void setImagen(String nombreArchivo){
        cargarImagen(nombreArchivo); // Llama al método protegido de Pieza
    }
}

