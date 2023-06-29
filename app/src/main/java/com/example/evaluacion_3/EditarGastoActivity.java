package com.example.evaluacion_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.evaluacion_3.db.DbGastos;

public class EditarGastoActivity extends AppCompatActivity {

    private EditText editTextNombreGasto;
    private EditText editTextCantidadGasto;
    private EditText editTextFecha;
    private Button btnGuardar;
    private Button btnEliminar;
    private Spinner spinnerCategoria;

    private DbGastos dbGastos;
    private Gasto gasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_gasto);

        // Inicializar vistas
        editTextNombreGasto = findViewById(R.id.edittext_nombre_gasto);
        editTextCantidadGasto = findViewById(R.id.edittext_cantidad_gasto);
        editTextFecha = findViewById(R.id.edittext_fecha);
        btnGuardar = findViewById(R.id.btn_guardar_cambios);
        btnEliminar = findViewById(R.id.btn_eliminar_gasto);
        spinnerCategoria = findViewById(R.id.spinner_categoria_editar);

        // Inicializar base de datos
        dbGastos = new DbGastos(this);

        // Obtener el ID del gasto seleccionado de los extras del Intent
        long gastoId = getIntent().getLongExtra("gastoId", -1);
        int gastoIdInt = (int) gastoId;

        if (gastoIdInt != -1) {
            // Obtener el gasto de la base de datos
            gasto = dbGastos.obtenerGastoPorId(gastoIdInt);

            if (gasto != null) {
                // Mostrar los detalles del gasto en los campos de edición
                editTextNombreGasto.setText(gasto.getNombre());
                editTextCantidadGasto.setText(String.valueOf(gasto.getPrecio()));
                editTextFecha.setText(gasto.getFecha());

                // Configurar el spinner con la categoría seleccionada
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        this,
                        R.array.categorias,
                        android.R.layout.simple_spinner_item
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategoria.setAdapter(adapter);
                int categoriaPosition = adapter.getPosition(gasto.getCategoria());
                spinnerCategoria.setSelection(categoriaPosition);

                // Configurar el botón de guardar
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guardarCambios();
                    }
                });

                // Configurar el botón de eliminar
                // Configurar el botón de eliminar
                btnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarGasto(gasto.getId());
                    }
                });

            } else {
                // El gasto no se encontró en la base de datos, mostrar mensaje de error
                Toast.makeText(this, "No se encontró el gasto", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // No se proporcionó el ID del gasto, mostrar mensaje de error
            Toast.makeText(this, "No se proporcionó el ID del gasto", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void guardarCambios() {
        String nombreGasto = editTextNombreGasto.getText().toString();
        String cantidadGastoString = editTextCantidadGasto.getText().toString();
        String fecha = editTextFecha.getText().toString();

        if (!nombreGasto.isEmpty() && !cantidadGastoString.isEmpty()) {
            // Obtener la categoría seleccionada
            String categoria = spinnerCategoria.getSelectedItem().toString();

            // Convertir la cantidad a número flotante
            float cantidadGasto = Float.parseFloat(cantidadGastoString);

            // Actualizar los datos del gasto
            gasto.setNombre(nombreGasto);
            gasto.setPrecio(cantidadGasto);
            gasto.setFecha(fecha);
            gasto.setCategoria(categoria);

            // Actualizar el gasto en la base de datos
            if (dbGastos.editarGasto(gasto)) {
                Toast.makeText(this, "Gasto actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar el gasto", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarGasto(long gastoId) {
        if (dbGastos.eliminarGasto(gastoId)) {
            Toast.makeText(this, "Gasto eliminado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al eliminar el gasto", Toast.LENGTH_SHORT).show();
        }
    }
}
