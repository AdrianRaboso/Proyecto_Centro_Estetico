package com.example.adrin.proyecto_centro_estetico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getFragmentManager().beginTransaction().replace(R.id.content_layout, new PrefsFragment()).commit();

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_config);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PrefsFragment extends PreferenceFragment {
        private Preference crear, modificar, borrar, verUsuarios;
        private SwitchPreference mensajes;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.config_layout);

            crear = findPreference("add_tratamiento");
            modificar = findPreference("modify_tratamiento");
            borrar = findPreference("delete_tratamiento");
            verUsuarios = findPreference("ver_usuarios");
            mensajes = (SwitchPreference) findPreference("config_envia_mail");

            crear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent crear = new Intent(getActivity(), CrearTratamientoActivity.class);
                    crear.putExtra("modificar", false);
                    startActivity(crear);
                    return true;
                }
            });

            modificar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent modificar = new Intent(getActivity(), BorrarTratamientoActivity.class);
                    modificar.putExtra("modificar", true);
                    startActivity(modificar);
                    return false;
                }
            });

            borrar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent borrar = new Intent(getActivity(), BorrarTratamientoActivity.class);
                    borrar.putExtra("modificar", false);
                    startActivity(borrar);
                    return true;
                }
            });

            verUsuarios.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent ver = new Intent(getActivity(), VerUsuariosActivity.class);
                    startActivity(ver);
                    return true;
                }
            });

            mensajes.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    boolean isRecibirCorreos = (Boolean) o;
                    //Sacamos el propietario de la app
                    FirebaseDatabase database;
                    DatabaseReference refPropietario;

                    //Cambiamos
                    database = FirebaseDatabase.getInstance();
                    refPropietario = database.getReference("Propietario");
                    refPropietario.child("recibirCorreos").setValue(isRecibirCorreos);
                    return true;
                }
            });
        }
    }
}
