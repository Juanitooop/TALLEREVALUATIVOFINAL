/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.dto;


public class AuthResponseDto {
    private Long id;
    private String token;
    private String email;
    private String rol;

    public AuthResponseDto(Long id, String token, String email, String rol) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.rol = rol;
    }

    // Getters, creo que no necesito setters porque no voy a modificar el token, username o rol después de la creación del objeto
    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }
}
