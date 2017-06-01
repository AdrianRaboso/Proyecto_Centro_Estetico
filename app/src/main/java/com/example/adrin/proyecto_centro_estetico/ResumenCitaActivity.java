package com.example.adrin.proyecto_centro_estetico;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adrin.proyecto_centro_estetico.model.Cita;
import com.example.adrin.proyecto_centro_estetico.model.Hora;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ResumenCitaActivity extends AppCompatActivity {

    private TextView tratamiento, fecha, hora;
    private Button confirmar, cancelar;
    private final int ACEPTAR_CITA = 1;
    private final int CAMBIAR_CITA = 2;
    private final int CANCELAR_CITA = 3;
    private FirebaseDatabase database;
    private DatabaseReference refCitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_cita);

        //Firebase
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        refCitas = database.getReference("Citas");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Recogemos los campos de la vista
        tratamiento = (TextView) findViewById(R.id.txt_resumen_tratamiento);
        fecha = (TextView) findViewById(R.id.txt_resumen_fecha);
        hora = (TextView) findViewById(R.id.txt_resumen_hora);
        confirmar = (Button) findViewById(R.id.btn_resumen_aceptar);
        cancelar = (Button) findViewById(R.id.btn_resumen_cancelar);


        //Guardamos los datos que nos pasa el activity anterior
        tratamiento.setText(getIntent().getExtras().getString("tratamiento"));
        String fechaString = getIntent().getExtras().getString("fecha");
        String horaString;
        if (getIntent().getExtras().getInt("hora") < 10) {
            horaString = "0" + getIntent().getExtras().getInt("hora") + ":00";
        } else {
            horaString = getIntent().getExtras().getInt("hora") + ":00";
        }
        fecha.setText(fechaString);
        hora.setText(horaString);

        //Ponemos a la escucha los botones
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(ACEPTAR_CITA, returnIntent);
                //Añadimos la cita a la base de datos
                Cita datosCita = new Cita(tratamiento.getText().toString(), fecha.getText().toString(), getIntent().getExtras().getInt("hora"));
                //Sacamos el usuario que esta logueado
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                datosCita.setCod_cliente(user.getEmail());
                refCitas.push().setValue(datosCita);
                finish();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder cancelDialog = new AlertDialog.Builder(ResumenCitaActivity.this);
                cancelDialog.setTitle("Cambiar la cita");
                cancelDialog.setMessage("¿Quieres hacer algún cambio?");
                cancelDialog.setPositiveButton("CAMBIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        setResult(CAMBIAR_CITA, returnIntent);
                        finish();//Cerramos la vista resumen y volvemos a la anterior
                    }
                });
                cancelDialog.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        setResult(CANCELAR_CITA, returnIntent);
                        finish();//Cerramos la vista resumen y volvemos a la anterior
                    }
                });
                cancelDialog.show();
            }
        });
    }

}
