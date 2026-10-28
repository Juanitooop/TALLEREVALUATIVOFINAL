/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import com.example.taller.entity.Usuario;

@Service
public class JwtService {
    private JwtEncoder encoder;
    private Duration vigencia;

    //"${app.jwt.minutos}" --> eso es nuevo. Con eso lo trae del .properties
    public JwtService(JwtEncoder encoder, @Value("${app.jwt.vigencia}") Long minutos) {
        this.encoder = encoder;
        this.vigencia = Duration.ofMinutes(minutos);
    }

    public String generar(Usuario usuario){
        return ""; //Esto ya está más duro, por ahora lo dejo así. Esto es para generar el token, pero no lo vamos a hacer todavía
    }


}