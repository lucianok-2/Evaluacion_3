package com.example.evaluacion_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion_3.db.DbGastos;

import java.util.ArrayList;

public class Presupuesto extends AppCompatActivity implements PresupuestoAdapter.OnItemClickListener {

    private EditText editTextNombreGasto;
    private EditText editTextCantidadGasto;
    private EditText editTextFecha;
    private Button btnEnviar;
    private Spinner spinnerCategoria;

    private DbGastos dbGastos;
    private ArrayList<Gasto> listaGastos;
    private PresupuestoAdapter gastoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);

        // Inicializar vistas
        editTextNombreGasto = findViewById(R.id.edittext_nombre_gasto);
        editTextCantidadGasto = findViewById(R.id.edittext_cantidad_gasto);
        editTextFecha = findViewById(R.id.edittext_fecha);
        btnEnviar = findViewById(R.id.btnEnviar);
        spinnerCategoria = findViewById(R.id.spinner_categoria);

        // Inicializar base de datos y adaptador
        dbGastos = new DbGastos(this);
        listaGastos = new ArrayList<>();
        gastoAdapter = new PresupuestoAdapter(this, listaGastos);

        // Configurar RecyclerView
        RecyclerView recyclerViewMontos = findViewById(R.id.recyclerview_montos);
        recyclerViewMontos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMontos.setAdapter(gastoAdapter);

        // Configurar el listener del adaptador
        gastoAdapter.setOnItemClickListener(this);

        // Actualizar la lista de gastos
        actualizarListaGastos();

        // Configurar el botón de enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener datos del formulario
                String nombreGasto = editTextNombreGasto.getText().toString();
                String cantidadGastoString = editTextCantidadGasto.getText().toString();
                String fecha = editTextFecha.getText().toString();

                if (!nombreGasto.isEmpty() && !cantidadGastoString.isEmpty()) {
                    // Obtener categoría seleccionada
                    String categoria = spinnerCategoria.getSelectedItem().toString();

                    // Convertir cantidad a número flotante
                    float cantidadGasto = Float.parseFloat(cantidadGastoString);
                    double latitud = obtenerLatitud(); // Obtener la latitud desde el GPS
                    double longitud = obtenerLongitud(); // Obtener la longitud desde el GPS

                    // Crear nuevo gasto
                    Gasto gasto = new Gasto(categoria, nombreGasto, cantidadGasto, fecha, latitud, longitud);
                    long id = dbGastos.insertarGasto(gasto);

                    if (id != -1) {
                        gasto.setId(id);
                        listaGastos.add(gasto);
                        gastoAdapter.notifyDataSetChanged();
                        limpiarCampos();
                    } else {
                        Toast.makeText(Presupuesto.this, "Error al guardar el gasto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Presupuesto.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(long gastoId) {
        // Abre la ventana de edición con el ID del gasto seleccionado
        Intent intent = new Intent(Presupuesto.this, EditarGastoActivity.class);
        intent.putExtra("gastoId", gastoId);
        startActivity(intent);
    }

    private void actualizarListaGastos() {
        listaGastos.clear();
        listaGastos.addAll(dbGastos.obtenerTodosLosGastos());
        gastoAdapter.notifyDataSetChanged();
    }

    private double obtenerLatitud() {
        // Código para obtener la latitud desde el GPS
        return 0.0;
    }

    private double obtenerLongitud() {
        // Código para obtener la longitud desde el GPS
        return 0.0;
    }

    private void limpiarCampos() {
        editTextNombreGasto.setText("");
        editTextCantidadGasto.setText("");
        editTextFecha.setText("");
    }
}
