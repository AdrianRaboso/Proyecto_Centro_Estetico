package com.example.adrin.proyecto_centro_estetico;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Adrián on 03/06/2017.
 */

public class OfertasHolder extends RecyclerView.ViewHolder {
    private View mView;

    public OfertasHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDescripcion(String nombre) {
        TextView field = (TextView) mView.findViewById(R.id.txt_oferta_descripcion);
        field.setText(nombre);
    }

    public void setTema(String tema) {
        TextView field = (TextView) mView.findViewById(R.id.txt_oferta_tema);
        field.setText(tema);
    }

    public void setImagen(Context context, byte[] img) {
        ImageView field = (ImageView) mView.findViewById(R.id.image_oferta);
        Glide.with(context)
                .load(img)
                .crossFade()
                .animate(android.R.anim.slide_out_right)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.relax)
                .into(field);
    }
}
