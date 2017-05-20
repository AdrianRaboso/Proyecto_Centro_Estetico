package com.example.adrin.proyecto_centro_estetico;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.adrin.proyecto_centro_estetico.model.Cita;
import com.example.adrin.proyecto_centro_estetico.model.Hora;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.drozdzynski.library.steppers.OnCancelAction;
import me.drozdzynski.library.steppers.OnChangeStepAction;
import me.drozdzynski.library.steppers.OnFinishAction;
import me.drozdzynski.library.steppers.SteppersItem;
import me.drozdzynski.library.steppers.SteppersView;

public class PedirCitaActivity extends AppCompatActivity implements TratamientoFragment.TratamientosListener, FechaFragment.FechaListener, HorasFragment.OnListFragmentInteractionListener {
    //Create steps list
    private ArrayList<SteppersItem> steps = new ArrayList<>();
    private Bundle datosCita = new Bundle();
    private TratamientoFragment tratamiento;
    private FechaFragment fecha;
    private HorasFragment horas;
    private final int REQUEST_CODE_RESUMEN = 10;
    private final int ACEPTAR_CITA = 1;
    private final int CAMBIAR_CITA = 2;
    private final int CANCELAR_CITA = 3;
    private FirebaseDatabase database;
    private DatabaseReference refHorario;
    private DatabaseReference refCitas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_cita);

        //Referenciamos a la tabla horario
        database = FirebaseDatabase.getInstance();
        refCitas = database.getReference("Citas");
        refHorario = database.getReference("Horario");
        //Cargamos la lista de horario
        Hora.crearLista();

        //Recogemos la lista de citas
        refCitas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Borramos la lista para que se carguen los datos nuevos
                Cita.listaCitas.clear();
                for (DataSnapshot citaSnapshot : snapshot.getChildren()) {
                    String tratamiento = citaSnapshot.child("tratamiento").getValue().toString();
                    String fecha = citaSnapshot.child("fecha").getValue().toString();
                    String horaString = citaSnapshot.child("hora").getValue().toString();
                    int hora = Integer.parseInt(horaString);
                    String id = citaSnapshot.getKey();

                    Cita cita = new Cita(id, tratamiento, fecha, hora);
                    Cita.listaCitas.add(cita);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("La lectura falló: " + databaseError.getMessage());
            }
        });

        //Colocamos el actionBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Setup config for SteppersView
        SteppersView.Config steppersViewConfig = new SteppersView.Config();

        steppersViewConfig.setOnFinishAction(new OnFinishAction() {
            @Override
            public void onFinish() {
                // Action on last step Finish button
                Intent resumen = new Intent(PedirCitaActivity.this, ResumenCitaActivity.class);
                resumen.putExtras(datosCita);
                startActivityForResult(resumen, REQUEST_CODE_RESUMEN);
            }
        });

        steppersViewConfig.setOnCancelAction(new OnCancelAction() {
            @Override
            public void onCancel() {
                // Action when click cancel on one of steps
                finish();
            }
        });

        steppersViewConfig.setOnChangeStepAction(new OnChangeStepAction() {
            @Override
            public void onChangeStep(int position, SteppersItem activeStep) {
                // Si hay elegido ya la fecha rellenamos el horario de ese dia
                if (position == 2) {
                    for (Cita cita : Cita.listaCitas) {
                        //Si la fecha de la cita actual coincide con las que ya estan hechas guardamos la hora
                        if (datosCita.get("fecha").equals(cita.getFecha())) {
                            //Recorremos el horario y marcamos la hora que este ocupada
                            for (Hora hora : Hora.listaHoras) {
                                if (hora.getHora() == cita.getHora()) {
                                    hora.setOcupada(true);
                                }
                            }
                        }
                    }
                }
            }
        });

        //Set config, list and build view;
        steppersViewConfig.setFragmentManager(getSupportFragmentManager());
        SteppersView steppersView = (SteppersView) findViewById(R.id.steppersView);
        steppersView.setConfig(steppersViewConfig);
        crearPasos();
        steppersView.setItems(steps);
        steppersView.build();
    }

    private void crearPasos() {
        //Paso 1: Elegir tratamiento
        SteppersItem stepFirst = new SteppersItem();
        stepFirst.setLabel("Elija su tratamiento favorito");
        tratamiento = new TratamientoFragment();
        tratamiento.setTratamientoListener(this);
        stepFirst.setFragment(tratamiento);
        stepFirst.setPositiveButtonEnable(false);
        steps.add(stepFirst);

        //Paso 2: Elegir Fecha
        SteppersItem stepTwo = new SteppersItem();
        stepTwo.setLabel("Escoja una fecha disponible");
        fecha = new FechaFragment();
        fecha.setFechaListener(this);
        stepTwo.setFragment(fecha);
        stepTwo.setPositiveButtonEnable(false);
        steps.add(stepTwo);

        //Paso 3: Elegir hora
        SteppersItem stepThree = new SteppersItem();
        stepThree.setLabel("Por último, seleccione la hora");
        horas = new HorasFragment();
        horas.onAttach(this);
        stepThree.setFragment(horas);
        stepThree.setPositiveButtonEnable(false);
        steps.add(stepThree);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RESUMEN && resultCode == ACEPTAR_CITA) {
            finish();
        } else if (requestCode == REQUEST_CODE_RESUMEN && resultCode == CANCELAR_CITA) {
            finish();
        } else if (requestCode == REQUEST_CODE_RESUMEN && resultCode == CAMBIAR_CITA) {
            startActivity(new Intent(PedirCitaActivity.this, PedirCitaActivity.class));
            finish();
        }
    }

    @Override
    public void onTratamientoSeleccionado(String c) {
        steps.get(0).setPositiveButtonEnable(true);
        datosCita.putString("tratamiento", c);
        Snackbar.make(getCurrentFocus(), "¡Has elegido " + c + "!", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    public void onFechaSeleccionado(int y, int m, int d) {
        String fechaString = y + "/" + m + "/" + d;
        Date fechaElegida = new Date(y, m, d);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaElegida);
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        if (diaSemana != 1 && diaSemana != 2) {//Si el dia elegido no es fin de semana
            steps.get(1).setPositiveButtonEnable(true);
            datosCita.putString("fecha", fechaString);
        } else {
            Snackbar.make(getCurrentFocus(), "¡Ese día no trabajamos, lo sentimos!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            steps.get(1).setPositiveButtonEnable(false);
        }
    }

    @Override
    public void onListFragmentInteraction(Hora item) {
        steps.get(2).setPositiveButtonEnable(true);
        datosCita.putInt("hora", item.getHora());
    }
}
