/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.service;

import com.example.taller.entity.Autor;
import com.example.taller.repository.AutorRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class AutorService {
    @Autowired
    AutorRepository autorRepository;
    
    //obtener todos, paginado
    public Page<Autor> getAutores(int page, int size){
        return autorRepository.findAll(PageRequest.of(page, size));
    }
    
    //Para encontrar por id
    public Optional<Autor> getAutor(Long id){
        return autorRepository.findById(id);
    }
    
    public void saveOrUpdate(Autor autor){
        autorRepository.save(autor);
    }
    
    public void delete(Long id){
        autorRepository.deleteById(id);
    }
}
