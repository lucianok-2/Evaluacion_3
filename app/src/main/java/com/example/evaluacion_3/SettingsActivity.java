package com.example.evaluacion_3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditTextPreference presupuestoPreference;
    private Preference fechaPagoPreference;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
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

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SharedPreferences sharedPreferences;
        private EditTextPreference presupuestoPreference;
        private Preference fechaPagoPreference;
        private SimpleDateFormat dateFormat;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // Obtener las preferencias de presupuesto y fecha de pago
            presupuestoPreference = findPreference("pref_key_presupuesto");
            fechaPagoPreference = findPreference("pref_key_fecha_pago");

            // Obtener los valores almacenados en SharedPreferences
            int presupuesto = sharedPreferences.getInt("Presupuesto", 0);
            long fechaPago = sharedPreferences.getLong("FechaPago", 0);

            // Establecer los valores en los campos correspondientes
            presupuestoPreference.setText(String.valueOf(presupuesto));
            if (fechaPago > 0) {
                Date fechaPagoDate = new Date(fechaPago);
                String formattedFechaPago = dateFormat.format(fechaPagoDate);
                fechaPagoPreference.setSummary(formattedFechaPago);
            }

            // Establecer el listener para la preferencia de presupuesto
            presupuestoPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int newPresupuesto = Integer.parseInt(newValue.toString());
                    updatePresupuesto(newPresupuesto);
                    return true;
                }
            });

            // Establecer el listener para la preferencia de fecha de pago
            fechaPagoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showFechaPagoDialog();
                    return true;
                }
            });
        }

        private void updatePresupuesto(int presupuesto) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Presupuesto", presupuesto);
            editor.apply();
        }

        private void showFechaPagoDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Seleccionar Fecha de Pago");

            View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_fecha_pago, null);
            builder.setView(view);

            Spinner spinnerFechaPago = view.findViewById(R.id.spinnerFechaPago);
            spinnerFechaPago.setAdapter(ArrayAdapter.createFromResource(requireContext(),
                    R.array.fecha_pago_entries, android.R.layout.simple_spinner_dropdown_item));

            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String fechaPago = spinnerFechaPago.getSelectedItem().toString();
                    guardarFechaPago(fechaPago);
                }
            });

            builder.setNegativeButton("Cancelar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void guardarFechaPago(String fechaPago) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("pref_key_fecha_pago", fechaPago);
            editor.apply();
            fechaPagoPreference.setSummary(fechaPago);
        }

    }
}
