package com.example.evaluacion_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PresupuestoAdapter extends RecyclerView.Adapter<PresupuestoAdapter.GastoViewHolder> {

    private Context context;
    private ArrayList<Gasto> listaGastos;

    public PresupuestoAdapter(Context context, ArrayList<Gasto> listaGastos) {
        this.context = context;
        this.listaGastos = listaGastos;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_presupuesto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = listaGastos.get(position);
        holder.bind(gasto);
    }

    @Override
    public int getItemCount() {
        return listaGastos.size();
    }

    public class GastoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombre;
        private TextView textViewPrecio;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textview_nombre);
            textViewPrecio = itemView.findViewById(R.id.textview_precio);
        }

        public void bind(Gasto gasto) {
            textViewNombre.setText(gasto.getNombre());
            textViewPrecio.setText(String.valueOf(gasto.getPrecio()));
        }
    }
}
