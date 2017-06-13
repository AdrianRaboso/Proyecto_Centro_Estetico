package com.example.adrin.proyecto_centro_estetico;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.adrin.proyecto_centro_estetico.model.Oferta;
import com.example.adrin.proyecto_centro_estetico.model.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CrearOfertaActivity extends AppCompatActivity {

    private Button confirmar, cancelar;
    private EditText nombre, descripcion;
    private FirebaseDatabase database;
    private DatabaseReference refOfertas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_oferta);

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crear_oferta);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Recogemos los datos de la BD
        database = FirebaseDatabase.getInstance();
        refOfertas = database.getReference("Ofertas");

        //Buscamos los componentes
        confirmar = (Button) findViewById(R.id.btn_crear_aceptar_oferta);
        cancelar = (Button) findViewById(R.id.btn_crear_cancelar_oferta);
        nombre = (EditText) findViewById(R.id.nombre_oferta);
        descripcion = (EditText) findViewById(R.id.descripcion_nuevo_oferta);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCamposRellenos()) {
                    Oferta oferta = new Oferta(descripcion.getText().toString(), nombre.getText().toString());
                    refOfertas.push().setValue(oferta);
                    vaciarCampos();
                    Snackbar.make(getCurrentFocus(), R.string.oferta_creada_exito, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else {
                    Snackbar.make(getCurrentFocus(), R.string.error_campos_vacios, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaciarCampos();
            }
        });
    }

    private boolean isCamposRellenos() {
        return !nombre.getText().toString().equals("") && !descripcion.getText().toString().equals("");
    }

    private void vaciarCampos() {
        nombre.setText("");
        descripcion.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
