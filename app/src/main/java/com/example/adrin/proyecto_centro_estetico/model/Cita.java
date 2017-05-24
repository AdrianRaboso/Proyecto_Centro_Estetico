package com.example.adrin.proyecto_centro_estetico.model;

import java.util.ArrayList;

/**
 * Created by Adrián on 29/04/2017.
 */

public class Cita {
    private String cod_cita;
    private String cod_cliente;
    private String tratamiento;
    private String fecha;
    private int hora;
    public static ArrayList<Cita> listaCitas = new ArrayList<>();

    public Cita(String cod_cita, String tratamiento, String fecha_cita, int hora) {
        this.cod_cita = cod_cita;
        this.tratamiento = tratamiento;
        this.hora = hora;
        this.fecha = fecha_cita;
    }

    public Cita(String tratamiento, String fecha_cita, int hora) {
        this.tratamiento = tratamiento;
        this.hora = hora;
        this.fecha = fecha_cita;
    }

    public Cita() {
    }

    public static void setListaCitas(ArrayList<Cita> listaCitas) {
        Cita.listaCitas = listaCitas;
    }

    public static ArrayList<Cita> getListaCitas() {
        return listaCitas;
    }

    public void setCod_cliente(String cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    public String getCod_cliente() {
        return cod_cliente;
    }

    public void setCod_cita(String cod_cita) {
        this.cod_cita = cod_cita;
    }


    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getHora() {
        return hora;
    }

    public String getCod_cita() {
        return cod_cita;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getTratamiento() {
        return tratamiento;
    }
}
