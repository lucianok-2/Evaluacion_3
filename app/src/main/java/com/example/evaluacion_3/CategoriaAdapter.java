// CategoriaAdapter.java
package com.example.evaluacion_3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion_3.R;

import java.util.ArrayList;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {
    private Context context;
    private ArrayList<Categoria> categorias;

    public CategoriaAdapter(Context context, ArrayList<Categoria> categorias) {
        this.context = context;
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.tvNombre.setText(categoria.getNombre());
        holder.tvTotalGastado.setText(String.valueOf(categoria.getTotalGastado()));
        holder.tvPorcentaje.setText((categoria.getPorcentaje()) + "%");

        // Cambiar el color del porcentaje según el presupuesto
        if (categoria.getPorcentaje() > 100) {
            holder.tvPorcentaje.setTextColor(Color.RED);
        } else {
            holder.tvPorcentaje.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvTotalGastado;
        TextView tvPorcentaje;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTotalGastado = itemView.findViewById(R.id.tvTotalGastado);
            tvPorcentaje = itemView.findViewById(R.id.tvPorcentaje);
        }
    }
}
