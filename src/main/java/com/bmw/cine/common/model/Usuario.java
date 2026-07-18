package com.bmw.cine.common.model;

import java.time.LocalDateTime;

public class Usuario {
    public static final int ROL_ADMINISTRADOR = 1;
    public static final int ROL_PERSONAL = 2;
    public static final int ROL_ESPECTADOR = 3;

    private int id;
    private String nombre;
    private String correo;
    private String username;
    private String contrasenaHash;
    private int rol;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    public Usuario() {}

    public Usuario(String nombre, String correo, String username, int rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.username = username;
        this.rol = rol;
        this.activo = true;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getContrasenaHash() {return contrasenaHash;}
    public void setContrasenaHash(String contrasenaHash) {this.contrasenaHash = contrasenaHash;}

    public int getRol() {return rol;}
    public void setRol(int rol) {this.rol = rol;}

    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    public LocalDateTime getFechaRegistro() {return fechaRegistro;}
    public void setFechaRegistro(LocalDateTime fechaRegistro) {this.fechaRegistro = fechaRegistro;}


    public String getNombreRol() {
        switch (rol) {
            case ROL_ADMINISTRADOR:
                return "Administrador";
            case ROL_PERSONAL:
                return "Personal";
            case ROL_ESPECTADOR:
                return "Espectador";
            default:
                return "Desconocido";
        }
    }
}
