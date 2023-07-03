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
        int presupuesto = sharedPreferences.getInt("Presupuesto", 0);

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

        // Obtener la fecha de pago inicial de las preferencias
        String fechaPagoInicial = sharedPreferences.getString("pref_key_fecha_pago", "Pago el 5");

        // Establecer la fecha de pago inicial en el Spinner
        int spinnerPosition = getSpinnerPosition(spinnerFechaPago, fechaPagoInicial);
        spinnerFechaPago.setSelection(spinnerPosition);

        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
        builder.setView(dialogView)
                .setTitle("Definir Presupuesto")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener el presupuesto ingresado por el usuario
                        int newPresupuesto = Integer.parseInt(editTextPresupuesto.getText().toString());

                        // Actualizar el presupuesto en las preferencias
                        updatePresupuesto(newPresupuesto);

                        // Obtener la fecha de pago seleccionada por el usuario
                        String fechaPagoSeleccionada = spinnerFechaPago.getSelectedItem().toString();

                        // Guardar la fecha de pago inicial en las preferencias
                        guardarFechaPagoInicial(fechaPagoSeleccionada);

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

    private int getSpinnerPosition(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private void updatePresupuesto(int presupuesto) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Presupuesto", presupuesto);
        editor.apply();
    }

    private void guardarFechaPagoInicial(String fechaPagoInicial) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_key_fecha_pago", fechaPagoInicial);
        editor.apply();
    }
}
