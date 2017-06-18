package com.example.adrin.proyecto_centro_estetico;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar bar;
    private TextView txtVersion;
    private TextView txtTitulo;
    private static final int PERMISSIONS_INTERNET = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Pide acceso a los permisos en tiempo de ejecucion si todavía no se les ha concedido a la aplicacion
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSIONS_INTERNET);
        }

        //Sacamos los datos de la tabla propietario de la app
        //Utils.getPropietario();
        //Utils.getEnviarMensajes();

        //Ponemos fondo de pantalla al splahs
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.splash_theme));
        bar = (ProgressBar) findViewById(R.id.barraProgreso);
        txtVersion = (TextView) findViewById(R.id.txt_version);
        txtTitulo = (TextView) findViewById(R.id.txt_titulo);
        txtVersion.setText("v. " + getVersion());
        txtTitulo.setText(R.string.txt_bienvenida);

        final FirebaseDatabase database;
        DatabaseReference refCitas;

        database = FirebaseDatabase.getInstance();
        refCitas = database.getReference("Propietario");
        refCitas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Utils.PROPIETARIO = dataSnapshot.child("correo").getValue().toString();
                Utils.TELEFONO = dataSnapshot.child("telefono").getValue().toString();
                Utils.CORREO = dataSnapshot.child("correo").getValue().toString();
                Utils.IS_ENVIAR = (boolean) dataSnapshot.child("recibirCorreos").getValue();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.PROPIETARIO = "avenuebeautycenter@gmail.com";
                //Toast.makeText(SplashActivity.this, "Ha habido un error al conectarse a la base de datos. Intentelo de nuevo más tarde", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "No ha concedido permisos para acceder a Internet", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private String getVersion() {
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e(this.getClass().getSimpleName(), "Name not found", e1);
        }
        return version;
    }
}
