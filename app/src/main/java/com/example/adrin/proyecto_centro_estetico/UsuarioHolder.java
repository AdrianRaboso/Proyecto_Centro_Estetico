package com.example.adrin.proyecto_centro_estetico;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Adrián on 12/06/2017.
 */

public class UsuarioHolder extends RecyclerView.ViewHolder {

    private View mView;

    public UsuarioHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setNombre(String categoria) {
        TextView field = (TextView) mView.findViewById(R.id.textTratamiento);
        field.setText(categoria);
    }

    public void setCorreo(String nombre) {
        TextView field = (TextView) mView.findViewById(R.id.textFecha);
        field.setText(nombre);
    }

}
