/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

import java.util.Calendar;

public class Player {

    private String username;
    private String password;
    private int puntos;
    private Calendar fechaIngreso;
    private boolean activo;

    public Player(String username, String password, int puntos, Calendar fechaIngreso, boolean activo) {
        this.username = username;
        this.password = password;
        this.puntos = puntos;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPuntos() {
        return puntos;
    }

    public Calendar getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setFechaIngreso(Calendar fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Método para obtener fecha en formato legible dd/mm/yyyy
    public String getFechaString() {
        int dia = fechaIngreso.get(Calendar.DAY_OF_MONTH);
        int mes = fechaIngreso.get(Calendar.MONTH) + 1; // Calendar.MONTH empieza en 0
        int anio = fechaIngreso.get(Calendar.YEAR);
        return dia + "/" + mes + "/" + anio;
    }
}
