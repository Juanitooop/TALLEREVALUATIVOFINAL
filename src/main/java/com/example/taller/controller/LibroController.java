/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.controller;

import com.example.taller.dto.LibroDto;
import com.example.taller.service.LibroService;
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
@RequestMapping("/libros") //Aqui llega mi peticion de angular
public class LibroController {
        
    @Autowired
    LibroService libroService;
    
  
    @GetMapping //GET consulta
    public Page<LibroDto> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return libroService.getLibros(page, size).map(libro -> LibroDto.fromEntity(libro));
    }
    
    @GetMapping("/buscar") 
    public Page<LibroDto> buscar(@RequestParam("texto") String texto, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return libroService.buscar(texto, page, size).map(libro -> LibroDto.fromEntity(libro));
    }
    
    @GetMapping("/{libroId}")
    public Optional<LibroDto> getOne(@PathVariable("libroId") Long libroId){
        return libroService.getLibro(libroId).map(libro -> LibroDto.fromEntity(libro));
    }
    
    @PostMapping //POST guarda o actualiza
    public void saveOrUpdate(@Valid @RequestBody LibroDto libroDto){
        libroService.saveOrUpdate(libroDto.toEntity());
    }
    
    @DeleteMapping("/{libroId}") //DELETE elimina
    public void delete(@PathVariable("libroId") Long libroId){
        libroService.delete(libroId);
    }
}
