package com.bmw.cine.common.model;

public class Usuario {
    public static final int ROL_ADMINISTRADOR = 1;
    public static final int ROL_PERSONAL = 2;
    public static final int ROL_ESPECTADOR = 3;

    private int id;
    private String nombre;
    private String correo;
    private String contrasenaHash;
    private int rol;

    public Usuario() {}

    public Usuario(int id, String nombre, String correo, int rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}

    public String getContrasenaHash() {return contrasenaHash;}
    public void setContrasenaHash(String contrasenaHash) {this.contrasenaHash = contrasenaHash;}

    public int getRol() {return rol;}
    public void setRol(int rol) {this.rol = rol;}


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
