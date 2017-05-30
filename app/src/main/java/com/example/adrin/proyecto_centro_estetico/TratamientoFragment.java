package com.example.adrin.proyecto_centro_estetico;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.adrin.proyecto_centro_estetico.model.Tratamiento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TratamientoFragment extends Fragment {

    private List<String> grupoTratamiento;
    private List<String> childList;
    private Map<String, List<String>> tratamientoCollection;
    private ExpandableListView expListView;
    private TratamientosListener listener;
    private ExpandableListAdapter expListAdapter;
    private FirebaseDatabase database;
    private DatabaseReference refTratamiento;

    public TratamientoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tratamiento, container, false);

        //Generamos la lista de tratamientos
        cargarListaTratamientos();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializamos la coleccion de tratamientos
        expListView = (ExpandableListView) getView().findViewById(R.id.listaTratamientos);

        //Listener de la lista desplegable
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (listener != null) {
                    listener.onTratamientoSeleccionado((String) expListAdapter.getChild(groupPosition, childPosition));
                }
                return false;
            }
        });
        //Habilitamos el scroll del listview
        expListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
    }

    public interface TratamientosListener {
        void onTratamientoSeleccionado(String c);
    }

    public void setTratamientoListener(TratamientosListener listener) {
        this.listener = listener;
    }

    public void cargarListaTratamientos() {
        //Recogemos los datos de la BD
        database = FirebaseDatabase.getInstance();
        refTratamiento = database.getReference("Tratamiento");
        //Recogemos la lista de citas
        refTratamiento.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Borramos la lista para que se carguen los datos nuevos
                Tratamiento.listaTratamientos.clear();
                for (DataSnapshot tratamientoSnapshot : snapshot.getChildren()) {
                    String categoria = tratamientoSnapshot.child("categoria").getValue().toString();
                    String nombre = tratamientoSnapshot.child("nombre").getValue().toString();
                    String duracionString = tratamientoSnapshot.child("duracion").getValue().toString();
                    String precioString = tratamientoSnapshot.child("precio").getValue().toString();
                    String esteticista = tratamientoSnapshot.child("esteticista").getValue().toString();
                    double precio = Double.parseDouble(precioString);
                    int duracion = Integer.parseInt(duracionString);
                    String id = tratamientoSnapshot.getKey();

                    Tratamiento cita = new Tratamiento(id, nombre, categoria, precio, esteticista, duracion);
                    Tratamiento.listaTratamientos.add(cita);
                }
                //Despues de recoger los datos los añadimos al adaptador
                rellenarGruposYColleccion();
                expListAdapter = new ExpandableListAdapter(getActivity(), grupoTratamiento, tratamientoCollection);
                expListView.setAdapter(expListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("La lectura falló: " + databaseError.getMessage());
            }
        });
    }

    private void rellenarGruposYColleccion() {
        grupoTratamiento = new ArrayList<>();
        tratamientoCollection = new LinkedHashMap<>();
        String cat = "";
        //Añadimos los grupos
        for (Tratamiento tra : Tratamiento.listaTratamientos) {
            if (!cat.equals(tra.getCategoria())) {
                grupoTratamiento.add(tra.getCategoria());
                cat = tra.getCategoria();
            }
        }
        //Añadimos los hijos de cada grupo
        for (String tratamiento : grupoTratamiento) {
            if (tratamiento.equals("Cuidados faciales")) {
                cargarHijos("Cuidados faciales");
            } else if (tratamiento.equals("Cuidados corporales")) {
                cargarHijos("Cuidados corporales");
            } else if (tratamiento.equals("Maquillaje")) {
                cargarHijos("Maquillaje");
            } else if (tratamiento.equals("Manos y pies")) {
                cargarHijos("Manos y pies");
            } else if (tratamiento.equals("Pestañas")) {
                cargarHijos("Pestañas");
            } else {
                cargarHijos("Depilación");
            }
            tratamientoCollection.put(tratamiento, childList);
        }
    }

    private void cargarHijos(String tipoTratamiento) {
        childList = new ArrayList<>();
        for (Tratamiento tra : Tratamiento.listaTratamientos) {
            if (tra.getCategoria().equals(tipoTratamiento)) {
                childList.add(tra.getNombre());
            }
        }
    }

    /*private void createGroupList() {
        grupoTratamiento = new ArrayList<>();
        grupoTratamiento.add("Cuidados faciales");
        grupoTratamiento.add("Cuidados corporales");
        grupoTratamiento.add("Maquillaje");
        grupoTratamiento.add("Manos y pies");
        grupoTratamiento.add("Pestañas");
        grupoTratamiento.add("Depilación");
    }

    public void createCollection() {
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
            } else if (tratamiento.equals("Cuidados corporales")) {
                loadChild(corporales);
            } else if (tratamiento.equals("Maquillaje")) {
                loadChild(maquillaje);
            } else if (tratamiento.equals("Manos y pies")) {
                loadChild(manosPies);
            } else if (tratamiento.equals("Pestañas")) {
                loadChild(pestanas);
            } else {
                loadChild(depilacion);
            }
            tratamientoCollection.put(tratamiento, childList);
        }

    }

    private void crearTratamietnos(String categoria, String[] hijos) {
        DatabaseReference dbTratamiento = FirebaseDatabase.getInstance().getReference().child("Tratamiento");
        Tratamiento tra = new Tratamiento();
        for (int i = 0; i < hijos.length; i++) {
            tra.setCategoria(categoria);
            tra.setNombre(hijos[i]);
            tra.setDuracion(1);
            tra.setEsteticista("Laura");
            tra.setPrecio(15.50);
            dbTratamiento.push().setValue(tra);
            //Tratamiento.listaTratamientos.add(tra);
        }

    }


    private void loadChild(String[] tipoTratamiento) {
        childList = new ArrayList<>();
        for (String model : tipoTratamiento) {
            childList.add(model);
        }
    }*/
}
