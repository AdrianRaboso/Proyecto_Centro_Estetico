package com.example.adrin.proyecto_centro_estetico;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrin.proyecto_centro_estetico.model.Oferta;
import com.example.adrin.proyecto_centro_estetico.model.Tratamiento;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class OfertasFragment extends Fragment {

    private FirebaseRecyclerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recycler;
    private DatabaseReference dbOfertas;
    private final String CARPETA_IMAGENES = "imgOferta";
    private final String SLASH = "/";
    private final String BASE_DATOS = "gs://centro-estetico.appspot.com/";

    private OnOfertasInteractionListener mListener;

    public OfertasFragment() {
        // Required empty public constructor
    }

    public static OfertasFragment newInstance() {
        return new OfertasFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ofertas, container, false);
        //Recogemos los datos de la BD
        dbOfertas = FirebaseDatabase.getInstance().getReference().child("Ofertas");

        recycler = (RecyclerView) view.findViewById(R.id.list_ofertas);
        recycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recycler.setLayoutManager(layoutManager);

        //Creamos una referencia de nuestro Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl(BASE_DATOS);

        mAdapter = new FirebaseRecyclerAdapter<Oferta, OfertasHolder>(
                Oferta.class, R.layout.fragment_ofertas_list_item, OfertasHolder.class, dbOfertas) {
            @Override
            public void populateViewHolder(final OfertasHolder ofertasViewHolder, Oferta oferta, int position) {
                ofertasViewHolder.setDescripcion(oferta.getDescripcion());
                ofertasViewHolder.setTema(oferta.getTema());

                /*StorageReference imgTratamientoRef = storageRef.child(CARPETA_IMAGENES + SLASH + oferta.getImagen());

                final long ONE_MEGABYTE = 1024 * 1024;
                imgTratamientoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        ofertasViewHolder.setImagen(getActivity(), bytes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });*/
            }
        };

        recycler.setAdapter(mAdapter);
        return view;
    }

    public void onButtonPressed(Oferta oferta) {
        if (mListener != null) {
            mListener.onOfertasInteraction(oferta);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOfertasInteractionListener) {
            mListener = (OnOfertasInteractionListener) context;
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

    public interface OnOfertasInteractionListener {
        void onOfertasInteraction(Oferta oferta);
    }
}
