package org.das.das_grupo;

/**
 * Created by Alberto on 14/05/2015.
 */
public class Etiqueta {
    private int id, cantidad;
    private String nombre;

    public Etiqueta(int pId, String pNombre, int pCantidad){
        id = pId;
        nombre = pNombre;
        cantidad = pCantidad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }
}
