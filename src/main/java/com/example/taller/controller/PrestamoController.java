/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.controller;

import com.example.taller.dto.PrestamoDto;
import com.example.taller.service.PrestamoService;
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
@RequestMapping("/prestamos")
public class PrestamoController {
        
    @Autowired
    PrestamoService prestamoService;
    
    @GetMapping
    public Page<PrestamoDto> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return prestamoService.getPrestamos(page, size).map(prestamo -> PrestamoDto.fromEntity(prestamo));
    }
    

    @GetMapping("/{prestamoId}")
    public Optional<PrestamoDto> getOne(@PathVariable("prestamoId") Long prestamoId){
        return prestamoService.getPrestamo(prestamoId).map(prestamo -> PrestamoDto.fromEntity(prestamo));
    }
    
    @PostMapping
    public void saveOrUpdate(@Valid @RequestBody PrestamoDto prestamoDto){
        prestamoService.saveOrUpdate(prestamoDto.toEntity());
    }
    
    @DeleteMapping("/{prestamoId}")
    public void delete(@PathVariable("prestamoId") Long prestamoId){
        prestamoService.delete(prestamoId);
    }
}
