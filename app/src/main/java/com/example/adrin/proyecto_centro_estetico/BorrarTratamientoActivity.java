package com.example.adrin.proyecto_centro_estetico;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BorrarTratamientoActivity extends AppCompatActivity {

    private ListView lista;
    private ArrayList listaTra = new ArrayList();
    private ArrayList listaImg = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_borrar_tratamiento);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Recogemos los datos de la BD
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference refTratamiento = database.getReference("Tratamiento");

        //Recogemos las imagenes del Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://centro-estetico.appspot.com/");

        lista = (ListView) findViewById(R.id.listaBorrarTratamiento);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //Borramos la el tratamiento de  la abse de datos
                TextView nombreView = (TextView) view.findViewById(android.R.id.text1);
                final String nombre = nombreView.getText().toString();

                //Creamos un dialogo de aviso al borrar
                AlertDialog.Builder vent = new AlertDialog.Builder(BorrarTratamientoActivity.this);
                vent.setTitle("Borrar \"" + nombre + "\"");
                vent.setMessage("¿Borrar tratamiento de forma definitiva?");
                vent.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query queryRef = database.getReference().child("Tratamiento").orderByChild("nombre").equalTo(nombre);
                        queryRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                snapshot.getRef().setValue(null);
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        lista.deferNotifyDataSetChanged();
                    }
                });
                vent.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                vent.show();
            }
        });

        //Recogemos la lista de citas
        refTratamiento.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Borramos la lista para que se carguen los datos nuevos
                listaTra.clear();
                listaImg.clear();
                for (DataSnapshot tratamientoSnapshot : snapshot.getChildren()) {
                    String nombre = tratamientoSnapshot.child("nombre").getValue().toString();
                    String img = tratamientoSnapshot.child("imagen").getValue().toString();
                    listaTra.add(nombre);
                    listaImg.add(img);
                }
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(BorrarTratamientoActivity.this, android.R.layout.simple_list_item_1, listaTra);
                lista.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("La lectura falló: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
