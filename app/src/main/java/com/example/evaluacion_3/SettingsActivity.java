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
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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

            // Establecer el resumen de la preferencia de fecha de pago
            long fechaPago = sharedPreferences.getLong("FechaPago", 0);
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
            // Implementa el código para mostrar un diálogo de selección de fecha de pago
            // y actualizar la preferencia correspondiente con la nueva fecha seleccionada.
        }
    }
}
