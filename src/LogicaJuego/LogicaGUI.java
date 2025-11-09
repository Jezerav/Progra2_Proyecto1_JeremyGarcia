/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class LogicaGUI {

    private ArrayList<Player> jugadores;

    public LogicaGUI() {
        jugadores = new ArrayList<>();
    }

    public ArrayList<Player> getJugadores() {
        return jugadores;
    }

    public boolean agregarJugador(Player p) {
        if (!isUsernameUnico(p.getUsername())) {
            return false;
        }
        jugadores.add(p);
        return true;
    }

    public boolean isUsernameUnico(String username) {
        for (Player p : jugadores) {
            if (p.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validarPasswordGUI(String password) {
        if (password.length() != 5) return false;
        if (!password.matches(".*[0-9].*")) return false;
        if (!password.matches(".*[!@#$%^&*()].*")) return false;
        return true;
    }

    public Player buscarJugador(String username, String password) {
        for (Player p : jugadores) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Player> obtenerRanking() {
        ArrayList<Player> ranking = new ArrayList<>(jugadores);
        Collections.sort(ranking, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.getPuntos() - o1.getPuntos();
            }
        });
        return ranking;
    }

    public void eliminarJugador(Player p) {
        jugadores.remove(p);
    }

    public Calendar obtenerFechaActual() {
        return Calendar.getInstance();
    }
}
