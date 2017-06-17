package com.example.adrin.proyecto_centro_estetico;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.example.adrin.proyecto_centro_estetico.model.Cita;
import com.example.adrin.proyecto_centro_estetico.model.Oferta;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class InicioActivity extends AppCompatActivity implements CitasFragment.OnFragmentCitasListener, InicioFragment.OnInicioListener, OfertasFragment.OnOfertasInteractionListener, GoogleApiClient.OnConnectionFailedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FirebaseAuth firebaseAuth;
    private ViewPager mViewPager;
    private GoogleApiClient googleApiClient;
    private FloatingActionButton fab;
    private String user;
    private boolean isPrimerInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Si no tiene usuario creado se lo creamos al isPrimerInicio
        Utils.crearResgistroAutomaticoUsuario();

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Crea el adaptador que devuelve las 3 paginas
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cita = new Intent(InicioActivity.this, PedirCitaActivity.class);
                startActivity(cita);
            }
        });

        //Creamos el objeto de autentificacion
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void cargarConfiguracion() {
        SharedPreferences preferences = getSharedPreferences("inicioDatosUsuario", Context.MODE_PRIVATE);
        isPrimerInicio = preferences.getBoolean("confDatosUsuario", true);
        if (isPrimerInicio) {
            Intent i = new Intent(this, DatosUsuarioActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Sacamos al propietario y al usuario de la app
        user = Utils.currentUser();

        if (Utils.PROPIETARIO.equals(user)) {
            menu.setGroupVisible(R.id.menu_config_group, true);
        } else {
            menu.setGroupVisible(R.id.menu_config_group, false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cerrar_sesion) {
            firebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        goLoginScreen();
                    } else {
                        Toast.makeText(getApplicationContext(), "No se pudo cerrar sesion", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            finish();
            return true;
        } else if (id == R.id.action_llamar && Utils.PROPIETARIO.equals(Utils.currentUser())) {
            //Pide acceso a los permisos en tiempo de ejecucion si todavía no se les ha concedido a la aplicacion
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 12);
                Intent llamada = new Intent(Intent.ACTION_CALL);
                llamada.setData(Uri.parse("tel:+34" + Utils.TELEFONO));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(llamada);
                    finish();
                }
            } else {
                Snackbar.make(getCurrentFocus(), "La aplicación no tiene permisos para hacer llamadas", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        } else if (id == R.id.action_preferencias) {
            //Accedemos a las preferencias
            Intent config = new Intent(InicioActivity.this, DatosUsuarioActivity.class);
            startActivity(config);
        } else if (id == R.id.action_config) {
            //Accedemos a las preferencias de propietario
            Intent config = new Intent(InicioActivity.this, ConfigActivity.class);
            startActivity(config);
        }
        return super.onOptionsItemSelected(item);
    }

    private void goLoginScreen() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onFragmentCitasListener(Cita cita) {

    }

    @Override
    public void onInicioListener(String nombreTratamiento) {
        Intent cita = new Intent(InicioActivity.this, PedirCitaActivity.class);
        cita.putExtra("nombreTratamiento", nombreTratamiento);
        startActivity(cita);
    }

    @Override
    public void onOfertasInteraction(Oferta oferta) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return InicioFragment.newInstance();
                case 1:
                    return CitasFragment.newInstance();
                case 2:
                    return OfertasFragment.newInstance();
                default:
                    return InicioFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TRATAMIENTOS";
                case 1:
                    return "TUS CITAS";
                case 2:
                    return "OFERTAS";
            }
            return null;
        }
    }
}
