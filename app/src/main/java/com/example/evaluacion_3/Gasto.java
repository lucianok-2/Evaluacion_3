package com.example.evaluacion_3;

public class Gasto {
    private long id;
    private String categoria; // Agregada la variable categoria
    private String nombre;
    private float precio;
    private String fecha;
    private double latitud;
    private double longitud;

    public Gasto(String categoria, String nombre, float precio, String fecha, double latitud, double longitud) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.precio = precio;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public String getFecha() {
        return fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
