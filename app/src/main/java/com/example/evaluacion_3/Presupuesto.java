package com.example.evaluacion_3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

    private static final int REQUEST_CODE_PERMISSION = 1;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

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

        // Verificar y solicitar permisos de ubicación si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, REQUEST_CODE_PERMISSION);
            }
        }

        // Configurar el botón de enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener datos del formulario
                String nombreGasto = editTextNombreGasto.getText().toString();
                String cantidadGastoString = editTextCantidadGasto.getText().toString();
                String fecha = editTextFecha.getText().toString();
                Log.d("numero",cantidadGastoString);

                if (!nombreGasto.isEmpty() && !cantidadGastoString.isEmpty()) {
                    // Obtener categoría seleccionada
                    String categoria = spinnerCategoria.getSelectedItem().toString();

                    // Convertir cantidad a número flotante
                    int cantidadGasto = Integer.parseInt(cantidadGastoString);
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
        startActivityForResult(intent, REQUEST_CODE_EDITAR_GASTO);
    }

    private void actualizarListaGastos() {
        listaGastos.clear();
        listaGastos.addAll(dbGastos.obtenerTodosLosGastos());
        gastoAdapter.notifyDataSetChanged();
    }

    private double obtenerLatitud() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                return location.getLatitude();
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se han concedido los permisos de ubicación", Toast.LENGTH_SHORT).show();
        }
        return 0.0;
    }

    private double obtenerLongitud() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                return location.getLongitude();
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se han concedido los permisos de ubicación", Toast.LENGTH_SHORT).show();
        }
        return 0.0;
    }

    private void limpiarCampos() {
        editTextNombreGasto.setText("");
        editTextCantidadGasto.setText("");
        editTextFecha.setText("");
    }

    private static final int REQUEST_CODE_EDITAR_GASTO = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDITAR_GASTO) {
            if (resultCode == RESULT_OK) {
                // Actualizar la lista de gastos al regresar de la Activity de edición
                actualizarListaGastos();
            }
        }
    }
}
