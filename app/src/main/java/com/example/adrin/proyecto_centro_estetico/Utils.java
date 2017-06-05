package com.example.adrin.proyecto_centro_estetico;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Adrián on 05/06/2017.
 */

public class Utils {
    static String user = "";
    static String CORREO = "AvenueBeautyCenter@gmail.com";
    static String PASS = "avenuebeauty";

    public static String currentUser() {
        //Sacamos el usuario
        if (user.equals("")) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser usuario = firebaseAuth.getCurrentUser();
            user = usuario.getEmail();
        }
        return user;
    }

    public static void crearMensaje(String subject, String body) {
        List toEmailList = Arrays.asList(Utils.CORREO.split("\\s*,\\s*"));
        new SendEmailTask().execute(Utils.CORREO, Utils.PASS, toEmailList, subject, body);
    }

}
