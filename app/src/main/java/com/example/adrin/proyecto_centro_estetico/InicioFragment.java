package com.example.adrin.proyecto_centro_estetico;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrin.proyecto_centro_estetico.model.Tratamiento;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class InicioFragment extends Fragment {

    private FirebaseRecyclerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recycler;
    private DatabaseReference dbTratamiento;

    private final String CARPETA_IMAGENES = "imgTratamiento";
    private final String SLASH = "/";

    private OnInicioListener mListener;

    public InicioFragment() {
        // Required empty public constructor
    }

    public static InicioFragment newInstance() {
        InicioFragment fragment = new InicioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_tratamiento, container, false);
        //Recogemos los datos de la BD
        dbTratamiento = FirebaseDatabase.getInstance().getReference().child("Tratamiento");

        recycler = (RecyclerView) view.findViewById(R.id.list_tratamiento);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);

        //Creamos una referencia de nuestro Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://centro-estetico.appspot.com/");

        mAdapter = new FirebaseRecyclerAdapter<Tratamiento, TratamientoHolder>(
                Tratamiento.class, R.layout.fragment_inicio_list_item, TratamientoHolder.class, dbTratamiento) {
            @Override
            public void populateViewHolder(final TratamientoHolder tratamientoViewHolder, Tratamiento tratamiento, int position) {
                tratamientoViewHolder.setNombre(tratamiento.getNombre());
                tratamientoViewHolder.setPrecio(String.valueOf(tratamiento.getPrecio()) + " €");
                tratamientoViewHolder.setEsteticista("Esteticista: " + tratamiento.getEsteticista());
                tratamientoViewHolder.setDuracion(String.valueOf(tratamiento.getDuracion()) + " hora");
                tratamientoViewHolder.infoClick(tratamiento.getDescripcion());
                tratamientoViewHolder.citaClick(mListener, tratamiento.getNombre());

                StorageReference imgTratamientoRef = storageRef.child(CARPETA_IMAGENES + SLASH + tratamiento.getImagen());

                final long ONE_MEGABYTE = 2048 * 2048;
                imgTratamientoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        tratamientoViewHolder.setImagen(getActivity(), bytes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        };

        //Le añadimos una separacion entre items
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), layoutManager.getOrientation());
        //recycler.addItemDecoration(dividerItemDecoration);

        recycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInicioListener) {
            mListener = (OnInicioListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOfertasInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnInicioListener {
        void onInicioListener(String nombreTratamiento);
    }
}
