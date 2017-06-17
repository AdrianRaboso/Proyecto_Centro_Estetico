package com.example.adrin.proyecto_centro_estetico;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private EditText mail, password;
    private Button login, signin, botPwd;
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressDialog progressDialog;
    public static final int SIGNIN_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarGoogleAppi();

        ponerEscuchaBotGoogle();

        iniciarFirebaseAuth();

        llamarElementosVista();

        ponerEscuchaBotones();

    }

    private void ponerEscuchaBotGoogle() {
        signInButton = (SignInButton) this.findViewById(R.id.botGoogle);
        signInButton.setOnClickListener(this);
    }

    private void llamarElementosVista() {
        //Editext de correo y contraseña
        mail = (EditText) this.findViewById(R.id.emailText);
        password = (EditText) this.findViewById(R.id.passwordText);

        //botones
        login = (Button) this.findViewById(R.id.botRegistrar);
        signin = (Button) this.findViewById(R.id.registrar);
        botPwd = (Button) this.findViewById(R.id.olvidarPwd);

        //ProgressDialog
        progressDialog = new ProgressDialog(this);
    }

    private void ponerEscuchaBotones() {
        login.setOnClickListener(this);
        signin.setOnClickListener(this);
        botPwd.setOnClickListener(this);
    }

    private void inicializarGoogleAppi() {
        //objeto de opciones que dira como autenticarnos
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))//para obtener un token
                .requestEmail()//para obtener el correo del usuario autenticado
                .build();
        //inicializamos googlApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)//gestiona automaticamente el ciclo de vida de la api con el del activity
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)//llamamos a la aplicacion
                .build();
    }

    private void iniciarFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };
    }

    private void userLogin() {
        String email = mail.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Por favor ingrese el correo y la contraseña", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor ingrese el correo", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Por favor ingrese la contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                        } else {
                            comprobarEmailVerificado();
                            Toast.makeText(getApplicationContext(),"Error de autenticación",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private void comprobarEmailVerificado() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                goMainScreen();
            } else {
                FirebaseAuth.getInstance().signOut();
            }
        }else{
            Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        Intent i = new Intent(this, InicioActivity.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);//flags para que una activity no se quede como anterior de otra
        startActivity(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void recuperarContraseña() {
        String correo = mail.getText().toString();

        if (!correo.equals("")) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.sendPasswordResetEmail(correo);
            Toast.makeText(getApplicationContext(), "Recibirá un correo con los pasos a seguir.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Por favor ingrese el correo.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == signInButton.getId()) {
            //Abrimos el inicio de sesion para una cuenta google
            Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(i, SIGNIN_CODE);//lo iniciamos esperando un resultado
        } else if (v.getId() == login.getId()) {
            userLogin();
        } else if (v.getId() == signin.getId()) {
            startActivity(new Intent(this, RegistroUser.class));
        } else if (v.getId() == botPwd.getId()) {
            recuperarContraseña();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNIN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);//obtenemos un objeto resultado
            handleSigninResult(result);//enviamos el objeto a otro metodo
        }
    }

    private void handleSigninResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //goMainScreen();//Abrimos el activity principal donde mostraremos los datos
            firebaseAuthWithGoogle(result.getSignInAccount());//le mandamos la cuenta
        } else {
            Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        //creamos la credencial que contendra el token de la cuenta
        AuthCredential credencial = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

        //añadimos otro oyente que nos dira cuando esto termine
        firebaseAuth.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "No se pudo autenticar con Firebase", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
}
