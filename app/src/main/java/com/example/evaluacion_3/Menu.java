package com.example.evaluacion_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.evaluacion_3.db.DbHelper;

public class Menu extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        float presupuesto = sharedPreferences.getFloat("Presupuesto", 0);

        if (presupuesto == 0) {
            showPresupuestoDialog();
        }

        Button settingsButton = findViewById(R.id.btn_Presupuesto);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            DbHelper dbHelper = new DbHelper(Menu.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            @Override
            public void onClick(View view) {
                openActivity("Presupuesto");
            }
        });

        Button preferencesButton = findViewById(R.id.btn_settings3);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity("Settings");
            }
        });

        Button mapButton = findViewById(R.id.btn_mapa);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity("Mapa");
            }
        });

        Button walletButton = findViewById(R.id.btn_wallet);
        walletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity("Categorias");
            }
        });
    }

    private void openActivity(String activityName) {
        Class<?> activityClass;
        switch (activityName) {
            case "Presupuesto":
                activityClass = Presupuesto.class;
                break;
            case "Settings":
                activityClass = SettingsActivity.class;
                break;
            case "Mapa":
                activityClass = MapActivity.class;
                break;
            case "Categorias":
                activityClass = CategoriaActivity.class;
                break;
            default:
                return;
        }
        Intent intent = new Intent(Menu.this, activityClass);
        startActivity(intent);
    }

    private void showPresupuestoDialog() {
        // Inflar el layout del diálogo
        LayoutInflater inflater = LayoutInflater.from(Menu.this);
        View dialogView = inflater.inflate(R.layout.dialog, null);

        // Obtener las referencias de los elementos del diálogo
        EditText editTextPresupuesto = dialogView.findViewById(R.id.editTextPresupuesto);
        Spinner spinnerFechaPago = dialogView.findViewById(R.id.spinnerFechaPago);

        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
        builder.setView(dialogView)
                .setTitle("Definir Presupuesto")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener el presupuesto ingresado por el usuario
                        int presupuesto = Integer.parseInt(editTextPresupuesto.getText().toString());

                        // Guardar el presupuesto en las preferencias compartidas
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putFloat("Presupuesto", presupuesto);
                        editor.apply();

                        // Abrir la actividad de Presupuesto
                        openActivity("Presupuesto");
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
