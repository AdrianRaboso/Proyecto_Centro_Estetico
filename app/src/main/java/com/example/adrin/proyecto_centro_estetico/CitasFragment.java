package com.example.adrin.proyecto_centro_estetico;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.adrin.proyecto_centro_estetico.model.Cita;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.Date;

public class CitasFragment extends Fragment {

    private ListAdapter firebaseCitasAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recycler;
    private TextView empty;

    private FirebaseDatabase database;
    private DatabaseReference refCitas;
    private FirebaseRecyclerAdapter mAdapter;

    private OnFragmentCitasListener mListener;

    public CitasFragment() {
        // Required empty public constructor
    }

    public static CitasFragment newInstance() {
        CitasFragment fragment = new CitasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_citas, container, false);
        database = FirebaseDatabase.getInstance();
        refCitas = database.getReference("Citas");
        final Query citasOrdenadas = refCitas.orderByChild("cod_cliente").equalTo(Utils.currentUser());

        recycler = (RecyclerView) view.findViewById(R.id.list_citas);
        empty = (TextView) view.findViewById(android.R.id.empty);
        empty = (TextView) view.findViewById(R.id.empty_view);

        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);

        mAdapter = new FirebaseRecyclerAdapter<Cita, CitaHolder>(
                Cita.class, R.layout.fragment_citas_list_item, CitaHolder.class, citasOrdenadas) {

            @Override
            public void populateViewHolder(CitaHolder citaViewHolder, final Cita cita, int position) {
                //Sacamos la fecha de la cita y la convertimos al formato Date
                String fecha[] = cita.getFecha().split("/");
                int anio = Integer.parseInt(fecha[0]);
                int mes = Integer.parseInt(fecha[1]);
                int dia = Integer.parseInt(fecha[2]);
                Calendar calFecha = Calendar.getInstance();
                calFecha.set(anio, mes, dia);

                //Sacamos la referencia del objeto en la base de datos
                final DatabaseReference refCita = getRef(position);

                if (calFecha.getTime().after(new Date())) {
                    //Seteamos la fecha y el nombre
                    citaViewHolder.setTratamiento(cita.getTratamiento());
                    citaViewHolder.setFecha(cita.getFecha() + " a las " + cita.getHora() + ":00");
                    //Añadimos un listener a cada elemento
                    citaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Preguntamos si desea borrar el elemento de la lista
                            crearDialogoBorrar(refCita, cita);
                        }
                    });
                } else {
                    //Borramos de la base de datos las citas que sean mayores que la fecha actual
                    refCita.removeValue();
                }
            }
        };

        recycler.setAdapter(mAdapter);
        return view;
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
        void onFragmentCitasListener(Cita cita);
    }

    public void crearDialogoBorrar(final DatabaseReference ref, final Cita cita) {
        AlertDialog.Builder mensajeCambiarClave = new AlertDialog.Builder(getActivity());
        mensajeCambiarClave.setTitle(R.string.title_dialog_borrar);
        mensajeCambiarClave.setMessage(R.string.message_dialog_borrar);
        mensajeCambiarClave.setPositiveButton(R.string.borrar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ref.removeValue();
                //Enviamos un mensaje al centro de belleza
                if (Utils.IS_ENVIAR) {
                    String emailSubject = "Cita cancelada";
                    String emailBody = "Cita del usuario " + Utils.currentUser() + " con fecha " + cita.getFecha() + " y hora " + cita.getHora() + ", para el tratamiento " + cita.getTratamiento() + " ha sido cancelada";
                    Utils.crearMensaje(emailSubject, emailBody);
                }
            }
        });
        mensajeCambiarClave.setNegativeButton(R.string.cancelar, null);
        mensajeCambiarClave.show();
    }
}
