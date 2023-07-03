package com.example.evaluacion_3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.evaluacion_3.db.DbGastos;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private ArrayList<Gasto> listaGastos;
    private ArrayList<Marker> marcadores;
    private DbGastos dbGastos;
    private Button btnDatePicker;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private SimpleDateFormat dateFormatter;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar la base de datos
        dbGastos = new DbGastos(this);

        // Obtener la lista de gastos desde la base de datos
        listaGastos = dbGastos.obtenerTodosLosGastos();

        // Inicializar la lista de marcadores
        marcadores = new ArrayList<>();

        // Inicializar el MapView
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Inicializar el botón de fecha y su listener
        btnDatePicker = findViewById(R.id.btnDatePicker);
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTime();
                filterMapPoints();
            }
        };

        // Establecer el listener para el botón de fecha
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Agregar todos los marcadores a la lista y al mapa
        for (Gasto gasto : listaGastos) {
            double lat = gasto.getLatitud();
            double lng = gasto.getLongitud();

            // Generar un número aleatorio entre -0.0001 y 0.0001 para el desplazamiento en grados
            Random random = new Random();
            double latOffset = (random.nextDouble() - 0.5) / 10000.0; // Rango de -0.0001 a 0.0001 para la latitud
            double lngOffset = (random.nextDouble() - 0.5) / 10000.0; // Rango de -0.0001 a 0.0001 para la longitud

            LatLng latLng = new LatLng(lat + latOffset, lng + lngOffset);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(gasto.getNombre()).snippet("Precio: $" + gasto.getPrecio()));
            marker.setTag(gasto);
            marcadores.add(marker);
        }

        // Ajustar la cámara para mostrar todos los marcadores
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Gasto gasto : listaGastos) {
            LatLng latLng = new LatLng(gasto.getLatitud(), gasto.getLongitud());
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void filterMapPoints() {
        for (Marker marker : marcadores) {
            Gasto gasto = (Gasto) marker.getTag();
            if (selectedDate == null || gasto.getFecha().compareTo(selectedDate) >= 0) {
                marker.setVisible(true);
            } else {
                marker.setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
