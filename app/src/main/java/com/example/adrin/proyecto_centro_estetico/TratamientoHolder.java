package com.example.adrin.proyecto_centro_estetico;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Adrián on 24/05/2017.
 */

public class TratamientoHolder extends RecyclerView.ViewHolder {
    private View mView;

    public TratamientoHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    /*public void setCategoria(String categoria) {
        TextView field = (TextView) mView.findViewById(R.id.txt_inicio_categoria);
        field.setText(categoria);
    }*/

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

    public void setImagen(Context context, byte[] img) {
        ImageView field = (ImageView) mView.findViewById(R.id.image_tratamiento);
        Glide.with(context)
                .load(img)
                .crossFade()
                .animate(R.anim.img_custom_anim)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.relax)
                .into(field);
    }

    public void infoClick(final String descripcion){
        ImageButton info = (ImageButton)mView.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mostramos el activity de la descripcion del tratamiento
                Intent descripcionIntent = new Intent(view.getContext(), DescripcionActivity.class);
                descripcionIntent.putExtra("descripcion", descripcion);
                view.getContext().startActivity(descripcionIntent);
            }
        });
    }

    public void citaClick(final InicioFragment.OnInicioListener mListener, final String nombreTratamiento){
        Button pedirCita = (Button)mView.findViewById(R.id.cita);
        pedirCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onInicioListener(nombreTratamiento);
            }
        });
    }
}
