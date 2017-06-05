package com.example.adrin.proyecto_centro_estetico;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Date;


public class FechaFragment extends Fragment {

    private CalendarView fecha;
    private FechaListener listener;

    public FechaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fecha, container, false);
        fecha = (CalendarView) view.findViewById(R.id.datePicker);
        fecha.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                if (listener != null) {
                    listener.onFechaSeleccionado(y, m, d);
                }
            }
        });
        //Ponemos la fecha minima ese mismo dia
        fecha.setMinDate(new Date().getTime());
        // Inflate the layout for this fragment
        return view;
    }

    public interface FechaListener {
        void onFechaSeleccionado(int y, int m, int d);
    }

    public void setFechaListener(FechaListener listener) {
        this.listener = listener;
    }

}
