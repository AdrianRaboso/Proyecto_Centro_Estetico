package com.example.adrin.proyecto_centro_estetico;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adrin.proyecto_centro_estetico.model.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VerUsuariosActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private RecyclerView recycler;
    Query usuariosOrdenados;

    private FirebaseDatabase database;
    private DatabaseReference refUsuarios;
    private FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ver_usuarios);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database = FirebaseDatabase.getInstance();
        refUsuarios = database.getReference("Usuarios");
        usuariosOrdenados = refUsuarios.orderByChild("Nombre");

        recycler = (RecyclerView) findViewById(R.id.list_usuarios);

        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        mAdapter = new FirebaseRecyclerAdapter<Usuario, UsuarioHolder>(
                Usuario.class, R.layout.fragment_citas_list_item, UsuarioHolder.class, usuariosOrdenados) {

            @Override
            public void populateViewHolder(UsuarioHolder usuarioViewHolder, final Usuario usuario, int position) {
                usuarioViewHolder.setNombre(usuario.getNombre());
                usuarioViewHolder.setCorreo(usuario.getCorreo());
                usuarioViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crearDialogoOpciones(usuario.getCorreo(), usuario.getTelefono());
                    }
                });
            }
        };
        recycler.setAdapter(mAdapter);
    }

    private void crearDialogoOpciones(final String correoTo, final String telefonoTo) {
        AlertDialog.Builder alertMostrarOpciones = new AlertDialog.Builder(this);
        alertMostrarOpciones.setTitle(R.string.title_dialog_opciones);
        alertMostrarOpciones.setMessage(R.string.message_dialog_opciones);

        alertMostrarOpciones.setPositiveButton(R.string.llamar_usuario, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hacerLlamada(telefonoTo);
            }
        });
        alertMostrarOpciones.setNegativeButton(R.string.correo_usuario, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent correo = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", correoTo, null));
                startActivity(correo);
                //crearDialogoMensaje(correoTo);
            }
        });
        alertMostrarOpciones.show();
    }

    private void crearDialogoMensaje(final String correoTo) {
        AlertDialog.Builder alertMensaje = new AlertDialog.Builder(VerUsuariosActivity.this);
        alertMensaje.setTitle(R.string.title_dialog_mensaje);

        LayoutInflater inflater = VerUsuariosActivity.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_escribir_correo, null);
        alertMensaje.setView(layout);
        final EditText mensaje = (EditText) layout.findViewById(R.id.enviar_correo);

        alertMensaje.setPositiveButton(R.string.btn_resumen_aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!mensaje.getText().toString().equals("")) {
                    //Enviamos un mensaje del centro de belleza
                    String emailSubject = "Mensaje privado de " + Utils.PROPIETARIO;
                    String emailBody = mensaje.getText().toString();
                    Utils.crearMensajeUsuario(emailSubject, emailBody, correoTo);
                }
            }
        });
        alertMensaje.setNegativeButton(R.string.cancelar, null);
        alertMensaje.show();
    }

    private void hacerLlamada(String telefonoTo) {
        if (!telefonoTo.equals("")) {
            //Pide acceso a los permisos en tiempo de ejecucion si todavía no se les ha concedido a la aplicacion
            if (ActivityCompat.checkSelfPermission(VerUsuariosActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(VerUsuariosActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 12);
            } else {
                Intent llamada = new Intent(Intent.ACTION_CALL);
                llamada.setData(Uri.parse("tel:+34" + telefonoTo));
                startActivity(llamada);
            }
        } else {
            Snackbar.make(getCurrentFocus(), "El usuario no tiene número de teléfono", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
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
