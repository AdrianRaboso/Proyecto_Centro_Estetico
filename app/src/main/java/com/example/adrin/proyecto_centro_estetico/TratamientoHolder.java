package com.example.adrin.proyecto_centro_estetico;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Adrián on 24/05/2017.
 */

public class TratamientoHolder extends RecyclerView.ViewHolder {
    private View mView;

    public TratamientoHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setCategoria(String categoria) {
        TextView field = (TextView) mView.findViewById(R.id.txt_inicio_categoria);
        field.setText(categoria);
    }

    public void setNombre(String nombre) {
        TextView field = (TextView) mView.findViewById(R.id.txt_inicio_nombre);
        field.setText(nombre);
    }
    public void setDuracion(String duracion) {
        TextView field = (TextView) mView.findViewById(R.id.txt_inicio_duracion);
        field.setText(duracion);
    }

    public void setPrecio(String precio) {
        TextView field = (TextView) mView.findViewById(R.id.txt_inicio_precio);
        field.setText(precio);
    }
    public void setEsteticista(String esteticista) {
        TextView field = (TextView) mView.findViewById(R.id.txt_inicio_esteticista);
        field.setText(esteticista);
    }
}
