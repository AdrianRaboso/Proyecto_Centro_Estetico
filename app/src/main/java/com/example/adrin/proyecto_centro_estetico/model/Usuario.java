package com.example.adrin.proyecto_centro_estetico.model;

/**
 * Created by Adrián on 12/06/2017.
 */

public class Usuario {
    private String Dni;
    private String Correo;
    private String Nombre;
    private String Telefono;
    private boolean Alergias;
    private boolean Disp_Medicos;
    private boolean Medicamentos;
    private boolean Enfermedades;

    public Usuario() {
    }

    public Usuario(String correo, String nombre) {
        Correo = correo;
        Nombre = nombre;
    }

    public Usuario(String dni, String correo, String nombre, String telefono, boolean alergias, boolean disp_Medicos, boolean medicamentos, boolean enfermedades) {
        Dni = dni;
        Correo = correo;
        Nombre = nombre;
        Telefono = telefono;
        Alergias = alergias;
        Disp_Medicos = disp_Medicos;
        Medicamentos = medicamentos;
        Enfermedades = enfermedades;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public void setAlergias(boolean alergias) {
        Alergias = alergias;
    }

    public void setDisp_Medicos(boolean disp_Medicos) {
        Disp_Medicos = disp_Medicos;
    }

    public void setMedicamentos(boolean medicamentos) {
        Medicamentos = medicamentos;
    }

    public void setEnfermedades(boolean enfermedades) {
        Enfermedades = enfermedades;
    }

    public String getDni() {
        return Dni;
    }

    public String getCorreo() {
        return Correo;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public boolean isAlergias() {
        return Alergias;
    }

    public boolean isDisp_Medicos() {
        return Disp_Medicos;
    }

    public boolean isMedicamentos() {
        return Medicamentos;
    }

    public boolean isEnfermedades() {
        return Enfermedades;
    }
}
