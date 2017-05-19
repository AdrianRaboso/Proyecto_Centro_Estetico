package com.example.adrin.proyecto_centro_estetico.model;

/**
 * Created by Adrián on 28/04/2017.
 */

public class Tratamiento {
    private String nombre;
    private String categoria;
    private float precio;
    private String nom_esteticista;
    private int duracion;

    public Tratamiento() {

    }

    public Tratamiento(String nom, String cat, float pre, String est, int dur) {
        nombre = nom;
        categoria = cat;
        precio = pre;
        nom_esteticista = est;
        duracion = dur;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public float getPrecio() {
        return precio;
    }

    public String getNom_esteticista() {
        return nom_esteticista;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setNom_esteticista(String nom_esteticista) {
        this.nom_esteticista = nom_esteticista;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
