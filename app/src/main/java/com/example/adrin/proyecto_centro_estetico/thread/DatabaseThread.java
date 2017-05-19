package com.example.adrin.proyecto_centro_estetico.thread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Adrián on 27/04/2017.
 */

public class DatabaseThread extends Thread {
    private Connection conn;

    @Override
    public void run() {
        try {
            System.out.println("CONEXION REALIZADA CON EXITO!!!!!!!!!!!");
            //En el stsql se puede agregar cualquier consulta SQL deseada.
            String stsql = "SELECT * FROM schema_centro_estetico.tratamiento ORDER BY cod_tratamiento ASC ";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(stsql);
            rs.next();
            System.out.println();
        } catch (SQLException se) {
            System.out.println("oops! No se puede conectar. Error: " + se.toString());
        }
    }

    public void conectarBD() {
        // Solo te deja conectarte una vez
        if (conn == null) {
            try {
                Class.forName("org.postgresql.Driver");
                // "jdbc:postgresql://IP:PUERTO/DB", "USER", "PASSWORD");
                // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                conn = DriverManager.getConnection("jdbc:postgresql://10.0.2.2:5454/bd_centro_estetico", "postgres", "manager");
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }
        }
    }

    public void desconectarBD() {
        //Solo deja desconectarte si estaba conectado previamente
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cargarDatos(){

    }
}

