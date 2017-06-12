package com.example.adrin.proyecto_centro_estetico;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adrin.proyecto_centro_estetico.model.Cita;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.GregorianCalendar;


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
        database = FirebaseDatabase.getInstance();
        refCitas = database.getReference("Citas");

        //Ponemos la barra
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
                datosCita.setCod_cliente(Utils.currentUser());
                refCitas.push().setValue(datosCita);
                //Añadimos la cita al calendario
                addCitaCalendario();
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

    private void addCitaCalendario() {
        AlertDialog.Builder calendarDialog = new AlertDialog.Builder(ResumenCitaActivity.this);
        calendarDialog.setTitle("Añadir evento");
        calendarDialog.setMessage("¿Quieres añadir además el evento al calendario de tu dispositivo?");
        calendarDialog.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Ponemos la cita en el calendario
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, "Cita estética para " + tratamiento.getText());
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Avenue Beauty");
                //calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "A Pig Roast on the Beach");
                int[] fechaInt = sacarFecha(fecha.getText().toString());
                GregorianCalendar calDate = new GregorianCalendar(fechaInt[0], fechaInt[1], fechaInt[2]);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis());
                startActivityForResult(calIntent, 1);
            }
        });
        calendarDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent returnIntent = new Intent();
                setResult(CANCELAR_CITA, returnIntent);
                finish();//Cerramos la vista resumen y volvemos a la anterior
            }
        });
        calendarDialog.show();

        //Enviamos un mensaje al centro de belleza
        if (Utils.IS_ENVIAR) {
            String emailSubject = "Cita reservada";
            String emailBody = "Cita del usuario " + Utils.currentUser() + " con fecha " + fecha.getText().toString() + " y hora " + hora.getText().toString() + ", desea un tratamiento de " + tratamiento.getText().toString();
            Utils.crearMensajeTienda(emailSubject, emailBody);
        }
    }

    private int[] sacarFecha(String fechaString) {
        int[] fechaInt = new int[3];
        char[] arrayChar = fechaString.toCharArray();
        String f = "";
        int cont = 0;
        for (int i = 0; i < arrayChar.length; i++) {
            if (arrayChar[i] == '/') {
                fechaInt[cont] = Integer.parseInt(f);
                f = "";
                cont++;
            } else {
                f = f + arrayChar[i];
            }
        }
        fechaInt[cont] = Integer.parseInt(f);
        return fechaInt;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent returnIntent = new Intent();
        setResult(CANCELAR_CITA, returnIntent);
        finish();//Cerramos la vista resumen y volvemos a la anterior
    }
}
