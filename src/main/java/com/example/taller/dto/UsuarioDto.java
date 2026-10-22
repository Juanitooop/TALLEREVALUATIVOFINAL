/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.dto;

import com.example.taller.entity.Estado;
import com.example.taller.entity.Rol;
import com.example.taller.entity.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author alber
 */
public class UsuarioDto {
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El username es obligatorio")
    private String username;
    
    @NotBlank(message = "El email es obligatorio")
    private String email;
    
    //Entra por la API, pero no se devuelve: fromEntity no lo copia
    @NotBlank(message = "La contrasena es obligatoria")
    private String password;
    
    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
    
    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    //Convierte lo que llega por la API en una entity, para poder guardarla
    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNombre(this.nombre);
        usuario.setUsername(this.username);
        usuario.setEmail(this.email);
        usuario.setPasswordHash(this.password);
        usuario.setRol(this.rol);
        usuario.setEstado(this.estado);
        return usuario;
    }
    
    //Convierte la entity que viene de la base en lo que se devuelve por la API
    public static UsuarioDto fromEntity(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setEstado(usuario.getEstado());
        //passwordHash no se copia: la contrasena nunca sale en una respuesta
        return dto;
    }
}
