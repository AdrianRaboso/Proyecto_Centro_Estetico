package com.example.adrin.proyecto_centro_estetico.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrián on 14/05/2017.
 */

public class Hora {
    private int hora;
    private String ID;
    private boolean ocupada;
    public static ArrayList<Hora> listaHoras = new ArrayList<>();

    public Hora(int hora, boolean ocupada) {
        this.hora = hora;
        this.ocupada = ocupada;
    }

    public Hora(String ID, int hora, boolean ocupada) {
        this.ID = ID;
        this.hora = hora;
        this.ocupada = ocupada;
    }

    public Hora() {
    }

    public static List<Hora> crearLista() {
        Hora hora;
        Hora.listaHoras.clear();
        for (int h = 8; h < 22; h++) {
            hora = new Hora(h, false);
            listaHoras.add(hora);
        }
        return listaHoras;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public static void setListaHoras(ArrayList<Hora> listaHoras) {
        Hora.listaHoras = listaHoras;
    }

    public int getHora() {
        return hora;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public static ArrayList<Hora> getListaHoras() {
        return listaHoras;
    }
}
