package com.example.evaluacion_3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gastos.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_GASTOS = "gastos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORIA = "categoria";
    public static final String COLUMN_NOMBRE_GASTO = "nombre_gasto";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_LATITUD = "latitud";
    public static final String COLUMN_LONGITUD = "longitud";
    public static final String COLUMN_FECHA = "fecha";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_GASTOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORIA + " TEXT NOT NULL, " +
                COLUMN_NOMBRE_GASTO + " TEXT NOT NULL, " +
                COLUMN_PRECIO + " REAL NOT NULL, " +
                COLUMN_LATITUD + " REAL, " +
                COLUMN_LONGITUD + " REAL, " +
                COLUMN_FECHA + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASTOS);
        onCreate(db);
    }
}

