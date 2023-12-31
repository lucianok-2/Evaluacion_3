package com.example.evaluacion_3;

import java.util.Date;

public class Gasto {
    private long id;
    private String categoria;
    private String nombre;
    private int precio;
    private Date fecha;
    private double latitud;
    private double longitud;

    public Gasto(String categoria, String nombre, int precio, Date fecha, double latitud, double longitud) {
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

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setNombre(String nombreGasto) {
        this.nombre = nombreGasto;
    }
}
