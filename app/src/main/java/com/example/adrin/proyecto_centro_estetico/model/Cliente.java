package com.example.adrin.proyecto_centro_estetico.model;

/**
 * Created by Adrián on 29/04/2017.
 */

public class Cliente {
    private int cod_cliente;
    private String dni;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String correo;
    private String enfermedades;
    private String disp_medicos;
    private String alergias;
    private String medicamentos;

    public Cliente(int cod_cliente, String dni, String nombre, String apellido, String direccion, String telefono, String correo, String enfermedades, String disp_medicos, String alergias, String medicamentos) {
        this.cod_cliente = cod_cliente;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.enfermedades = enfermedades;
        this.disp_medicos = disp_medicos;
        this.alergias = alergias;
        this.medicamentos = medicamentos;
    }

    public void setCod_cliente(int cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setEnfermedades(String enfermedades) {
        this.enfermedades = enfermedades;
    }

    public void setDisp_medicos(String disp_medicos) {
        this.disp_medicos = disp_medicos;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public int getCod_cliente() {
        return cod_cliente;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getEnfermedades() {
        return enfermedades;
    }

    public String getDisp_medicos() {
        return disp_medicos;
    }

    public String getAlergias() {
        return alergias;
    }

    public String getMedicamentos() {
        return medicamentos;
    }
}
