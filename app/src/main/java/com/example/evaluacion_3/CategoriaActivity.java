package com.example.evaluacion_3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion_3.db.DbGastos;

import java.util.ArrayList;

public class CategoriaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoriaAdapter categoriaAdapter;
    private ArrayList<Categoria> categorias;
    private int presupuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener el presupuesto desde las preferencias compartidas
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        presupuesto = sharedPreferences.getInt("Presupuesto", 0);

        TextView presupuestoTextView = findViewById(R.id.presupuestoTextView);
        presupuestoTextView.setText("Presupuesto: " + presupuesto);

        // Inicializar la lista de categorías
        categorias = new ArrayList<>();

        // Obtener las categorías desde la base de datos
        DbGastos dbGastos = new DbGastos(this);
        ArrayList<String> nombresCategorias = obtenerNombresCategorias(dbGastos);
        for (String nombreCategoria : nombresCategorias) {
            Categoria categoria = new Categoria(nombreCategoria, dbGastos, presupuesto);
            categorias.add(categoria);
        }

        // Crear el adaptador y establecerlo en el RecyclerView
        categoriaAdapter = new CategoriaAdapter(this, categorias);
        recyclerView.setAdapter(categoriaAdapter);


        // Establecer el listener de clics en el adaptador
        categoriaAdapter.setOnItemClickListener(new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Categoria categoria = categorias.get(position);
                Intent intent = new Intent(CategoriaActivity.this, GastosCategoriaActivity.class);
                intent.putExtra("categoria", categoria.getNombre());
                startActivity(intent);
            }
        });

    }

    private ArrayList<String> obtenerNombresCategorias(DbGastos dbGastos) {
        ArrayList<String> nombresCategorias = new ArrayList<>();
        ArrayList<Gasto> gastos = dbGastos.obtenerTodosLosGastos();

        for (Gasto gasto : gastos) {
            String categoria = gasto.getCategoria();
            if (!nombresCategorias.contains(categoria)) {
                nombresCategorias.add(categoria);
            }
        }

        return nombresCategorias;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
