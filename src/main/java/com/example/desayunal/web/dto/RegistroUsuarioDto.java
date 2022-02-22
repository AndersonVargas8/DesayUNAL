package com.example.desayunal.web.dto;

public class RegistroUsuarioDto 
{
    private String userName;
    private String password;
    private String estado;
    private String role;

    
    public RegistroUsuarioDto(String userName, String password, String estado, String role) {
        this.userName = userName;
        this.password = password;
        this.estado = estado;
        this.role = role;
    }

    public RegistroUsuarioDto() {
        
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
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
