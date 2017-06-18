package com.example.adrin.proyecto_centro_estetico;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InformacionActivity extends AppCompatActivity implements View.OnClickListener {

    TextView telefono, correoEmp;
    ImageButton botLocalizacion, botMail, botLLamar;
    String TELEFONO;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_informacion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        telefono = (TextView) this.findViewById(R.id.num_telf);
        correoEmp = (TextView) this.findViewById(R.id.correo_empresa);
        botLocalizacion = (ImageButton) this.findViewById(R.id.botonLocalizacion);
        botLLamar = (ImageButton) this.findViewById(R.id.botonLlamar);
        botMail = (ImageButton) this.findViewById(R.id.botonCorreo);

        botLocalizacion.setOnClickListener(this);
        botLLamar.setOnClickListener(this);
        botMail.setOnClickListener(this);

        telefono.setText(Utils.TELEFONO);
        TELEFONO = telefono.getText().toString();

        correoEmp.setText(Utils.CORREO);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == botLLamar.getId()) {
            hacerLlamada(TELEFONO);
        } else if (view.getId() == botMail.getId()) {
            crearDialogoMensaje();
        } else if (view.getId() == botLocalizacion.getId()) {
            Intent maps = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:@40.4559471,-3.6780999,17?z=16&q=@40.4559471,-3.6780999,17(Centro estetica)"));
            maps.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(maps);
        }
    }

    private void hacerLlamada(String telefonoTo) {
        //Pide acceso a los permisos en tiempo de ejecucion si todavía no se les ha concedido a la aplicacion
        if (ActivityCompat.checkSelfPermission(InformacionActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InformacionActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 12);
            Intent llamada = new Intent(Intent.ACTION_CALL);
            llamada.setData(Uri.parse("tel:+34" + telefonoTo));
            if (ActivityCompat.checkSelfPermission(InformacionActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(llamada);
                finish();
            }
        } else {
            Snackbar.make(getCurrentFocus(), "La aplicación no tiene permisos para hacer llamadas", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
    }

    private void crearDialogoMensaje() {
        AlertDialog.Builder alertMensaje = new AlertDialog.Builder(InformacionActivity.this);
        alertMensaje.setTitle(R.string.title_dialog_mensaje);

        LayoutInflater inflater = InformacionActivity.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_escribir_correo, null);
        alertMensaje.setView(layout);
        final EditText mensaje = (EditText) layout.findViewById(R.id.enviar_correo);

        alertMensaje.setPositiveButton(R.string.btn_resumen_aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!mensaje.getText().toString().equals("")) {
                    //Enviamos un mensaje del centro de belleza
                    String emailSubject = "Mensaje privado de " + Utils.currentUser();
                    String emailBody = mensaje.getText().toString();
                    Utils.crearMensajeTienda(emailSubject, emailBody);
                }
            }
        });
        alertMensaje.setNegativeButton(R.string.cancelar, null);
        alertMensaje.show();
    }
}
