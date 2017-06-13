package com.example.adrin.proyecto_centro_estetico;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        private Preference crear, modificar, borrar, verUsuarios, telefono, correo, crearOferta, modificarOferta, borrarOferta;
        private SwitchPreference mensajes;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.config_layout);

            crear = findPreference("add_tratamiento");
            modificar = findPreference("modify_tratamiento");
            borrar = findPreference("delete_tratamiento");
            crearOferta = findPreference("add_oferta");
            //modificarOferta = findPreference("modify_oferta");
            borrarOferta = findPreference("delete_oferta");
            verUsuarios = findPreference("ver_usuarios");
            mensajes = (SwitchPreference) findPreference("config_envia_mail");
            telefono = findPreference("cambiar_telefono");
            correo = findPreference("cambiar_correo");

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

            telefono.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder alertTelefono = new AlertDialog.Builder(getActivity());
                    alertTelefono.setTitle(R.string.title_dialog_telefono);

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_telefono, null);
                    alertTelefono.setView(layout);
                    final EditText mensaje = (EditText) layout.findViewById(R.id.cambiar_telefono);
                    mensaje.setInputType(InputType.TYPE_CLASS_PHONE);
                    mensaje.setText(Utils.TELEFONO);

                    alertTelefono.setPositiveButton(R.string.btn_resumen_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!mensaje.getText().toString().equals("") && mensaje.getText().toString().length() == 9) {
                                FirebaseDatabase database;
                                database = FirebaseDatabase.getInstance();
                                database.getReference("Propietario").child("telefono").setValue(mensaje.getText().toString().trim());
                            } else {
                                Toast.makeText(getActivity(), "El teléfono no es válido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertTelefono.setNegativeButton(R.string.cancelar, null);
                    alertTelefono.show();
                    return true;
                }
            });

            correo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder alertCorreo = new AlertDialog.Builder(getActivity());
                    alertCorreo.setTitle(R.string.title_dialog_nuevo_correo);

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_telefono, null);
                    alertCorreo.setView(layout);
                    final EditText mensaje = (EditText) layout.findViewById(R.id.cambiar_telefono);
                    mensaje.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    mensaje.setHint("Escriba el nuevo correo...");
                    mensaje.setText(Utils.CORREO);

                    alertCorreo.setPositiveButton(R.string.btn_resumen_aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isValidEmail(mensaje.getText().toString())) {
                                FirebaseDatabase database;
                                database = FirebaseDatabase.getInstance();
                                database.getReference("Propietario").child("correo").setValue(mensaje.getText().toString().trim());
                            } else {
                                Toast.makeText(getActivity(), "El correo no es válido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertCorreo.setNegativeButton(R.string.cancelar, null);
                    alertCorreo.show();
                    return true;
                }
            });

            crearOferta.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent crearOferta = new Intent(getActivity(), CrearOfertaActivity.class);
                    startActivity(crearOferta);
                    return true;
                }
            });

            borrarOferta.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent borrarOferta = new Intent(getActivity(), BorrarOfertaActivity.class);
                    startActivity(borrarOferta);
                    return true;
                }
            });
        }

        private boolean isValidEmail(String email) {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }
}
