/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class DatosJuego {
    private List<Player> jugadores = new ArrayList<>();

    public void agregarJugador(Player p) { jugadores.add(p); }

    public Player buscarJugador(String username) {
        for (Player p : jugadores) {
            if (p.getUsername().equals(username) && p.isActivo()) return p;
        }
        return null;
    }

    public void eliminarJugador(Player p) { p.setActivo(false); }

    public Object[][] obtenerRanking() {
        Object[][] ranking = new Object[jugadores.size()][3];
        for (int i = 0; i < jugadores.size(); i++) {
            Player p = jugadores.get(i);
            ranking[i][0] = p.getUsername();
            ranking[i][1] = p.getPuntos();
            ranking[i][2] = calcularNivel(p.getPuntos());
        }

        Arrays.sort(ranking, new Comparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                int puntos1 = (int) o1[1];
                int puntos2 = (int) o2[1];
                return Integer.compare(puntos2, puntos1);
            }
        });

        Object[][] rankingFinal = new Object[ranking.length][4];
        for (int i = 0; i < ranking.length; i++) {
            rankingFinal[i][0] = i + 1;
            rankingFinal[i][1] = ranking[i][0];
            rankingFinal[i][2] = ranking[i][1];
            rankingFinal[i][3] = ranking[i][2];
        }
        return rankingFinal;
    }

    public Object[][] obtenerLogs(Player jugador) {
        return new Object[][] {
            {"03/11/2025", "Victoria", "Carmilla", "12"},
            {"02/11/2025", "Derrota", "Belmont", "9"},
            {"01/11/2025", "Victoria", "Bestia de la Niebla", "15"}
        };
    }

    private int calcularNivel(int puntos) { return puntos / 500; }
}
