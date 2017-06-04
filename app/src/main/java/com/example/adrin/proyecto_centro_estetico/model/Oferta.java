package com.example.adrin.proyecto_centro_estetico.model;

/**
 * Created by Adrián on 03/06/2017.
 */

public class Oferta {
    private String descripcion;
    private String tema;
    private String imagen;

    public Oferta() {
    }

    public Oferta(String descripcion, String tema) {
        this.descripcion = descripcion;
        this.tema = tema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTema() {
        return tema;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
