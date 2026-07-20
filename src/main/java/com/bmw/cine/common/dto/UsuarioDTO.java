package com.bmw.cine.common.dto;

import com.bmw.cine.common.model.Usuario;

public class UsuarioDTO {
    private final int id;
    private final String nombre;
    private final String correo;
    private final String username;
    private final int rol;
    private final boolean activo;

    public UsuarioDTO(int id, String nombre, String correo, String username, int rol, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.username = username;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() {return id;}
    public String getNombre() {return nombre;}
    public String getCorreo() {return correo;}
    public String getUsername() {return username;}
    public int getRol() {return rol;}
    public boolean isActivo() {return activo;}

    // Nombre legible del rol, para headers/subtítulos en las vistas.
    public String getNombreRol() {
        switch (rol) {
            case Usuario.ROL_ADMINISTRADOR:
                return "Administrador";
            case Usuario.ROL_PERSONAL:
                return "Personal";
            case Usuario.ROL_ESPECTADOR:
                return "Espectador";
            default:
                return "Desconocido";
        }
    }
}
