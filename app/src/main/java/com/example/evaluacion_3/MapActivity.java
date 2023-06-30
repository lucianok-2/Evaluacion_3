package com.example.evaluacion_3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.evaluacion_3.db.DbGastos;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private ArrayList<Gasto> listaGastos;
    private DbGastos dbGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Inicializar la base de datos
        dbGastos = new DbGastos(this);

        // Obtener la lista de gastos desde la base de datos
        listaGastos = dbGastos.obtenerTodosLosGastos();

        // Inicializar el MapView
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Agregar marcadores para cada gasto en la lista
        for (Gasto gasto : listaGastos) {
            LatLng latLng = new LatLng(gasto.getLatitud(), gasto.getLongitud());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(gasto.getNombre()).snippet("Precio: $" + gasto.getPrecio()));
        }

        // Ajustar la cámara para mostrar todos los marcadores
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Gasto gasto : listaGastos) {
            builder.include(new LatLng(gasto.getLatitud(), gasto.getLongitud()));
        }
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
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
