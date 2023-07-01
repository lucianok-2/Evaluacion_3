package com.example.evaluacion_3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion_3.db.DbGastos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GastosCategoriaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PresupuestoAdapter gastosAdapter;
    private ArrayList<Gasto> gastos;
    private String categoria;
    private Button btnSelectDate;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_categoria);

        // Obtener el nombre de la categoría seleccionada del Intent
        categoria = getIntent().getStringExtra("categoria");

        // Configurar el título de la actividad con el nombre de la categoría
        setTitle("Gastos de " + categoria);

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewGastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener los gastos de la categoría desde la base de datos
        DbGastos dbGastos = new DbGastos(this);
        gastos = dbGastos.obtenerGastosPorCategoria(categoria);

        // Crear el adaptador y establecerlo en el RecyclerView
        gastosAdapter = new PresupuestoAdapter(this, gastos);
        recyclerView.setAdapter(gastosAdapter);

        // Calcular y mostrar el total de gastos de la categoría
        double totalGastado = calcularTotalGastado(gastos);
        TextView totalGastadoTextView = findViewById(R.id.textView);
        totalGastadoTextView.setText("Total gastado: " + totalGastado);

        // Configurar el botón para seleccionar la fecha
        btnSelectDate = findViewById(R.id.btnSelectDate);
        selectedDate = Calendar.getInstance();

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private double calcularTotalGastado(ArrayList<Gasto> gastos) {
        double total = 0;
        for (Gasto gasto : gastos) {
            total += gasto.getPrecio();
        }
        return total;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Filtrar los gastos por la fecha seleccionada
                        filtrarGastosPorFecha();
                    }
                },
                year,
                month,
                dayOfMonth
        );
        datePickerDialog.show();
    }

    private void filtrarGastosPorFecha() {
        // Obtener la fecha seleccionada
        long fechaSeleccionada = selectedDate.getTimeInMillis();

        // Filtrar los gastos por la fecha seleccionada
        ArrayList<Gasto> gastosFiltrados = new ArrayList<>();
        for (Gasto gasto : gastos) {
            if (gasto.getFecha().equals(new Date(fechaSeleccionada))) {
                gastosFiltrados.add(gasto);
            }
        }

        // Actualizar el adaptador con los gastos filtrados
        gastosAdapter.setGastos(gastosFiltrados);
        gastosAdapter.notifyDataSetChanged();
    }

}
