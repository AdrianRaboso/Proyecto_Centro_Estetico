package com.example.adrin.proyecto_centro_estetico;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.adrin.proyecto_centro_estetico.model.Tratamiento;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CrearTratamientoActivity extends AppCompatActivity {

    private Button imagen, confirmar, cancelar;
    private EditText nombre, descripcion, esteticista, precio;
    private Spinner categoria;
    private final int RQS_PERMISSION_READ_EXTERNAL_STORAGE = 40;
    private final int SELECT_PICTURE = 42;
    private String nombreImagen;
    private Uri pathImagen;
    private final int DURACION = 1;
    private FirebaseDatabase database;
    private DatabaseReference refTratamiento;
    private boolean isEnabled = true;
    private Snackbar barraProgreso;
    private String id;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String imagenAntigua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tratamiento);

        //Colocamos la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crear_tratamiento);

        //Recogemos los datos de la BD
        database = FirebaseDatabase.getInstance();
        refTratamiento = database.getReference("Tratamiento");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://centro-estetico.appspot.com/");

        //Buscamos los componentes
        imagen = (Button) findViewById(R.id.btn_imagen_cancelar);
        confirmar = (Button) findViewById(R.id.btn_crear_aceptar);
        cancelar = (Button) findViewById(R.id.btn_crear_cancelar);
        nombre = (EditText) findViewById(R.id.nombre_tratamiento);
        descripcion = (EditText) findViewById(R.id.descripcion_nuevo_tratamiento);
        esteticista = (EditText) findViewById(R.id.esteticista_nuevo_tratamiento);
        precio = (EditText) findViewById(R.id.precio_nuevo_tratamiento);
        categoria = (Spinner) findViewById(R.id.spinner_categoria);

        //Si estamos modificando le ponemos los datos a los campos
        if (getIntent().getExtras().getBoolean("modificar")) {
            toolbar.setTitle("Modificar datos");
            nombre.setText(getIntent().getExtras().getString("nombre"));
            descripcion.setText(getIntent().getExtras().getString("descripcion"));
            esteticista.setText(getIntent().getExtras().getString("esteticista"));
            precio.setText(getIntent().getExtras().getString("precio"));
            id = getIntent().getExtras().getString("id");
            nombreImagen = getIntent().getExtras().getString("imagen");
            imagenAntigua = nombreImagen;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCamposRellenos()) {
                    double precioDouble = Double.parseDouble(precio.getText().toString());
                    Tratamiento nuevoTra = new Tratamiento(nombre.getText().toString(), categoria.getSelectedItem().toString(), precioDouble, esteticista.getText().toString(), DURACION, nombreImagen, descripcion.getText().toString());

                    if (imagen.getText().toString().startsWith("Seleccione")) {
                        refTratamiento.child(id).setValue(nuevoTra);
                        Snackbar.make(getCurrentFocus(), "¡Tratamiento modificado con éxito!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    } else {
                        if (getIntent().getExtras().getBoolean("modificar")) {
                            borrarImagen(imagenAntigua);
                            imagenAntigua = nombreImagen;
                        }
                        subirImagen(nuevoTra);
                        //Bloqueamos los componentes
                        isEnabled = false;
                        cancelar.setEnabled(isEnabled);
                        confirmar.setEnabled(isEnabled);
                        //Creamos la barra de carga
                        barraProgreso = Snackbar.make(getCurrentFocus(), R.string.txt_cargando, Snackbar.LENGTH_INDEFINITE);
                        ViewGroup contentLay = (ViewGroup) barraProgreso.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
                        ProgressBar item = new ProgressBar(getApplication());
                        contentLay.addView(item);
                        barraProgreso.show();
                    }
                } else {
                    Snackbar.make(getCurrentFocus(), "Por favor, rellene todos los campos", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaciarCampos();
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pide acceso a los permisos en tiempo de ejecucion si todavía no se les ha concedido a la aplicacion
                if (ActivityCompat.checkSelfPermission(CrearTratamientoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CrearTratamientoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RQS_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    //Abrimos el selector de imagen
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_PICTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //recogemos el valor que nos devuelve el intent
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data != null) {
                imagen.setText("Cambiar imagen...");
                //si la direccion no es nula extraemos la direccion uri
                pathImagen = data.getData();
                //Ponemos un nombre unico a la imagen
                Long tsLong = System.currentTimeMillis() / 1000;
                nombreImagen = "img_" + tsLong.toString();
            }
        }
    }

    private void subirImagen(final Tratamiento nuevoTra) {
        //Subimos la imagen al storage de firebase
        StorageReference imgTraRef = storageRef.child("imgTratamiento/" + nombreImagen);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pathImagen);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imgTraRef.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    isEnabled = true;
                    cancelar.setEnabled(isEnabled);
                    confirmar.setEnabled(isEnabled);
                    barraProgreso.dismiss();
                    Snackbar.make(getCurrentFocus(), "Ha habido un error al subir la imagen, inténtelo de nuevo", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    isEnabled = true;
                    cancelar.setEnabled(isEnabled);
                    confirmar.setEnabled(isEnabled);
                    barraProgreso.dismiss();
                    if (getIntent().getExtras().getBoolean("modificar")) {
                        refTratamiento.child(id).setValue(nuevoTra);
                        Snackbar.make(getCurrentFocus(), "¡Tratamiento modificado con éxito!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        refTratamiento.push().setValue(nuevoTra);
                        vaciarCampos();
                        Snackbar.make(getCurrentFocus(), "¡El nuevo tratamiento se ha subido con éxito!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void borrarImagen(String nombreImg) {
        //Borramos la imagen al storage de firebase
        StorageReference imgTraRef = storageRef.child("imgTratamiento/" + nombreImg);
        imgTraRef.delete();
    }

    private boolean isCamposRellenos() {
        try {
            return !nombre.getText().toString().equals("") && !descripcion.getText().toString().equals("") && !esteticista.getText().toString().equals("") && !precio.getText().toString().equals("") && !nombreImagen.equals("");
        } catch (NullPointerException ex) {
            return false;
        }
    }

    private void vaciarCampos() {
        nombre.setText("");
        descripcion.setText("");
        esteticista.setText("");
        precio.setText("");
        nombreImagen = "";
        imagen.setText("Seleccione una imagen...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && isEnabled) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isEnabled) {
            super.onBackPressed();
        }
    }
}
