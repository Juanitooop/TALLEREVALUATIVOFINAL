/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.controller;

import com.example.taller.dto.AutorDto;
import com.example.taller.service.AutorService;
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

/**
 *
 * @author alber
 */
@RestController
@RequestMapping("/autores")
public class AutorController {
        
    @Autowired
    AutorService autorService;
    
    @GetMapping
    public Page<AutorDto> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return autorService.getAutores(page, size).map(autor -> AutorDto.fromEntity(autor));
    }
    
    //obtener uno
    @GetMapping("/{autorId}")
    public Optional<AutorDto> getOne(@PathVariable("autorId") Long autorId){
        return autorService.getAutor(autorId).map(autor -> AutorDto.fromEntity(autor));
    }
    
    @PostMapping
    public void saveOrUpdate(@Valid @RequestBody AutorDto autorDto){
        autorService.saveOrUpdate(autorDto.toEntity());
    }
    
    @DeleteMapping("/{autorId}")
    public void delete(@PathVariable("autorId") Long autorId){
        autorService.delete(autorId);
    }
}
