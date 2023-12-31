package com.example.evaluacion_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion_3.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PresupuestoAdapter extends RecyclerView.Adapter<PresupuestoAdapter.GastoViewHolder> {

    private Context context;
    private ArrayList<Gasto> listaGastos;
    private OnItemClickListener listener;

    public PresupuestoAdapter(Context context, ArrayList<Gasto> listaGastos) {
        this.context = context;
        this.listaGastos = listaGastos;
    }

    public void setGastos(ArrayList<Gasto> gastos) {
        listaGastos = gastos;


    }

    public interface OnItemClickListener {
        void onItemClick(long gastoId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(gasto.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaGastos.size();
    }

    public class GastoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCategoria;
        private TextView textViewNombre;
        private TextView textViewFecha;
        private TextView textViewLatitud;
        private TextView textViewLongitud;
        private TextView textViewPrecio;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoria = itemView.findViewById(R.id.textview_categoria);
            textViewNombre = itemView.findViewById(R.id.textview_nombre);
            textViewFecha = itemView.findViewById(R.id.textview_fecha);
            textViewLatitud = itemView.findViewById(R.id.textview_latitud);
            textViewLongitud = itemView.findViewById(R.id.textview_longitud);
            textViewPrecio = itemView.findViewById(R.id.textview_precio);
        }

        public void bind(Gasto gasto) {
            textViewCategoria.setText(gasto.getCategoria());
            textViewNombre.setText(gasto.getNombre());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = dateFormat.format(gasto.getFecha());
            textViewFecha.setText(fechaFormateada);

            textViewLatitud.setText(String.valueOf(gasto.getLatitud()));
            textViewLongitud.setText(String.valueOf(gasto.getLongitud()));

            DecimalFormat decimalFormat = new DecimalFormat("#");
            String precioFormateado = decimalFormat.format(gasto.getPrecio());

            textViewPrecio.setText(precioFormateado);
        }

    }
}
