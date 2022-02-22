package com.example.desayunal.model;

import javax.persistence.*;

@Entity
@Table(name = "Imagen")
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int id_producto;
    private String nombre;
    private String tipo;
    private Long tamano;
    @Lob
    @Column(name = "pixel")
    private byte[] pixel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Imagen() {
    }

    public Imagen(int idProducto, String nombre, String tipo, Long tamano, byte[] pixel) {
        this.id_producto = idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.tamano = tamano;
        this.pixel = pixel;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int idProducto) {
        this.id_producto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getTamano() {
        return tamano;
    }

    public void setTamano(Long tamano) {
        this.tamano = tamano;
    }

    public byte[] getPixel() {
        return pixel;
    }

    public void setPixel(byte[] pixel) {
        this.pixel = pixel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
