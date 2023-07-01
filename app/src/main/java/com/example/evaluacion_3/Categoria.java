package com.example.evaluacion_3;

import com.example.evaluacion_3.db.DbGastos;

import java.util.ArrayList;

public class Categoria {
    private String nombre;
    private int totalGastado;
    private int porcentaje;


    public Categoria(String nombre, DbGastos dbGastos, int presupuesto) {
        this.nombre = nombre;
        this.totalGastado = calcularTotalGastado(nombre, dbGastos);
        this.porcentaje = calcularPorcentaje(totalGastado, presupuesto);
    }

    public String getNombre() {
        return nombre;
    }

    public int getTotalGastado() {
        return totalGastado;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    private int calcularTotalGastado(String nombreCategoria, DbGastos dbGastos) {
        ArrayList<Gasto> gastos = dbGastos.obtenerTodosLosGastos();
        int total = 0;

        for (Gasto gasto : gastos) {
            if (gasto.getCategoria().equals(nombreCategoria)) {
                total += gasto.getPrecio();
            }
        }

        return total;
    }

    private int calcularPorcentaje(int totalGastado, int presupuesto) {
        if (presupuesto == 0) {
            return 0;
        }

        double porcentaje = ((double) totalGastado / presupuesto) * 100;
        return (int) porcentaje;
    }
}
