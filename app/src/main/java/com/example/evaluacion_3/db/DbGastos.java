package com.example.evaluacion_3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.evaluacion_3.Gasto;
import com.example.evaluacion_3.Presupuesto;

import java.util.ArrayList;

public class DbGastos extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gastos.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_GASTOS = "gastos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORIA = "categoria";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_PRECIO = "precio";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_LATITUD = "latitud";
    private static final String COLUMN_LONGITUD = "longitud";

    public DbGastos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GASTOS_TABLE = "CREATE TABLE " + TABLE_GASTOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATEGORIA + " TEXT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_PRECIO + " REAL,"
                + COLUMN_FECHA + " TEXT,"
                + COLUMN_LATITUD + " REAL,"
                + COLUMN_LONGITUD + " REAL"
                + ")";
        db.execSQL(CREATE_GASTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASTOS);
        onCreate(db);
    }

    public long insertarGasto(Gasto gasto) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORIA, gasto.getCategoria());
        values.put(COLUMN_NOMBRE, gasto.getNombre());
        values.put(COLUMN_PRECIO, gasto.getPrecio());
        values.put(COLUMN_FECHA, gasto.getFecha());
        values.put(COLUMN_LATITUD, gasto.getLatitud());
        values.put(COLUMN_LONGITUD, gasto.getLongitud());
        long id = db.insert(TABLE_GASTOS, null, values);
        db.close();
        return id;
    }

    public ArrayList<Gasto> obtenerTodosLosGastos() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Gasto> listaGastos = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_GASTOS;
        Cursor cursor = db.rawQuery(query, null);

        int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
        int columnIndexCategoria = cursor.getColumnIndex(COLUMN_CATEGORIA);
        int columnIndexNombre = cursor.getColumnIndex(COLUMN_NOMBRE);
        int columnIndexPrecio = cursor.getColumnIndex(COLUMN_PRECIO);
        int columnIndexFecha = cursor.getColumnIndex(COLUMN_FECHA);
        int columnIndexLatitud = cursor.getColumnIndex(COLUMN_LATITUD);
        int columnIndexLongitud = cursor.getColumnIndex(COLUMN_LONGITUD);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(columnIndexId);
            String categoria = cursor.getString(columnIndexCategoria);
            String nombre = cursor.getString(columnIndexNombre);
            float precio = cursor.getFloat(columnIndexPrecio);
            String fecha = cursor.getString(columnIndexFecha);
            double latitud = cursor.getDouble(columnIndexLatitud);
            double longitud = cursor.getDouble(columnIndexLongitud);

            Gasto gasto = new Gasto(categoria, nombre, precio, fecha, latitud, longitud);
            gasto.setId(id);
            listaGastos.add(gasto);
        }

        cursor.close();
        db.close();
        return listaGastos;
    }

    public boolean editarGasto(Gasto gasto) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORIA, gasto.getCategoria());
        values.put(COLUMN_NOMBRE, gasto.getNombre());
        values.put(COLUMN_PRECIO, gasto.getPrecio());
        values.put(COLUMN_FECHA, gasto.getFecha());
        values.put(COLUMN_LATITUD, gasto.getLatitud());
        values.put(COLUMN_LONGITUD, gasto.getLongitud());
        int rowsAffected = db.update(TABLE_GASTOS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(gasto.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarGasto(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete(TABLE_GASTOS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }
}
