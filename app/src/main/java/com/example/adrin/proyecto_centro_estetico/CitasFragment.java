package com.example.adrin.proyecto_centro_estetico;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adrin.proyecto_centro_estetico.model.Cita;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CitasFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseDatabase database;
    private DatabaseReference refCitas;

    private OnFragmentCitasListener mListener;

    public CitasFragment() {
        // Required empty public constructor
    }

    public static CitasFragment newInstance(String param1, String param2) {
        CitasFragment fragment = new CitasFragment();
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
        View view = inflater.inflate(R.layout.fragment_citas, container, false);
        //Llamamos al listview del fragmento
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        TextView vista = (TextView) view.findViewById(android.R.id.empty);
        listView.setEmptyView(vista);

        database = FirebaseDatabase.getInstance();
        refCitas = database.getReference("Citas");

        ListAdapter firebaseCitasAdapter = new FirebaseListAdapter<Cita>(getActivity(), Cita.class, R.layout.fragment_citas_list_item, refCitas) {
            @Override
            protected void populateView(View view, Cita cita, int position) {
                ((TextView) view.findViewById(R.id.textTratamiento)).setText(cita.getTratamiento());
                ((TextView) view.findViewById(R.id.textFecha)).setText(cita.getFecha() + " a las " + cita.getHora() + ":00");
            }
        };
        //Asignamos el listadapter
        listView.setAdapter(firebaseCitasAdapter);

        //Damos funcioalidad al listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCitasListener) {
            mListener = (OnFragmentCitasListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCitasListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentCitasListener {
        void onFragmentInteraction(Uri uri);
    }
}
