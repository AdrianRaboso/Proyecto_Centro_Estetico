package com.example.adrin.proyecto_centro_estetico;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatosUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nomAp, dni, telf, mail;
    CheckBox enferm, dispMedic, alergia, medicamento;
    ImageButton botCancel, botSend;
    String id;
    boolean isModificado = false;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cuenta_usuario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nomAp = (EditText) this.findViewById(R.id.nom);
        dni = (EditText) this.findViewById(R.id.dni);
        telf = (EditText) this.findViewById(R.id.telefono);
        mail = (EditText) this.findViewById(R.id.correo);

        enferm = (CheckBox) this.findViewById(R.id.enfermedades);
        dispMedic = (CheckBox) this.findViewById(R.id.disMedicos);
        alergia = (CheckBox) this.findViewById(R.id.alergias);
        medicamento = (CheckBox) this.findViewById(R.id.medicamentos);

        botCancel = (ImageButton) this.findViewById(R.id.botCancelar);
        botSend = (ImageButton) this.findViewById(R.id.botEnviar);

        botCancel.setOnClickListener(this);
        botSend.setOnClickListener(this);

        obtenerUsuarioActivo();
    }

    //obtengo el usuario activo actual
    private void obtenerUsuarioActivo() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    cargarDatosPorDefecto();
                }
            }
        };
    }

    //metodo para cambiar la contraseñ
    private void cambiarPassword() {
        user = firebaseAuth.getInstance().getCurrentUser();
        String correo = mail.getText().toString();
        if (!correo.equals("")) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.sendPasswordResetEmail(correo);
            Toast.makeText(getApplicationContext(), R.string.aviso_correo_cambioPsw, Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_email_vacio, Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para cambiar el correo con un alerti dialog
    private void cambiarCorreo() {
        user = firebaseAuth.getInstance().getCurrentUser();//obtenemos el usuario
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_nuevo_correo);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_cambio, null);
        final EditText email = (EditText) view.findViewById(R.id.correoNuevo);
        builder.setView(view)
                .setPositiveButton(R.string.btn_resumen_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nuevoMail = email.getText().toString();
                        if (user != null) {
                            user.updateEmail(nuevoMail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), R.string.cambio_correo_usuario, Toast.LENGTH_LONG).show();
                                                //dejamos 3 segundos antes de cerrar sesión
                                                try {
                                                    Thread.sleep(3000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                firebaseAuth.signOut();
                                                goLoginActivity();
                                            } else {
                                                Toast.makeText(getApplicationContext(), R.string.error_cambiar_correo, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        email.addTextChangedListener(new TextWatcher() {
            private void handleText() {
                final Button botAceptar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (email.getText().length() == 0) {
                    botAceptar.setEnabled(false);
                } else {
                    botAceptar.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handleText();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                handleText();
            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    private void goLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void cargarDatosPorDefecto() {
        id = user.getUid();//obtengo el id del usuario
        String correo = user.getEmail().toString().trim();
        mail.setText(correo);
        ref.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nomAp.setText(dataSnapshot.child("Nombre").getValue().toString());
                dni.setText(dataSnapshot.child("DNI").getValue().toString());
                telf.setText(dataSnapshot.child("Telefono").getValue().toString());
                alergia.setChecked(Boolean.parseBoolean(dataSnapshot.child("Alergias").getValue().toString()));
                dispMedic.setChecked(Boolean.parseBoolean(dataSnapshot.child("Disp_medicos").getValue().toString()));
                enferm.setChecked(Boolean.parseBoolean(dataSnapshot.child("Enfermedades").getValue().toString()));
                medicamento.setChecked(Boolean.parseBoolean(dataSnapshot.child("Medicamentos").getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void guardarDatos() {
        String nombre = nomAp.getText().toString().trim();
        String DNI = dni.getText().toString().trim();
        String telefono = telf.getText().toString().trim();
        String correo = mail.getText().toString().trim();

        //Estructura: Usuarios.id.Campo:valor
        if (nombre.equals("") || DNI.equals("") || telefono.equals("") || correo.equals("")) {
            Toast.makeText(this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
        } else {
            isModificado = true;
            if (nombre.equals("")) {
                nomAp.setError("Campo vacio");
            } else {
                ref.child("Usuarios").child(id).child("Nombre").setValue(nombre);//guardo el nombre
            }

            if (DNI.equals("")) {
                dni.setError("Campo vacio");
            } else {
                if (dni.getText().length() == 9 && isValidDni(DNI)) {
                    ref.child("Usuarios").child(id).child("DNI").setValue(DNI);//guardo el dni
                } else {
                    dni.setError("DNI incorrecto");
                }
            }

            if (telf.equals("")) {
                telf.setError("Campo vacío");
            } else {
                if (telf.getText().length() == 9) {
                    ref.child("Usuarios").child(id).child("Telefono").setValue(telefono);//guardo el telefono
                } else {
                    telf.setError("Teléfono incorrecto");
                }
            }

            if (correo.equals("")) {
                mail.setError("Campo vacio");
            } else {
                if (isValidEmail(correo)) {
                    ref.child("Usuarios").child(id).child("Correo").setValue(correo);//guardo el correo
                } else {
                    mail.setError("Patrón incorrecto");
                }
            }

            //guardo si tiene enfermedades o no
            if (enferm.isChecked()) {
                ref.child("Usuarios").child(id).child("Enfermedades").setValue(true);
            } else {
                ref.child("Usuarios").child(id).child("Enfermedades").setValue(false);
            }

            //guardo si tiene dispositivos medicos o no
            if (dispMedic.isChecked()) {
                ref.child("Usuarios").child(id).child("Disp_medicos").setValue(true);
            } else {
                ref.child("Usuarios").child(id).child("Disp_medicos").setValue(false);
            }

            //guardo si tiene alguna alergia o no
            if (alergia.isChecked()) {
                ref.child("Usuarios").child(id).child("Alergias").setValue(true);
            } else {
                ref.child("Usuarios").child(id).child("Alergias").setValue(false);
            }

            //guardo si tiene medicamentos o no
            if (medicamento.isChecked()) {
                ref.child("Usuarios").child(id).child("Medicamentos").setValue(true);
            } else {
                ref.child("Usuarios").child(id).child("Medicamentos").setValue(false);
            }
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidDni(String dni) {
        String DNI_PATTERN = "[0-9]{7,8}[A-Z a-z]";
        Pattern pattern = Pattern.compile(DNI_PATTERN);
        Matcher matcher = pattern.matcher(dni);
        return matcher.matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CambiarContraseña:
                cambiarPassword();
                return true;
            case R.id.CambiarCorreo:
                cambiarCorreo();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == botCancel.getId()) {
            finish();
        } else if (view.getId() == botSend.getId()) {
            guardarDatos();
            if (isModificado) {
                Snackbar.make(getCurrentFocus(), "Su usuario ha sido modificado", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }
    }
}
