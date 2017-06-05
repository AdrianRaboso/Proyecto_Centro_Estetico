package com.example.adrin.proyecto_centro_estetico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DescripcionActivity extends AppCompatActivity {
    private TextView descripcion;
    private Button cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_descripcion);
        setSupportActionBar(toolbar);

        //Mostramos el texto pasado por parametros
        descripcion = (TextView) findViewById(R.id.txt_descripcion_tra);
        descripcion.setText(getIntent().getExtras().getString("descripcion"));

        //Cerramos la descripcion
        cerrar = (Button) findViewById(R.id.btn_descripcion_cancelar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
