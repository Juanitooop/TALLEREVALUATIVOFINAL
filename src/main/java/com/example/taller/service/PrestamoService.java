/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller.service;

import com.example.taller.entity.Prestamo;
import com.example.taller.repository.PrestamoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class PrestamoService {
    @Autowired
    PrestamoRepository prestamoRepository;
    
    //obtener todos, paginado
    public Page<Prestamo> getPrestamos(int page, int size){
        return prestamoRepository.findAll(PageRequest.of(page, size));
    }
    
    //obtener uno
    public Optional<Prestamo> getPrestamo(Long id){
        return prestamoRepository.findById(id);
    }
    
    public void saveOrUpdate(Prestamo prestamo){
        prestamoRepository.save(prestamo);
    }
    
    public void delete(Long id){
        prestamoRepository.deleteById(id);
    }
}
