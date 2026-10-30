/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.controller;

import com.example.taller.dto.UsuarioDto;
import com.example.taller.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
        
    @Autowired
    UsuarioService usuarioService;
    
    //obtener todos, paginado
    @GetMapping
    public Page<UsuarioDto> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return usuarioService.getUsuarios(page, size).map(usuario -> UsuarioDto.fromEntity(usuario));
    }
    
    //buscar por nombre o username, paginado
    @GetMapping("/buscar")
    public Page<UsuarioDto> buscar(@RequestParam("texto") String texto, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return usuarioService.buscar(texto, page, size).map(usuario -> UsuarioDto.fromEntity(usuario));
    }
    
    //obtener uno
    @GetMapping("/{usuarioId}")
    public Optional<UsuarioDto> getOne(@PathVariable("usuarioId") Long usuarioId){
        return usuarioService.getUsuario(usuarioId).map(usuario -> UsuarioDto.fromEntity(usuario));
    }
    
    @PostMapping
    public void saveOrUpdate(@Valid @RequestBody UsuarioDto usuarioDto){
        usuarioService.saveOrUpdate(usuarioDto.toEntity());
    }
    
    @DeleteMapping("/{usuarioId}")
    public void delete(@PathVariable("usuarioId") Long usuarioId){
        usuarioService.delete(usuarioId);
    }
}
