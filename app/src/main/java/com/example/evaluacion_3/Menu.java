package com.example.evaluacion_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.evaluacion_3.db.DbHelper;

public class Menu extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Button settingsButton = findViewById(R.id.btn_Presupuesto);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            DbHelper dbHelper = new DbHelper(Menu.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            @Override
            public void onClick(View v) {
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
            default:
                return;
        }
        Intent intent = new Intent(Menu.this, activityClass);
        startActivity(intent);
    }
}
