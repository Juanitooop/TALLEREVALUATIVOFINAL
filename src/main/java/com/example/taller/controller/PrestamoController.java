/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.controller;

import com.example.taller.dto.PrestamoDto;
import com.example.taller.entity.Rol;
import com.example.taller.entity.Usuario;
import com.example.taller.repository.UsuarioRepository;
import com.example.taller.service.PrestamoService;
import jakarta.validation.Valid;
import com.example.taller.entity.Prestamo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/prestamos")
public class PrestamoController {
        
    @Autowired
    PrestamoService prestamoService;
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
    @GetMapping
    public Page<PrestamoDto> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return prestamoService.getPrestamos(page, size).map(prestamo -> PrestamoDto.fromEntity(prestamo));
    }
    

    @GetMapping("/{prestamoId}")
    public PrestamoDto getOne(@PathVariable("prestamoId") Long prestamoId, JwtAuthenticationToken jwt){
        // Primero comprobamos si existe: un ID inexistente devuelve 404.
        Prestamo prestamo = prestamoService.getPrestamo(prestamoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestamo no encontrado"));

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(jwt.getToken().getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        // ADMIN puede consultar cualquiera; LECTOR solo puede consultar uno suyo.
        if (usuario.getRol() != Rol.ADMIN
                && (usuario.getRol() != Rol.LECTOR || !usuario.getId().equals(prestamo.getUsuarioId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes consultar este prestamo");
        }

        return PrestamoDto.fromEntity(prestamo);
    }
    
    @PostMapping
    public void saveOrUpdate(@Valid @RequestBody PrestamoDto prestamoDto, JwtAuthenticationToken jwt){
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(jwt.getToken().getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        if (usuario.getRol() == Rol.LECTOR) {
            if (prestamoDto.getId() == null) {
                prestamoDto.setUsuarioId(usuario.getId());
                prestamoDto.setDevuelto(false);
            } else {
                // Un lector solo puede devolver un prestamo suyo.
                var actual = prestamoService.getPrestamo(prestamoDto.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestamo no encontrado"));
                if (!usuario.getId().equals(actual.getUsuarioId())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El prestamo pertenece a otro usuario");
                }
                prestamoDto.setLibroId(actual.getLibroId());
                prestamoDto.setUsuarioId(actual.getUsuarioId());
                prestamoDto.setFechaPrestamo(actual.getFechaPrestamo());
                prestamoDto.setDevuelto(true);
                prestamoDto.setFechaDevolucion(java.time.LocalDate.now());
            }
        }
        
        prestamoService.saveOrUpdate(prestamoDto.toEntity());
    }
    
    @DeleteMapping("/{prestamoId}")
    public void delete(@PathVariable("prestamoId") Long prestamoId){
        prestamoService.delete(prestamoId);
    }
}
