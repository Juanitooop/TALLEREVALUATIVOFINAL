/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.dto;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author alber
 */
public class AuthRequestDto {
    @NotBlank(message  = "El campo no puede estar vacío")
    private String email;

    @NotBlank(message  = "El campo no puede estar vacío")
    private String password;


    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
