package com.example.desayunal.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table( )
public class Usuario {

    public Usuario(){
        
    }
    public Usuario(String userName, String password, String estado, String role) {
        super();
        this.userName = userName;
        this.password = password;
        this.estado = estado;
        this.role = role;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userName", nullable = false) /* llamarlo nombre o username??*/
    @NotNull
    private String userName;

    @Column(name = "password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "estado", nullable = false)
    @NotNull
    private String estado;

    @Column(name = "role", nullable = false)
    @NotNull
    private String role;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
