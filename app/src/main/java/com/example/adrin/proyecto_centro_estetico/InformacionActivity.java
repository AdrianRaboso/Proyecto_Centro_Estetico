package com.example.adrin.proyecto_centro_estetico;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class InformacionActivity extends AppCompatActivity implements View.OnClickListener {

    TextView telefono, correoEmp;
    ImageButton botLocalizacion, botMail, botLLamar;
    String TELEFONO;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

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
            Intent correo = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Utils.CORREO, null));
            startActivity(correo);
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
        } else {
            Intent llamada = new Intent(Intent.ACTION_CALL);
            llamada.setData(Uri.parse("tel:+34" + telefonoTo));
            startActivity(llamada);
        }
    }
}
