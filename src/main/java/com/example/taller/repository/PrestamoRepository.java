/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.taller.repository;

import com.example.taller.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PrestamoRepository extends JpaRepository<Prestamo, Long>{
    
}
