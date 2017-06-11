package com.example.adrin.proyecto_centro_estetico;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Adrián on 05/06/2017.
 */

public class Utils {
    public static String user = "";
    public static String CORREO = "AvenueBeautyCenter@gmail.com";
    public static String PASS = "avenuebeauty";
    public static String PROPIETARIO = "raborano@gmail.com";

    public static String currentUser() {
        //Sacamos el usuario
        //if (user.equals("")) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser usuario = firebaseAuth.getCurrentUser();
            user = usuario.getEmail();
        //}
        return user;
    }

    public static void getPropietario() {
        //Sacamos el propietario de la app
        FirebaseDatabase database;
        DatabaseReference refCitas;

        database = FirebaseDatabase.getInstance();
        refCitas = database.getReference("Propietario");
        refCitas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PROPIETARIO = dataSnapshot.child("correo").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void crearMensaje(String subject, String body) {
        List toEmailList = Arrays.asList(Utils.CORREO.split("\\s*,\\s*"));
        new SendEmailTask().execute(Utils.CORREO, Utils.PASS, toEmailList, subject, body);
    }

}
