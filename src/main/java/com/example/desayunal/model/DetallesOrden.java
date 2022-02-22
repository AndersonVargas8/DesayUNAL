package com.example.desayunal.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( )
public class DetallesOrden implements Serializable {

    public DetallesOrden(){}
    
    public DetallesOrden(Orden ordenID, Producto productoID, int cantidadProducto, int subtotal) {
        this.ordenID = ordenID;
        this.productoID = productoID;
        this.cantidadProducto = cantidadProducto;
        this.subtotal = subtotal;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @JoinColumn(name = "fk_ordenID", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Orden ordenID;

    
    @JoinColumn(name = "fk_productoID", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Producto productoID;


    @Column(name = "cantidadProducto", nullable = false)
    @NotNull
    private int cantidadProducto;

    @Column(name = "subtotal", nullable = false)
    @NotNull
    private int subtotal;

    public Orden getOrdenID() {
        return ordenID;
    }

    public void setOrdenID(Orden ordenID) {
        this.ordenID = ordenID;
    }

    public Producto getProductoID() {
        return productoID;
    }

    public void setProductoID(Producto productoID) {
        this.productoID = productoID;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
