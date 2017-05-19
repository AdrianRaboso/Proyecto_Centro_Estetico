package com.example.adrin.proyecto_centro_estetico.model;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Adrián on 29/04/2017.
 */

public class Cita {
    private int cod_cita;
    private Cliente cod_cliente;
    private Tratamiento cod_tratamiento;
    private Date fecha_cita;
    private Time horaInicio;
    private Time horaFin;

    public Cita(int cod_cita, Cliente cod_cliente, Tratamiento cod_tratamiento, Date fecha_cita, Time horaInicio, Time horaFin) {
        this.cod_cita = cod_cita;
        this.cod_cliente = cod_cliente;
        this.cod_tratamiento = cod_tratamiento;
        this.fecha_cita = fecha_cita;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public void setCod_cita(int cod_cita) {
        this.cod_cita = cod_cita;
    }

    public void setCod_cliente(Cliente cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    public void setCod_tratamiento(Tratamiento cod_tratamiento) {
        this.cod_tratamiento = cod_tratamiento;
    }

    public void setFecha_cita(Date fecha_cita) {
        this.fecha_cita = fecha_cita;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }

    public int getCod_cita() {
        return cod_cita;
    }

    public Cliente getCod_cliente() {
        return cod_cliente;
    }

    public Tratamiento getCod_tratamiento() {
        return cod_tratamiento;
    }

    public Date getFecha_cita() {
        return fecha_cita;
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public Time getHoraFin() {
        return horaFin;
    }
}
