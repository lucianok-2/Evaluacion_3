package com.example.evaluacion_3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.evaluacion_3.Gasto;

import java.util.ArrayList;

public class DbGastos {
    private DbHelper dbHelper;

    public DbGastos(Context context) {
        dbHelper = new DbHelper(context);
    }

    public long insertarGasto(Gasto gasto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_CATEGORIA, gasto.getCategoria());
        values.put(DbHelper.COLUMN_NOMBRE_GASTO, gasto.getNombre());
        values.put(DbHelper.COLUMN_PRECIO, gasto.getPrecio());
        values.put(DbHelper.COLUMN_FECHA, gasto.getFecha());
        values.put(DbHelper.COLUMN_LATITUD, gasto.getLatitud());
        values.put(DbHelper.COLUMN_LONGITUD, gasto.getLongitud());
        long id = db.insert(DbHelper.TABLE_GASTOS, null, values);
        db.close();
        return id;
    }

    public ArrayList<Gasto> obtenerTodosLosGastos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Gasto> listaGastos = new ArrayList<>();
        String query = "SELECT * FROM " + DbHelper.TABLE_GASTOS;
        Cursor cursor = db.rawQuery(query, null);

        int columnIndexId = cursor.getColumnIndex(DbHelper.COLUMN_ID);
        int columnIndexCategoria = cursor.getColumnIndex(DbHelper.COLUMN_CATEGORIA);
        int columnIndexNombre = cursor.getColumnIndex(DbHelper.COLUMN_NOMBRE_GASTO);
        int columnIndexPrecio = cursor.getColumnIndex(DbHelper.COLUMN_PRECIO);
        int columnIndexFecha = cursor.getColumnIndex(DbHelper.COLUMN_FECHA);
        int columnIndexLatitud = cursor.getColumnIndex(DbHelper.COLUMN_LATITUD);
        int columnIndexLongitud = cursor.getColumnIndex(DbHelper.COLUMN_LONGITUD);

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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_CATEGORIA, gasto.getCategoria());
        values.put(DbHelper.COLUMN_NOMBRE_GASTO, gasto.getNombre());
        values.put(DbHelper.COLUMN_PRECIO, gasto.getPrecio());
        values.put(DbHelper.COLUMN_FECHA, gasto.getFecha());
        values.put(DbHelper.COLUMN_LATITUD, gasto.getLatitud());
        values.put(DbHelper.COLUMN_LONGITUD, gasto.getLongitud());
        int rowsAffected = db.update(DbHelper.TABLE_GASTOS, values, DbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(gasto.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarGasto(long  id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(DbHelper.TABLE_GASTOS, DbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Gasto obtenerGastoPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Gasto gasto = null;
        String query = "SELECT * FROM " + DbHelper.TABLE_GASTOS + " WHERE " + DbHelper.COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            int columnIndexCategoria = cursor.getColumnIndex(DbHelper.COLUMN_CATEGORIA);
            int columnIndexNombre = cursor.getColumnIndex(DbHelper.COLUMN_NOMBRE_GASTO);
            int columnIndexPrecio = cursor.getColumnIndex(DbHelper.COLUMN_PRECIO);
            int columnIndexFecha = cursor.getColumnIndex(DbHelper.COLUMN_FECHA);
            int columnIndexLatitud = cursor.getColumnIndex(DbHelper.COLUMN_LATITUD);
            int columnIndexLongitud = cursor.getColumnIndex(DbHelper.COLUMN_LONGITUD);

            String categoria = cursor.getString(columnIndexCategoria);
            String nombre = cursor.getString(columnIndexNombre);
            float precio = cursor.getFloat(columnIndexPrecio);
            String fecha = cursor.getString(columnIndexFecha);
            double latitud = cursor.getDouble(columnIndexLatitud);
            double longitud = cursor.getDouble(columnIndexLongitud);

            gasto = new Gasto(categoria, nombre, precio, fecha, latitud, longitud);
            gasto.setId(id);
        }

        cursor.close();
        db.close();
        return gasto;
    }



}



