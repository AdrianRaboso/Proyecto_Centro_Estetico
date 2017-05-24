package com.example.adrin.proyecto_centro_estetico;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Adrián on 24/05/2017.
 */

public class CitaHolder extends RecyclerView.ViewHolder {
    private View mView;

    public CitaHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setTratamiento(String categoria) {
        TextView field = (TextView) mView.findViewById(R.id.textTratamiento);
        field.setText(categoria);
    }

    public void setFecha(String nombre) {
        TextView field = (TextView) mView.findViewById(R.id.textFecha);
        field.setText(nombre);
    }
}