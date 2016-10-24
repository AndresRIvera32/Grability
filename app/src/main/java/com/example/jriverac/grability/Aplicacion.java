package com.example.jriverac.grability;

import android.graphics.Bitmap;

/**
 * Created by jriverac on 18/10/2016.
 */

public class Aplicacion {

    private String nombre;

    private String imagen;

    private String detalle;

    private String categoria;

    private String precio;

    private Bitmap result;

    public Aplicacion(String nombre, String imagen, String detalle, String categoria, String precio) {
        this.detalle = detalle;
        this.imagen = imagen;
        this.nombre = nombre;
        this.categoria=categoria;
        this.precio=precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Bitmap getResult() {
        return result;
    }

    public void setResult(Bitmap result) {
        this.result = result;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

}
