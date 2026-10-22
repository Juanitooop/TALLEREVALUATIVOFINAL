/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.service;

import com.example.taller.entity.Libro;
import com.example.taller.repository.LibroRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author alber
 */
@Service
public class LibroService {
    @Autowired
    LibroRepository libroRepository;
    
    //obtener todos, paginado
    public Page<Libro> getLibros(int page, int size){
        return libroRepository.findAll(PageRequest.of(page, size));
    }
    
    //buscar por titulo o por autor, paginado
    public Page<Libro> buscar(String texto, int page, int size){
        return libroRepository.buscar(texto, PageRequest.of(page, size));
    }
    
    //obtener uno
    public Optional<Libro> getLibro(Long id){
        return libroRepository.findById(id);
    }
    
    public void saveOrUpdate(Libro libro){
        libroRepository.save(libro);
    }
    
    public void delete(Long id){
        libroRepository.deleteById(id);
    }
}
