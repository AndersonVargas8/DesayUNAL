package com.example.desayunal.model;

public class Carrito {
    int id;
    int idProducto;
    String nombre;
    String descripcion;
    int precioCompra;
    int cantidad;
    int subTotal;

    public Carrito(){}


    public Carrito(int item, int idProducto, String nombre, String descripcion, int precioCompra, int cantidad, int subTotal) {
        this.id = item;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
    }
    

    public int getId() {
        return this.id;
    }

    public void setId(int item) {
        this.id = item;
    }

    public int getIdProducto() {
        return this.idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecioCompra() {
        return this.precioCompra;
    }

    public void setPrecioCompra(int precioCompra) {
        this.precioCompra = precioCompra;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getSubTotal() {
        return this.subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

}
