package com.example.adrin.proyecto_centro_estetico;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adrin.proyecto_centro_estetico.model.Tratamiento;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseRecyclerAdapter mAdapter;

    private OnFragmentTratamientoListener mListener;

    public InicioFragment() {
        // Required empty public constructor
    }

    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_tratamiento, container, false);
        DatabaseReference dbTratamiento =
                FirebaseDatabase.getInstance().getReference()
                        .child("Tratamiento");

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.list_tratamiento);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FirebaseRecyclerAdapter<Tratamiento, TratamientoHolder>(
                Tratamiento.class, R.layout.fragment_inicio_list_item, TratamientoHolder.class, dbTratamiento) {

            @Override
            public void populateViewHolder(TratamientoHolder tratamientoViewHolder, Tratamiento tratamiento, int position) {
                tratamientoViewHolder.setCategoria(tratamiento.getCategoria());
                tratamientoViewHolder.setNombre(tratamiento.getNombre());
            }
        };

        recycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    public void onButtonPressed(Tratamiento tratamiento) {
        if (mListener != null) {
            mListener.onFragmentTratamientoListener(tratamiento);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentTratamientoListener) {
            mListener = (OnFragmentTratamientoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentTratamientoListener {
        void onFragmentTratamientoListener(Tratamiento tratamiento);
    }
}
