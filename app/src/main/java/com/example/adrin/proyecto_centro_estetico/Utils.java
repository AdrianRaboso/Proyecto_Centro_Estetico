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
    public static String CORREO = "avenuebeautycenter@gmail.com";
    public static String PASS = "avenuebeauty";
    public static String PROPIETARIO = "avenuebeautycenter@gmail.com";
    public static String TELEFONO = "636952978";
    public static boolean IS_ENVIAR = true;

    public static String currentUser() {
        //Sacamos el usuario
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        user = usuario.getEmail();
        return user;
    }

    public static void crearResgistroAutomaticoUsuario() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference refUsuarios = database.getReference("Usuarios");

        //Si da un error es que no existe entonces le generamos valores por defecto
        refUsuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    if (user != null) {
                        //Usuario usuario = new Usuario("", user.getEmail(), user.getDisplayName(), "", false, false, false, false);
                        String id = user.getUid();
                        //refUsuarios.child("Usuarios").child(id).push().setValue(usuario);
                        refUsuarios.child(id).child("Alergias").setValue(false);
                        refUsuarios.child(id).child("Medicamentos").setValue(false);
                        refUsuarios.child(id).child("Disp_medicos").setValue(false);
                        refUsuarios.child(id).child("Enfermedades").setValue(false);
                        refUsuarios.child(id).child("DNI").setValue("");
                        refUsuarios.child(id).child("Correo").setValue(user.getEmail());
                        refUsuarios.child(id).child("Nombre").setValue("Nuevo usuario");
                        refUsuarios.child(id).child("Telefono").setValue("");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void crearMensajeTienda(String subject, String body) {
        List toEmailList = Arrays.asList(Utils.CORREO.split("\\s*,\\s*"));
        new SendEmailTask().execute(Utils.CORREO, Utils.PASS, toEmailList, subject, body);
    }

    public static void crearMensajeUsuario(String subject, String body, String correoTo) {
        List toEmailList = Arrays.asList(correoTo.split("\\s*,\\s*"));
        new SendEmailTask().execute(Utils.CORREO, Utils.PASS, toEmailList, subject, body);
    }

}
