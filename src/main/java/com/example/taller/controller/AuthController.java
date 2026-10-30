/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.controller;

import com.example.taller.dto.AuthRequestDto;
import com.example.taller.dto.AuthResponseDto;
import com.example.taller.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody AuthRequestDto peticion) {
        return authService.login(peticion);
    }
}


//angular envia la peticion, este invoca a authservice, que invoca a jwtservice, que genera el token y lo devuelve a angular. IMPORTANTE EL FLUJO!!!