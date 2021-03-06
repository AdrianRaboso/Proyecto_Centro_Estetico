package com.example.adrin.proyecto_centro_estetico;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroUser extends AppCompatActivity implements View.OnClickListener {

    EditText correo, confirmarCorreo, contraseña;
    Button registrar, cancelar;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    FirebaseUser user;
    String mail, confMail, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);

        correo = (EditText) this.findViewById(R.id.correo);
        confirmarCorreo = (EditText) this.findViewById(R.id.confirmarCorreo);
        contraseña = (EditText) this.findViewById(R.id.password);

        registrar = (Button) this.findViewById(R.id.botRegistrar);
        cancelar = (Button) this.findViewById(R.id.botCancelar);

        registrar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    enviarEmailVerificacion();
                }
            }
        };
    }

    private void registrarUsuario() {
        mail = correo.getText().toString().trim();
        confMail = confirmarCorreo.getText().toString().trim();
        password = contraseña.getText().toString().trim();

        if (mail.equals("") && confMail.equals("") && password.equals("")) {
            Toast.makeText(this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
        } else {
            if (mail.equals("")) {
                Toast.makeText(this, R.string.error_email_vacio, Toast.LENGTH_SHORT).show();
            } else {
                if (confMail.equals("")) {
                    Toast.makeText(this, R.string.error_confirmMail_vacio, Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals("")) {
                        Toast.makeText(this, R.string.error_contraseña_vacia, Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mail.equals(confMail)) {
                            confirmarCorreo.setText("");
                            Toast.makeText(this, R.string.coincidir_correo, Toast.LENGTH_SHORT).show();
                        } else if (!mail.equals("") && !confMail.equals("") && !password.equals("")) {
                            if (password.toString().length() < 6) {
                                contraseña.setText("");
                                Toast.makeText(this, R.string.error_contraseña_corta, Toast.LENGTH_SHORT).show();
                            } else {
                                //Se crea el registro en la BD
                                firebaseAuth.createUserWithEmailAndPassword(mail, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), R.string.error_cuenta_existente, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), R.string.registro_correcto, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }
            }
        }
    }

    private void enviarEmailVerificacion() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //despues de que el email se haya enviado se desconecta el usuario y le enviamos al login
                            FirebaseAuth.getInstance().signOut();
                            goLoginActivity();
                        } else {
                            //reiniciamos esta activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    }
                });

    }

    private void goLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == registrar.getId()) {
            registrarUsuario();
        } else if (v.getId() == cancelar.getId()) {
            goLoginActivity();
        }
    }
}
