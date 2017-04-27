package com.example.adrin.proyecto_centro_estetico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    List<String> grupoTratamiento;
    List<String> childList;
    Map<String, List<String>> tratamientoCollection;
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.laptop_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, grupoTratamiento, tratamientoCollection);
        expListView.setAdapter(expListAdapter);

        setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();
                return true;
            }
        });
    }

    private void createGroupList() {
        grupoTratamiento = new ArrayList<>();
        grupoTratamiento.add("Cuidados faciales");
        grupoTratamiento.add("Cuidados corporales");
        grupoTratamiento.add("Maquillaje");
        grupoTratamiento.add("Manos y pies");
        grupoTratamiento.add("Pestañas");
        grupoTratamiento.add("Depilación");
    }

    private void createCollection() {
        // preparing laptops collection(child)
        String[] faciales = {"Higiene facial", "Higiene facial y electro estimulación",
                "Vitaminas", "Oxigenación", "Maderoterapia Ant-Aging"};
        String[] corporales = {"Maderoterapia", "Masaje de sales", "Tratamiento sudación", "Espalda relajación", "Masaje drenaje linfático"};
        String[] maquillaje = {"Novias con prueba", "Eventos", "Express",
                "Automaquillaje"};
        String[] manosPies = {"Esmaltado semipermanente", "Pedicura Spa"};
        String[] pestanas = {"Permanente y tinte", "Extensiones pestañas"};
        String[] depilacion = {"Labio superior", "Cejas con pizas", "Diseño de cejas con pinza", "Brazos", "Íngles", "Axilas", "Medias piernas", "Piernas enteras", "Piernas enteras"};

        tratamientoCollection = new LinkedHashMap<>();

        for (String tratamiento : grupoTratamiento) {
            if (tratamiento.equals("Cuidados faciales")) {
                loadChild(faciales);
            } else if (tratamiento.equals("Cuidados corporales"))
                loadChild(corporales);
            else if (tratamiento.equals("Maquillaje"))
                loadChild(maquillaje);
            else if (tratamiento.equals("Manos y pies"))
                loadChild(manosPies);
            else if (tratamiento.equals("Pestañas"))
                loadChild(pestanas);
            else
                loadChild(depilacion);

            tratamientoCollection.put(tratamiento, childList);
        }
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<>();
        for (String model : laptopModels)
            childList.add(model);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
