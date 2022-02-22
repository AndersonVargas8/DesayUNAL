package com.example.desayunal.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.print.DocFlavor.STRING;

@Entity
@Table( )
public class Orden {

    public Orden(){}

    public Orden(int dia, int mes, int anio, int horaPedido, String minPedido, String horaEntrega, int precio, String estado, Usuario usuario) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.horaPedido = horaPedido;
        this.minPedido = minPedido;
        this.horaEntrega = horaEntrega;
        this.precio = precio;
        this.estado = estado;
        this.usuario = usuario;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dia", nullable = false)
    @NotNull
    private int dia;

    @Column(name = "mes", nullable = false)
    @NotNull
    private int mes;

    @Column(name = "anio", nullable = false)
    @NotNull
    private int anio;

    @Column(name = "horaPedido", nullable = false)
    @NotNull
    private int horaPedido;

    @Column(name = "minPedido", nullable = false)
    @NotNull
    private String minPedido;

    @Column(name = "horaEntrega", nullable = false)
    @NotNull
    private String horaEntrega;

    @Column(name = "precio", nullable = false)
    @NotNull
    private int precio;

    @Column(name = "estado", nullable = false)
    @NotNull
    private String estado;

    @JoinColumn(name = "fk_usuarioID", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Usuario usuario;

   


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDia() {
        return this.dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return this.mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return this.anio;
    }

    public void setAño(int anio) {
        this.anio = anio;
    }

    public int getHoraPedido() {
        return this.horaPedido;
    }

    
    public void setHoraPedido(int horaPedido) {
        this.horaPedido = horaPedido;
    }
    public String getMinPedido() {
        return this.minPedido;
    }

    public void setMinPedido(String minPedido) {
        this.minPedido = minPedido;
    }

    public String getHoraEntrega() {
        return this.horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public int getPrecio() {
        return this.precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

  

}
