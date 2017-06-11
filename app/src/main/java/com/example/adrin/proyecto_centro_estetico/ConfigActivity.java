package com.example.adrin.proyecto_centro_estetico;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        // Display the fragment as the main content.
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

        private Preference crear, modificar, borrar;
        //private EditTextPreference borrar;
        private SharedPreferences.OnSharedPreferenceChangeListener listener;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.config_layout);

            crear = findPreference("add_tratamiento");
            modificar = findPreference("modify_tratamiento");
            borrar = findPreference("delete_tratamiento");
            //borrar.setDialogTitle("Borrar tratamiento");
            //borrar.setDialogMessage("Escriba el nombre del tratamiento que quiera borrar: ");

            crear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent crear = new Intent(getActivity(), CrearTratamientoActivity.class);
                    startActivity(crear);
                    return true;
                }
            });

            modificar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), "m", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            borrar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent borrar = new Intent(getActivity(), BorrarTratamientoActivity.class);
                    startActivity(borrar);
                    return true;
                }
            });
        }

        /*private void createListener() {
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    final String value = sharedPreferences.getString("delete_tratamiento", "NULL");
                    if (!value.equals("NULL")) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference refTratamiento = database.getReference("Tratamiento");

                        refTratamiento.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Si existe el valor lo borramos
                                if (dataSnapshot.child("nmbre").equals(value)) {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            };
            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(listener);
        }*/
    }
}
