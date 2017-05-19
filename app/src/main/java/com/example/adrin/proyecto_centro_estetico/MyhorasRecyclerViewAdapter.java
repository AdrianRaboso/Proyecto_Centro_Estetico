package com.example.adrin.proyecto_centro_estetico;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adrin.proyecto_centro_estetico.HorasFragment.OnListFragmentInteractionListener;
import com.example.adrin.proyecto_centro_estetico.model.Hora;

import java.util.List;


public class MyhorasRecyclerViewAdapter extends RecyclerView.Adapter<MyhorasRecyclerViewAdapter.ViewHolder> {

    private final List<Hora> mValues;
    private final OnListFragmentInteractionListener mListener;
    private View antiguo = null;

    public MyhorasRecyclerViewAdapter(List<Hora> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_horas, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if (holder.mItem.getHora() < 10) {
            holder.mIdView.setText("0" + mValues.get(position).getHora() + ":00");
        } else {
            holder.mIdView.setText(mValues.get(position).getHora() + ":00");
        }

        //Si esta la hora ocupada la ponemos de color ROJO y no le añadimos el listener
        if (holder.mItem.isOcupada()) {
            holder.mView.setBackgroundColor(Color.RED);
        } else {
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                    //Sirve para cambiar de color la hora elegida
                    if (antiguo == null) {
                        v.setBackgroundColor(Color.CYAN);
                        antiguo = v;
                    } else if (antiguo != v) {
                        v.setBackgroundColor(Color.CYAN);
                        antiguo.setBackgroundColor(Color.WHITE);
                        antiguo = v;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public Hora mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
