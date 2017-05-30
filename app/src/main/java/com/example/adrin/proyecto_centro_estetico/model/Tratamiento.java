package com.example.adrin.proyecto_centro_estetico.model;

import java.util.ArrayList;

/**
 * Created by Adrián on 28/04/2017.
 */

public class Tratamiento {
    private String cod_tratamiento;
    private String nombre;
    private String categoria;
    private double precio;
    private String esteticista;
    private int duracion;
    public static ArrayList<Tratamiento> listaTratamientos = new ArrayList<>();

    public Tratamiento() {

    }

    public Tratamiento(String nombre, String categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public Tratamiento(String codtratamiento, String nombre, String categoria, double precio, String esteticista, int duracion) {
        this.cod_tratamiento = codtratamiento;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.esteticista = esteticista;
        this.duracion = duracion;
    }

    public void setCod_tratamiento(String cod_tratamiento) {
        this.cod_tratamiento = cod_tratamiento;
    }

    public String getCod_tratamiento() {
        return cod_tratamiento;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setEsteticista(String esteticista) {
        this.esteticista = esteticista;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getNombre() {

        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public String getEsteticista() {
        return esteticista;
    }

    public int getDuracion() {
        return duracion;
    }
}
